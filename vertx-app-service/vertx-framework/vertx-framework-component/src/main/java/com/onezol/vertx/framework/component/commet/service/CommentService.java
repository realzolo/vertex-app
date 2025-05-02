package com.onezol.vertx.framework.component.commet.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.constant.StringConstants;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertx.framework.common.util.BeanUtils;
import com.onezol.vertx.framework.common.util.MapUtils;
import com.onezol.vertx.framework.common.util.NetworkUtils;
import com.onezol.vertx.framework.common.util.ServletUtils;
import com.onezol.vertx.framework.component.commet.mapper.CommentMapper;
import com.onezol.vertx.framework.component.commet.model.dto.Comment;
import com.onezol.vertx.framework.component.commet.model.entity.CommentEntity;
import com.onezol.vertx.framework.component.commet.model.payload.CommentPayload;
import com.onezol.vertx.framework.security.api.model.dto.User;
import com.onezol.vertx.framework.security.api.service.UserInfoService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentService extends BaseServiceImpl<CommentMapper, CommentEntity> {

    private final UserInfoService userInfoService;

    public CommentService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * 创建评论
     */
    public Comment create(CommentPayload payload) {
        CommentEntity parentComment = this.getById(payload.getParentId());

        String clientIP = ServletUtils.getClientIP();
        // TODO: 根据IP获取地理位置信息
        CommentEntity entity = BeanUtils.toBean(payload, CommentEntity.class);
        entity.setAddress(NetworkUtils.getAddressByIP(clientIP));
        entity.setBusinessType("COMMON");
        if (Objects.nonNull(parentComment)) {
            entity.setPath(parentComment.getPath() + StringConstants.SLASH + parentComment.getId());
        } else {
            entity.setPath(StringConstants.EMPTY);
        }

        this.save(entity);

        return BeanUtils.toBean(entity, Comment.class);
    }


    /**
     * 删除评论
     *
     * @param id 评论ID
     */
    public void delete(Long id) {
        CommentEntity entity = this.getById(id);
        // 删除评论、评论下的回复
        this.remove(
                Wrappers.lambdaQuery(CommentEntity.class)
                        .like(Objects.nonNull(entity.getPath()), CommentEntity::getPath, entity.getPath())
                        .or()
                        .eq(CommentEntity::getId, id)
        );
    }

    /**
     * 获取评论列表
     *
     * @param queryPage 分页参数
     * @param objectId  评论对象
     */
    public PagePack<Comment> listPage(Page<CommentEntity> queryPage, Long objectId) {
        Page<CommentEntity> resultPage = this.page(
                queryPage,
                Wrappers.<CommentEntity>lambdaQuery()
                        .eq(CommentEntity::getPath, StringConstants.EMPTY)
                        .eq(CommentEntity::getObjectId, objectId)
                        .orderByDesc(CommentEntity::getId)
        );

        PagePack<Comment> pagePack = PagePack.from(resultPage, Comment.class);
        List<Comment> comments = pagePack.getItems();

        // 获取评论下的回复
        List<Long> ids = comments.stream().map(Comment::getId).toList();
        List<String> paths = ids.stream().map(id -> StringConstants.SLASH + id).toList();
        QueryWrapper<CommentEntity> replyQueryWrapper = new QueryWrapper<>();
        paths.forEach(path -> {
            replyQueryWrapper.or().like("path", path);
        });
        List<CommentEntity> resultReplies = this.list(replyQueryWrapper);
        List<Comment> replies = BeanUtils.toList(resultReplies, Comment.class);

        // 获取用户列表
        List<Long> commentCreatorIds = comments.stream().map(Comment::getCreator).toList();
        List<Long> applyCreatorIds = replies.stream().map(Comment::getCreator).toList();
        Set<Long> userIds = new HashSet<Long>() {{
            addAll(commentCreatorIds);
            addAll(applyCreatorIds);
        }};
        List<User> users = userInfoService.getUsersInfo(userIds.stream().toList());
        Map<Long, User> userMap = MapUtils.list2Map(users, User::getId);

        // 组装数据：将回复放到评论的replies中。 存在多级回复，需要递归。
        assembleReplies(comments, replies, userMap);

        return pagePack;
    }

    /**
     * 递归组装评论回复数据
     *
     * @param comments 评论列表
     * @param replies  回复列表
     */
    private void assembleReplies(List<Comment> comments, List<Comment> replies, Map<Long, User> userMap) {
        for (Comment comment : comments) {
            comment.setAuthor(userMap.get(comment.getCreator()));
            List<Comment> commentReplies = replies.stream()
                    .filter(reply -> comment.getId().equals(reply.getParentId()))
                    .toList();
            if (!commentReplies.isEmpty()) {
                commentReplies = commentReplies.stream().sorted(Comparator.comparing(Comment::getId).reversed()).toList();
                comment.setReplies(commentReplies);
                commentReplies.forEach(reply -> {
                    reply.setAuthor(comment.getAuthor());
                });
                assembleReplies(comment.getReplies(), replies, userMap);
            }
        }
    }
}
