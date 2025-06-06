package com.onezol.vertx.framework.component.comment.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.constant.StringConstants;
import com.onezol.vertx.framework.common.exception.InvalidParameterException;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.common.skeleton.service.BaseServiceImpl;
import com.onezol.vertx.framework.common.util.BeanUtils;
import com.onezol.vertx.framework.common.util.MapUtils;
import com.onezol.vertx.framework.common.util.NetworkUtils;
import com.onezol.vertx.framework.common.util.ServletUtils;
import com.onezol.vertx.framework.component.comment.mapper.CommentMapper;
import com.onezol.vertx.framework.component.comment.model.dto.Comment;
import com.onezol.vertx.framework.component.comment.model.entity.CommentEntitySoft;
import com.onezol.vertx.framework.component.comment.model.payload.CommentPayload;
import com.onezol.vertx.framework.component.upvote.constant.enumeration.UpvoteObjectType;
import com.onezol.vertx.framework.component.upvote.service.UpvoteRecordService;
import com.onezol.vertx.framework.security.api.context.UserIdentityContext;
import com.onezol.vertx.framework.security.api.model.UserIdentity;
import com.onezol.vertx.framework.security.api.model.dto.User;
import com.onezol.vertx.framework.security.api.service.UserInfoService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentService extends BaseServiceImpl<CommentMapper, CommentEntitySoft> {

    private final UserInfoService userInfoService;
    private final UpvoteRecordService upvoteRecordService;

    public CommentService(UserInfoService userInfoService, UpvoteRecordService upvoteRecordService) {
        this.userInfoService = userInfoService;
        this.upvoteRecordService = upvoteRecordService;
    }

    /**
     * 创建评论
     */
    public Comment create(CommentPayload payload) {
        CommentEntitySoft parentComment = this.getById(payload.getParentId());

        String clientIP = ServletUtils.getClientIP();
        CommentEntitySoft entity = BeanUtils.toBean(payload, CommentEntitySoft.class);
        entity.setAddress(NetworkUtils.getAddressByIP(clientIP));
        entity.setBusinessType("COMMON");

        if (Objects.nonNull(payload.getParentId()) && Objects.isNull(parentComment)) {
            throw new InvalidParameterException("此评论已被删除，无法创建回复");
        }

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
        CommentEntitySoft entity = this.getById(id);
        // 删除评论、评论下的回复
        String subPath = entity.getPath() + StringConstants.SLASH + entity.getId();
        this.remove(
                Wrappers.lambdaQuery(CommentEntitySoft.class)
                        .like(CommentEntitySoft::getPath, subPath)
                        .or()
                        .eq(CommentEntitySoft::getId, id)
        );
    }

    /**
     * 获取评论列表
     *
     * @param page 分页参数
     * @param objectId  评论对象
     * @param sortType  排序类型
     */
    public PagePack<Comment> listPage(Page<CommentEntitySoft> page, Long objectId, String sortType) {
        Page<CommentEntitySoft> resultPage = this.baseMapper.queryTopLevelCommentPage(page, UpvoteObjectType.COMMENT.getValue(), objectId, sortType);

        PagePack<Comment> pagePack = PagePack.from(resultPage, Comment.class);
        List<Comment> comments = pagePack.getItems();

        // 获取评论下的回复
        List<Long> ids = comments.stream().map(Comment::getId).toList();
        List<String> paths = ids.stream().map(id -> StringConstants.SLASH + id).toList();
        QueryWrapper<CommentEntitySoft> replyQueryWrapper = new QueryWrapper<>();
        paths.forEach(path -> replyQueryWrapper.or().like("path", path));
        List<CommentEntitySoft> resultReplies = this.list(replyQueryWrapper);
        List<Comment> replies = BeanUtils.copyToList(resultReplies, Comment.class);

        // 获取所有用户ID列表与评论ID列表
        Set<Long> fullUserIds = new HashSet<>();
        Set<Long> fullCommentIds = new HashSet<>();
        for (Comment comment : comments) {
            fullUserIds.add(comment.getCreator());
            fullCommentIds.add(comment.getId());
        }
        for (Comment reply : replies) {
            fullUserIds.add(reply.getCreator());
            fullCommentIds.add(reply.getId());
        }
        // 获取所有用户信息
        List<User> users = userInfoService.getUsersInfo(fullUserIds.stream().toList());
        Map<Long, User> userMap = MapUtils.list2Map(users, User::getId);

        // 获取所有评论点赞数
        Map<Long, Long> voteCounts = upvoteRecordService.getVoteCounts(UpvoteObjectType.COMMENT, fullCommentIds.stream().toList());
        Map<Long, Boolean> votedStatus = upvoteRecordService.hasVotedBatch(UpvoteObjectType.COMMENT, fullCommentIds.stream().toList(), UserIdentityContext.get().getUserId());

        // 组装数据
        assembleReplies(comments, replies, userMap, voteCounts, votedStatus);

        return pagePack;
    }

    /**
     * 递归组装评论回复数据
     *
     * @param comments    评论列表
     * @param replies     回复列表
     * @param userMap     用户信息
     * @param voteCounts  点赞数
     * @param votedStatus 点赞状态
     */
    private void assembleReplies(List<Comment> comments, List<Comment> replies, Map<Long, User> userMap, Map<Long, Long> voteCounts, Map<Long, Boolean> votedStatus) {
        for (Comment comment : comments) {
            comment.setAuthor(userMap.get(comment.getCreator()));
            List<Comment> commentReplies = replies.stream()
                    .filter(reply -> comment.getId().equals(reply.getParentId()))
                    .sorted(Comparator.comparing(Comment::getId).reversed())
                    .toList();
            comment.setReplies(commentReplies);
            comment.setUpvotes(voteCounts.getOrDefault(comment.getId(), 0L));
            comment.setUpvoted(votedStatus.getOrDefault(comment.getId(), false));
            if (!commentReplies.isEmpty()) {
                assembleReplies(comment.getReplies(), replies, userMap, voteCounts, votedStatus);
            }
        }
    }

    /**
     * 点赞
     *
     * @param id 评论ID
     */
    public void toggleVote(Long id) {
        if (!this.exists(id)) {
            throw new InvalidParameterException("点赞失败，评论不存在");
        }

        UserIdentity userIdentity = UserIdentityContext.get();

        upvoteRecordService.toggleVote(UpvoteObjectType.COMMENT, id, userIdentity.getUserId());
    }

    /**
     * 快速判断评论是否存在
     *
     * @param id 评论ID
     */
    public boolean exists(Long id) {
        return this.count(Wrappers.lambdaQuery(CommentEntitySoft.class).eq(CommentEntitySoft::getId, id)) > 0;
    }

}
