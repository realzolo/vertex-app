package com.onezol.vertx.framework.component.comment.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.model.GenericResponse;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.common.mvc.controller.BaseController;
import com.onezol.vertx.framework.component.comment.model.dto.Comment;
import com.onezol.vertx.framework.component.comment.model.entity.CommentEntity;
import com.onezol.vertx.framework.component.comment.model.payload.CommentPayload;
import com.onezol.vertx.framework.component.comment.service.CommentService;
import com.onezol.vertx.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "评论")
@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController<CommentEntity> {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "创建评论")
    @PostMapping
    public GenericResponse<Comment> createComment(@RequestBody @Valid CommentPayload payload) {
        Comment comment = commentService.create(payload);
        return ResponseHelper.buildSuccessfulResponse(comment);
    }

    @Operation(summary = "删除评论")
    @DeleteMapping("/{id}")
    public GenericResponse<Void> deleteComment(@PathVariable("id") Long id) {
        commentService.delete(id);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @Operation(summary = "分页获取评论")
    @GetMapping("/page")
    public GenericResponse<PagePack<Comment>> pageComment(
            @RequestParam("pageNumber") Long pageNumber,
            @RequestParam("pageSize") Long pageSize,
            @RequestParam("objectId") Long objectId,
            @RequestParam(value = "sortType", required = false) String sortType
    ) {
        Page<CommentEntity> queryPage = this.getPageObject(pageNumber, pageSize);
        PagePack<Comment> resultPage = commentService.listPage(queryPage, objectId, sortType);
        return ResponseHelper.buildSuccessfulResponse(resultPage);
    }

    @Operation(summary = "评论点赞/取消点赞")
    @PostMapping("/{id}/upvote")
    public GenericResponse<Void> upvoteComment(@PathVariable("id") Long id) {
        commentService.toggleVote(id);
        return ResponseHelper.buildSuccessfulResponse();
    }

}
