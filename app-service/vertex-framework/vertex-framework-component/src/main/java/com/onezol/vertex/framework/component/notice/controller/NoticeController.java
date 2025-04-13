package com.onezol.vertex.framework.component.notice.controller;

import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.common.model.LabelValue;
import com.onezol.vertex.framework.common.mvc.controller.BaseController;
import com.onezol.vertex.framework.component.notice.model.Notice;
import com.onezol.vertex.framework.component.notice.model.NoticeEntity;
import com.onezol.vertex.framework.component.notice.model.NoticePayload;
import com.onezol.vertex.framework.component.notice.service.NoticeService;
import com.onezol.vertex.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "通知公告")
@RestController
@RequestMapping("/notice")
public class NoticeController extends BaseController<NoticeEntity> {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Operation(summary = "通知公告类型")
    @PostMapping("/dict")
    public GenericResponse<List<LabelValue<String, Integer>>> getDict() {
        return ResponseHelper.buildSuccessfulResponse(noticeService.getDict());
    }

    @Operation(summary = "新增通知公告")
    @PostMapping("/create")
    public GenericResponse<Void> create(NoticePayload payload) {
        noticeService.createOrUpdate(payload);
        return ResponseHelper.buildSuccessfulResponse();
    }

}
