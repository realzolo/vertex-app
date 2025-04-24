package com.onezol.vertex.framework.component.notice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.constant.enumeration.ServiceStatus;
import com.onezol.vertex.framework.common.exception.RuntimeServiceException;
import com.onezol.vertex.framework.common.model.PagePack;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.component.notice.enumeration.NoticeStatus;
import com.onezol.vertex.framework.component.notice.mapper.NoticeMapper;
import com.onezol.vertex.framework.component.notice.model.Notice;
import com.onezol.vertex.framework.component.notice.model.NoticeEntity;
import com.onezol.vertex.framework.component.notice.model.NoticeSavePayload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class NoticeService {

    private final NoticeMapper noticeMapper;

    public NoticeService(NoticeMapper noticeMapper) {
        this.noticeMapper = noticeMapper;
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
            throw new RuntimeServiceException(ServiceStatus.BAD_REQUEST, "生效时间不能大于终止时间");
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

    public Notice getById(Long id) {
        NoticeEntity entity = noticeMapper.selectById(id);
        return BeanUtils.toBean(entity, Notice.class);
    }

    public PagePack<Notice> getPage(Page<NoticeEntity> page) {
        Page<NoticeEntity> quriedPage = noticeMapper.selectPage(page, null);
        List<Integer> types = quriedPage.getRecords().stream().map(identity -> identity.getType().getValue()).toList();
        PagePack<Notice> pack = PagePack.from(quriedPage, Notice.class);
        Collection<Notice> items = pack.getItems();
        int index = 0;
        for (Notice item : items) {
            item.setType(types.get(index++));
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
