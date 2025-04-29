package com.onezol.vertx.framework.component.approval.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.model.DictionaryEntry;
import com.onezol.vertx.framework.common.model.GenericResponse;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.component.approval.model.dto.ApprovalFlowBindingRelation;
import com.onezol.vertx.framework.component.approval.model.dto.ApprovalFlowNode;
import com.onezol.vertx.framework.component.approval.model.dto.ApprovalFlowTemplate;
import com.onezol.vertx.framework.component.approval.model.entity.ApprovalFlowBindingRelationEntity;
import com.onezol.vertx.framework.component.approval.model.entity.ApprovalFlowTemplateEntity;
import com.onezol.vertx.framework.component.approval.model.payload.ApprovalFlowBindingRelationPayload;
import com.onezol.vertx.framework.component.approval.model.payload.ApprovalFlowNodeCandidatePayload;
import com.onezol.vertx.framework.component.approval.model.payload.ApprovalFlowTemplateSavePayload;
import com.onezol.vertx.framework.component.approval.service.ApprovalFlowNodeService;
import com.onezol.vertx.framework.component.approval.service.ApprovalFlowService;
import com.onezol.vertx.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/approval-flow")
public class ApprovalFlowController {

    private final ApprovalFlowService approvalFlowService;
    private final ApprovalFlowNodeService approvalFlowNodeService;

    public ApprovalFlowController(ApprovalFlowService approvalFlowService, ApprovalFlowNodeService approvalFlowNodeService) {
        this.approvalFlowService = approvalFlowService;
        this.approvalFlowNodeService = approvalFlowNodeService;
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

    @Operation(summary = "设置候选人", description = "设置流程节点审批候选人")
    @PostMapping("/candidates")
    public GenericResponse<Void> setCandidates(@RequestBody ApprovalFlowNodeCandidatePayload payload) {
        approvalFlowService.setCandidates(payload);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @Operation(summary = "获取节点信息", description = "根据节点ID获取节点详情")
    @GetMapping("/node")
    public GenericResponse<ApprovalFlowNode> setCandidates(@RequestParam("nodeId") String nodeId) {
        ApprovalFlowNode node = approvalFlowNodeService.getNodeDetails(nodeId);
        return ResponseHelper.buildSuccessfulResponse(node);
    }

}
