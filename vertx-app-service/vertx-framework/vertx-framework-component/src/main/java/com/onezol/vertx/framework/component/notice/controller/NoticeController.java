package com.onezol.vertx.framework.component.notice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.model.GenericResponse;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.common.mvc.controller.BaseController;
import com.onezol.vertx.framework.common.util.Asserts;
import com.onezol.vertx.framework.component.notice.model.Notice;
import com.onezol.vertx.framework.component.notice.model.NoticeEntity;
import com.onezol.vertx.framework.component.notice.model.NoticeQueryPayload;
import com.onezol.vertx.framework.component.notice.model.NoticeSavePayload;
import com.onezol.vertx.framework.component.notice.service.NoticeService;
import com.onezol.vertx.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "通知公告")
@RestController
@RequestMapping("/notice")
public class NoticeController extends BaseController<NoticeEntity> {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Operation(summary = "新增通知公告")
    @PostMapping
    @PreAuthorize("@Security.hasPermission('system:notice:create')")
    public GenericResponse<Void> create(@RequestBody NoticeSavePayload payload) {
        noticeService.createOrUpdate(payload);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @Operation(summary = "更新通知公告")
    @PutMapping
    @PreAuthorize("@Security.hasPermission('system:notice:update')")
    public GenericResponse<Void> update(@RequestBody NoticeSavePayload payload) {
        Asserts.notNull(payload.getId(), "ID不能为空");
        noticeService.createOrUpdate(payload);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @Operation(summary = "删除通知公告")
    @DeleteMapping("/{id}")
    @PreAuthorize("@Security.hasPermission('system:notice:delete')")
    public GenericResponse<Void> delete(@PathVariable("id") Long id) {
        noticeService.deleteById(id);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @Operation(summary = "获取通知公告")
    @GetMapping("/{id}")
    public GenericResponse<Notice> get(@PathVariable("id") Long id) {
        return ResponseHelper.buildSuccessfulResponse(noticeService.getById(id));
    }

    @Operation(summary = "获取通知公告列表")
    @GetMapping("/page")
    @PreAuthorize("@Security.hasPermission('system:notice:list')")
    public GenericResponse<PagePack<Notice>> page(
            @RequestParam("pageNumber") Long pageNumber,
            @RequestParam("pageSize") Long pageSize,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "type", required = false) Integer type
    ) {
        NoticeQueryPayload queryPayload = new NoticeQueryPayload();
        queryPayload.setTitle(title);
        queryPayload.setType(type);

        Page<NoticeEntity> page = this.getPageObject(pageNumber, pageSize);

        PagePack<Notice> pack = noticeService.getPage(page, queryPayload);
        return ResponseHelper.buildSuccessfulResponse(pack);
    }

}
