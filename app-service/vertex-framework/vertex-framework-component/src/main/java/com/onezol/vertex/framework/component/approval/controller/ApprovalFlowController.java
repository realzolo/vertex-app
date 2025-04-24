package com.onezol.vertex.framework.component.approval.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.model.DictionaryEntry;
import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.common.model.PagePack;
import com.onezol.vertex.framework.component.approval.model.dto.ApprovalFlowBindingRelation;
import com.onezol.vertex.framework.component.approval.model.dto.ApprovalFlowTemplate;
import com.onezol.vertex.framework.component.approval.model.entity.ApprovalFlowBindingRelationEntity;
import com.onezol.vertex.framework.component.approval.model.entity.ApprovalFlowTemplateEntity;
import com.onezol.vertex.framework.component.approval.model.payload.ApprovalFlowBindingRelationPayload;
import com.onezol.vertex.framework.component.approval.model.payload.ApprovalFlowTemplateSavePayload;
import com.onezol.vertex.framework.component.approval.service.ApprovalFlowService;
import com.onezol.vertex.framework.support.support.ResponseHelper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/approval-flow")
public class ApprovalFlowController {

    private final ApprovalFlowService approvalFlowService;

    public ApprovalFlowController(ApprovalFlowService approvalFlowService) {
        this.approvalFlowService = approvalFlowService;
    }

    @GetMapping("/{id}")
    public GenericResponse<ApprovalFlowTemplate> getFlowTemplate(@PathVariable("id") Long id) {
        return ResponseHelper.buildSuccessfulResponse(approvalFlowService.getFlowTemplate(id));
    }

    @GetMapping("/template/dict")
    public GenericResponse<List<DictionaryEntry>> getFlowTemplateDict() {
        return ResponseHelper.buildSuccessfulResponse(approvalFlowService.getFlowTemplateDict());
    }

    @PostMapping
    public GenericResponse<ApprovalFlowTemplate> createFlowTemplate(@RequestBody ApprovalFlowTemplateSavePayload payload) {
        ApprovalFlowTemplate approvalFlowTemplate = approvalFlowService.createFlowTemplate(payload);
        return ResponseHelper.buildSuccessfulResponse(approvalFlowTemplate);
    }

    @PutMapping
    public GenericResponse<ApprovalFlowTemplate> updateFlowTemplate(@RequestBody ApprovalFlowTemplateSavePayload payload) {
        ApprovalFlowTemplate approvalFlowTemplate = approvalFlowService.updateFlowTemplate(payload);
        return ResponseHelper.buildSuccessfulResponse(approvalFlowTemplate);
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> deleteFlowTemplate(@PathVariable("id") Long id) {
        approvalFlowService.deleteFlowTemplate(id);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @GetMapping("/page")
    public GenericResponse<PagePack<ApprovalFlowTemplate>> getFlowTemplatePage(
            @RequestParam("pageNumber") Long pageNumber,
            @RequestParam("pageSize") Long pageSize
    ) {
        Page<ApprovalFlowTemplateEntity> page = new Page<>(pageNumber, pageSize);
        return ResponseHelper.buildSuccessfulResponse(approvalFlowService.getFlowTemplatePage(page));
    }

    @PostMapping("/bind")
    public GenericResponse<Void> bindFlowToBusinessType(@RequestBody ApprovalFlowBindingRelationPayload payload) {
        approvalFlowService.bindFlowToBusinessType(payload);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @DeleteMapping("/unbind")
    public GenericResponse<Void> unbindFlowFromBusinessType(@RequestBody ApprovalFlowBindingRelationPayload payload) {
        approvalFlowService.unbindFlowFromBusinessType(payload);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @GetMapping("/bind/page")
    public GenericResponse<PagePack<ApprovalFlowBindingRelation>> getFlowBindingRelation(
            @RequestParam("pageNumber") Long pageNumber,
            @RequestParam("pageSize") Long pageSize
    ) {
        Page<ApprovalFlowBindingRelationEntity> page = new Page<>(pageNumber, pageSize);
        return ResponseHelper.buildSuccessfulResponse(approvalFlowService.getFlowBindingRelation(page));
    }

}
