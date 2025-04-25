package com.onezol.vertex.framework.component.notice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.common.model.PagePack;
import com.onezol.vertex.framework.common.mvc.controller.BaseController;
import com.onezol.vertex.framework.component.notice.model.Notice;
import com.onezol.vertex.framework.component.notice.model.NoticeEntity;
import com.onezol.vertex.framework.component.notice.model.NoticeSavePayload;
import com.onezol.vertex.framework.component.notice.service.NoticeService;
import com.onezol.vertex.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Tag(name = "通知公告")
@RestController
@RequestMapping("/notice")
public class NoticeController extends BaseController<NoticeEntity> {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Operation(summary = "新增通知公告")
    @PostMapping("/create")
    public GenericResponse<Void> create(@RequestBody NoticeSavePayload payload) {
        noticeService.createOrUpdate(payload);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @Operation(summary = "新增通知公告")
    @PutMapping("/{id}")
    public GenericResponse<Void> update(@PathVariable("id") Long id, @RequestBody NoticeSavePayload payload) {
        if (Objects.isNull(payload.getId())) {
            payload.setId(id);
        }
        noticeService.createOrUpdate(payload);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @Operation(summary = "删除通知公告")
    @DeleteMapping("/{id}")
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
    public GenericResponse<PagePack<Notice>> page(
            @RequestParam("pageNumber") Long pageNumber,
            @RequestParam("pageSize") Long pageSize
    ) {
        Page<NoticeEntity> page = this.getPageObject(pageNumber, pageSize);
        return ResponseHelper.buildSuccessfulResponse(noticeService.getPage(page));
    }

}
