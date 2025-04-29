package com.onezol.vertx.framework.component.notice.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.exception.InvalidParameterException;
import com.onezol.vertx.framework.common.model.DataPairRecord;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.common.util.BeanUtils;
import com.onezol.vertx.framework.common.util.StringUtils;
import com.onezol.vertx.framework.component.notice.enumeration.NoticeStatus;
import com.onezol.vertx.framework.component.notice.mapper.NoticeMapper;
import com.onezol.vertx.framework.component.notice.model.Notice;
import com.onezol.vertx.framework.component.notice.model.NoticeEntity;
import com.onezol.vertx.framework.component.notice.model.NoticeQueryPayload;
import com.onezol.vertx.framework.component.notice.model.NoticeSavePayload;
import com.onezol.vertx.framework.security.api.model.dto.User;
import com.onezol.vertx.framework.security.api.service.UserInfoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Service
public class NoticeService {

    private final NoticeMapper noticeMapper;
    private final UserInfoService userInfoService;

    public NoticeService(NoticeMapper noticeMapper, UserInfoService userInfoService) {
        this.noticeMapper = noticeMapper;
        this.userInfoService = userInfoService;
    }

    /**
     * 创建或更新通知公告
     *
     * @param payload 通知公告信息
     */
    public void createOrUpdate(NoticeSavePayload payload) {
        LocalDateTime effectiveTime = payload.getEffectiveTime();
        LocalDateTime terminateTime = payload.getTerminateTime();
        // 生效时间 不能大于 终止时间
        if (effectiveTime != null && terminateTime != null && effectiveTime.isAfter(terminateTime)) {
            throw new InvalidParameterException("生效时间不能大于终止时间");
        }
        NoticeEntity entity = BeanUtils.toBean(payload, NoticeEntity.class);
        if (entity.getId() == null) {
            noticeMapper.insert(entity);
        } else {
            noticeMapper.updateById(entity);
        }
    }

    /**
     * 删除通知公告
     *
     * @param id 通知公告ID
     */
    public void deleteById(Long id) {
        noticeMapper.deleteById(id);
    }

    /**
     * 根据ID获取通知公告
     *
     * @param id 通知公告ID
     */
    public Notice getById(Long id) {
        NoticeEntity entity = noticeMapper.selectById(id);
        Notice notice = BeanUtils.toBean(entity, Notice.class);
        Long creatorId = entity.getCreator();
        User user = userInfoService.getUserById(creatorId);
        DataPairRecord userInfo = new DataPairRecord(user.getId(), user.getNickname());
        notice.setPublisher(userInfo);
        return notice;
    }

    /**
     * 获取通知公告列表
     */
    public PagePack<Notice> getPage(Page<NoticeEntity> page, NoticeQueryPayload queryPayload) {
        Page<NoticeEntity> quriedPage = noticeMapper.selectPage(
                page,
                Wrappers.<NoticeEntity>lambdaQuery()
                        .eq(queryPayload.getId() != null, NoticeEntity::getId, queryPayload.getId())
                        .like(StringUtils.isNotBlank(queryPayload.getTitle()), NoticeEntity::getTitle, queryPayload.getTitle())
                        .eq(Objects.nonNull(queryPayload.getType()), NoticeEntity::getType, queryPayload.getType())
        );
        PagePack<Notice> pack = PagePack.from(quriedPage, Notice.class);
        Collection<Notice> items = pack.getItems();
        for (Notice item : items) {
            item.setStatus(calculateStatus(item.getEffectiveTime(), item.getTerminateTime()));
        }
        return pack;
    }

    /**
     * 根据开始时间和终止时间计算状态
     *
     * @param effectiveTime 开始时间
     * @param terminateTime 终止时间
     */
    public Integer calculateStatus(LocalDateTime effectiveTime, LocalDateTime terminateTime) {
        if (Objects.isNull(effectiveTime)) {
            return NoticeStatus.PENDING.getValue();
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(effectiveTime)) {
            // 待发布
            return NoticeStatus.PENDING.getValue();
        } else if (now.isAfter(terminateTime)) {
            // 已过期
            return NoticeStatus.EXPIRED.getValue();
        } else {
            // 已发布(生效中)
            return NoticeStatus.PUBLISHED.getValue();
        }
    }

}
