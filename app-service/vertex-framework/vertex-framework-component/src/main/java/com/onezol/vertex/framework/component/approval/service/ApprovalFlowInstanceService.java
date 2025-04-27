package com.onezol.vertex.framework.component.approval.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.component.approval.constant.ApprovalFlowStatus;
import com.onezol.vertex.framework.component.approval.mapper.ApprovalFlowInstanceMapper;
import com.onezol.vertex.framework.component.approval.model.entity.ApprovalFlowInstanceEntity;
import com.onezol.vertex.framework.component.approval.model.entity.ApprovalNodeRecordEntity;
import com.onezol.vertex.framework.security.api.service.UserInfoService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApprovalFlowInstanceService {

    private final ApprovalFlowInstanceMapper approvalFlowInstanceMapper;
    private final UserInfoService userInfoService;

    public ApprovalFlowInstanceService(ApprovalFlowInstanceMapper approvalFlowInstanceMapper, UserInfoService userInfoService) {
        this.approvalFlowInstanceMapper = approvalFlowInstanceMapper;
        this.userInfoService = userInfoService;
    }

    /**
     * 发起审批流程
     *
     * @param businessType 业务类型
     * @param businessId   业务ID
     * @param starterId    发起人ID
     */
    public ApprovalFlowInstanceEntity startApprovalFlow(String businessType, String businessId, Long starterId) {
//        userInfoService.get
        // 创建审批流程实例
        ApprovalFlowInstanceEntity instance = new ApprovalFlowInstanceEntity();
        instance.setBusinessTypeCode(businessType);
        instance.setBusinessId(businessId);
        instance.setInitiatorId(starterId);
        instance.setStatus(ApprovalFlowStatus.PENDING);
        approvalFlowInstanceMapper.insert(instance);

        // 创建审批节点记录
        ApprovalNodeRecordEntity record = new ApprovalNodeRecordEntity();
        record.setInstanceId(instance.getId());
        record.setApproverId(starterId);
        return instance;
    }

    /**
     * 审批操作
     *
     * @param instanceId 流程实例ID
     * @param approverId 审批人ID
     * @param approved   是否通过
     * @param comment    审批意见
     */
    public void approve(Long instanceId, Long approverId, boolean approved, String comment) {

    }

    /**
     * 获取审批历史
     * @param instanceId 流程实例ID
     */
    public List<ApprovalNodeRecordEntity> getApprovalHistory(Long instanceId) {
        return null;
    }

    /**
     * 取消审批流程
     * @param instanceId 流程实例ID
     * @param operatorId 操作人ID
     */
    public void cancelApprovalFlow(Long instanceId, Long operatorId) {

    }

    /**
     * 查询当前用户的待办审批
     * @param userId 用户ID
     * @param pageable
     */
    public Page<ApprovalFlowInstanceEntity> getPendingApprovals(Long userId, Pageable pageable) {
        return null;
    }
}