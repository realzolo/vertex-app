package com.onezol.vertx.framework.component.approval.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.exception.InvalidParameterException;
import com.onezol.vertx.framework.common.model.DictionaryEntry;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.common.util.BeanUtils;
import com.onezol.vertx.framework.common.util.EnumUtils;
import com.onezol.vertx.framework.common.util.MapUtils;
import com.onezol.vertx.framework.component.approval.constant.*;
import com.onezol.vertx.framework.component.approval.mapper.ApprovalFlowBindingRelationMapper;
import com.onezol.vertx.framework.component.approval.mapper.ApprovalFlowCandidateMapper;
import com.onezol.vertx.framework.component.approval.mapper.ApprovalFlowTemplateMapper;
import com.onezol.vertx.framework.component.approval.model.dto.ApprovalFlowBindingRelation;
import com.onezol.vertx.framework.component.approval.model.dto.ApprovalFlowTemplate;
import com.onezol.vertx.framework.component.approval.model.entity.ApprovalFlowBindingRelationEntity;
import com.onezol.vertx.framework.component.approval.model.entity.ApprovalFlowNodeCandidateEntity;
import com.onezol.vertx.framework.component.approval.model.entity.ApprovalFlowTemplateEntity;
import com.onezol.vertx.framework.component.approval.model.payload.ApprovalFlowBindingRelationPayload;
import com.onezol.vertx.framework.component.approval.model.payload.ApprovalFlowNodeCandidatePayload;
import com.onezol.vertx.framework.component.approval.model.payload.ApprovalFlowTemplateSavePayload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ApprovalFlowService {

    private final ApprovalFlowTemplateMapper approvalFlowTemplateMapper;
    private final ApprovalFlowBindingRelationMapper approvalFlowBindingRelationMapper;
    private final ApprovalFlowCandidateMapper approvalFlowCandidateMapper;
    private final ApprovalFlowNodeService approvalFlowNodeService;

    public ApprovalFlowService(ApprovalFlowTemplateMapper approvalFlowTemplateMapper, ApprovalFlowBindingRelationMapper approvalFlowBindingRelationMapper, ApprovalFlowCandidateMapper approvalFlowCandidateMapper, ApprovalFlowNodeService approvalFlowNodeService) {
        this.approvalFlowTemplateMapper = approvalFlowTemplateMapper;
        this.approvalFlowBindingRelationMapper = approvalFlowBindingRelationMapper;
        this.approvalFlowCandidateMapper = approvalFlowCandidateMapper;
        this.approvalFlowNodeService = approvalFlowNodeService;
    }

    /**
     * 创建流程模板
     */
    public ApprovalFlowTemplate createFlowTemplate(ApprovalFlowTemplateSavePayload payload) {
        ApprovalFlowTemplateEntity flowTemplate = BeanUtils.toBean(payload, ApprovalFlowTemplateEntity.class);
        approvalFlowTemplateMapper.insert(flowTemplate);
        return BeanUtils.toBean(flowTemplate, ApprovalFlowTemplate.class);
    }

    /**
     * 更新流程模板
     */
    @Transactional
    public ApprovalFlowTemplate updateFlowTemplate(ApprovalFlowTemplateSavePayload payload) {
        ApprovalFlowTemplateEntity flowTemplate = BeanUtils.toBean(payload, ApprovalFlowTemplateEntity.class);

        approvalFlowTemplateMapper.updateById(flowTemplate);

        approvalFlowNodeService.updateFlowNodes(flowTemplate.getId(), flowTemplate.getContent());

        return BeanUtils.toBean(flowTemplate, ApprovalFlowTemplate.class);
    }

    /**
     * 获取流程模板
     */
    public ApprovalFlowTemplate getFlowTemplate(Long id) {
        if (id == null) return null;
        ApprovalFlowTemplateEntity flowTemplate = approvalFlowTemplateMapper.selectById(id);
        return BeanUtils.toBean(flowTemplate, ApprovalFlowTemplate.class);
    }

    /**
     * 删除流程模板
     */
    public void deleteFlowTemplate(Long id) {
        approvalFlowTemplateMapper.deleteById(id);
    }

    /**
     * 分页获取流程模板
     */
    public PagePack<ApprovalFlowTemplate> getFlowTemplatePage(Page<ApprovalFlowTemplateEntity> page) {
        Page<ApprovalFlowTemplateEntity> flowTemplatePage = approvalFlowTemplateMapper.selectPage(page, null);
        return PagePack.from(flowTemplatePage, ApprovalFlowTemplate.class);
    }

    /**
     * 绑定业务到流程模板
     */
    public void bindFlowToBusinessType(ApprovalFlowBindingRelationPayload payload) {
        ApprovalFlowTemplateEntity flowTemplate = approvalFlowTemplateMapper.selectById(payload.getFlowTemplateId());
        if (flowTemplate == null) {
            throw new InvalidParameterException("流程模板不存在");
        }
        Long count = approvalFlowBindingRelationMapper.selectCount(
                Wrappers.<ApprovalFlowBindingRelationEntity>lambdaQuery()
                        .eq(ApprovalFlowBindingRelationEntity::getBusinessTypeCode, payload.getBusinessTypeCode())
                        .eq(ApprovalFlowBindingRelationEntity::getFlowTemplateId, payload.getFlowTemplateId())
        );
        if (count > 0) {
            throw new InvalidParameterException("该业务已绑定流程模板");
        }

        ApprovalFlowBindingRelationEntity relationEntity = new ApprovalFlowBindingRelationEntity();
        relationEntity.setBusinessTypeCode(payload.getBusinessTypeCode());
        relationEntity.setFlowTemplateId(payload.getFlowTemplateId());
        approvalFlowBindingRelationMapper.insert(relationEntity);
    }

    /**
     * 解绑业务流程
     */
    public void unbindFlowFromBusinessType(ApprovalFlowBindingRelationPayload payload) {
        approvalFlowBindingRelationMapper.delete(
                Wrappers.<ApprovalFlowBindingRelationEntity>lambdaQuery()
                        .eq(ApprovalFlowBindingRelationEntity::getBusinessTypeCode, payload.getBusinessTypeCode())
                        .eq(ApprovalFlowBindingRelationEntity::getFlowTemplateId, payload.getFlowTemplateId())
        );
    }

    /**
     * 分页获取业务流程模板绑定关系
     */
    public PagePack<ApprovalFlowBindingRelation> getFlowBindingRelation(Page<ApprovalFlowBindingRelationEntity> page) {
        List<ApprovalFlowTemplateEntity> approvalFlowTemplateEntities = approvalFlowTemplateMapper.selectList(
                Wrappers.<ApprovalFlowTemplateEntity>lambdaQuery()
                        .select(ApprovalFlowTemplateEntity::getId, ApprovalFlowTemplateEntity::getName)
        );
        Map<Long, ApprovalFlowTemplateEntity> flowTemplateMap = MapUtils.list2Map(approvalFlowTemplateEntities, ApprovalFlowTemplateEntity::getId);

        Page<ApprovalFlowBindingRelationEntity> relationPage = approvalFlowBindingRelationMapper.selectPage(page, null);
        PagePack<ApprovalFlowBindingRelation> pageModel = PagePack.from(relationPage, ApprovalFlowBindingRelation.class);
        pageModel.getItems().forEach(item -> {
            ApprovalFlowTemplateEntity flowTemplate = flowTemplateMap.get(item.getFlowTemplateId());
            if (flowTemplate != null) {
                item.setFlowTemplateName(flowTemplate.getName());
            }
            BusinessType businessType = EnumUtils.getEnumByValue(BusinessType.class, item.getBusinessTypeCode());
            if (businessType != null) {
                item.setBusinessTypeName(businessType.getName());
            }
        });
        return pageModel;
    }

    public List<DictionaryEntry> getFlowTemplateDict() {
        List<ApprovalFlowTemplateEntity> entities = approvalFlowTemplateMapper.selectList(null);
        return entities.stream().map(entity -> DictionaryEntry.of(entity.getName(), entity.getId())).collect(Collectors.toList());
    }

    /**
     * 判断业务是否绑定了流程
     */
    public boolean isBusinessTypeBound(String businessTypeCode) {
        Long count = approvalFlowBindingRelationMapper.selectCount(
                Wrappers.<ApprovalFlowBindingRelationEntity>lambdaQuery()
                        .eq(ApprovalFlowBindingRelationEntity::getBusinessTypeCode, businessTypeCode)
        );
        return count > 0;
    }

    /**
     * 设置流程节点审批候选人
     */
    public void setCandidates(ApprovalFlowNodeCandidatePayload payload) {
        CandidateStrategy candidateStrategy = EnumUtils.getEnumByValue(CandidateStrategy.class, payload.getCandidateStrategy());
        if (candidateStrategy == null) {
            throw new InvalidParameterException("无效的候选人选择策略");
        }
        switch (candidateStrategy) {
            case CandidateStrategy.DESIGNATE_USER -> setCandidateByDesignateUser(payload);
            case CandidateStrategy.DESIGNATE_ROLE -> setCandidateByDesignateRole(payload);
            case CandidateStrategy.INITIATOR -> setCandidateByInitiator(payload);
            case CandidateStrategy.SELECTION -> setCandidateBySelection(payload);
        }
    }

    /**
     * 设置审批候选人：指定用户
     */
    private void setCandidateByDesignateUser(ApprovalFlowNodeCandidatePayload payload) {
        ApprovalFlowNodeCandidateEntity candidateEntity = new ApprovalFlowNodeCandidateEntity();
        candidateEntity.setNodeId(payload.getNodeId());
        candidateEntity.setCandidateStrategy(CandidateStrategy.DESIGNATE_USER);
        candidateEntity.setUserIds(payload.getUserIds());
        ApprovalType approvalType = EnumUtils.getEnumByValue(ApprovalType.class, payload.getApprovalType());
        if (Objects.isNull(approvalType)) {
            throw new InvalidParameterException("无效的审批类型");
        }
        candidateEntity.setApprovalType(approvalType);
        UnmannedStrategy unmannedStrategy = EnumUtils.getEnumByValue(UnmannedStrategy.class, payload.getUnmannedStrategy());
        if (Objects.isNull(unmannedStrategy)) {
            throw new InvalidParameterException("无效的审批策略");
        }
        candidateEntity.setUnmannedStrategy(unmannedStrategy);
        if (Objects.nonNull(payload.getId())) {
            approvalFlowCandidateMapper.updateById(candidateEntity);
        } else {
            approvalFlowCandidateMapper.insert(candidateEntity);
        }
    }

    /**
     * 设置审批候选人：指定角色
     */
    private void setCandidateByDesignateRole(ApprovalFlowNodeCandidatePayload payload) {
        List<Long> roleIds = payload.getRoleIds();
        ApprovalFlowNodeCandidateEntity candidateEntity = new ApprovalFlowNodeCandidateEntity();
        candidateEntity.setNodeId(payload.getNodeId());
        candidateEntity.setCandidateStrategy(CandidateStrategy.DESIGNATE_ROLE);
        candidateEntity.setRoleIds(roleIds);
        ApprovalType approvalType = EnumUtils.getEnumByValue(ApprovalType.class, payload.getApprovalType());
        if (Objects.isNull(approvalType)) {
            throw new InvalidParameterException("无效的审批类型");
        }
        candidateEntity.setApprovalType(approvalType);
        UnmannedStrategy unmannedStrategy = EnumUtils.getEnumByValue(UnmannedStrategy.class, payload.getUnmannedStrategy());
        if (Objects.isNull(unmannedStrategy)) {
            throw new InvalidParameterException("无效的审批策略");
        }
        candidateEntity.setUnmannedStrategy(unmannedStrategy);
        if (Objects.nonNull(payload.getId())) {
            approvalFlowCandidateMapper.updateById(candidateEntity);
        } else {
            approvalFlowCandidateMapper.insert(candidateEntity);
        }
    }

    /**
     * 设置审批候选人：发起人自己
     */
    private void setCandidateByInitiator(ApprovalFlowNodeCandidatePayload payload) {
        List<Long> userIds = payload.getUserIds();
        ApprovalFlowNodeCandidateEntity candidateEntity = new ApprovalFlowNodeCandidateEntity();
        candidateEntity.setNodeId(payload.getNodeId());
        candidateEntity.setCandidateStrategy(CandidateStrategy.INITIATOR);
        candidateEntity.setUserIds(userIds);
        if (Objects.nonNull(payload.getId())) {
            approvalFlowCandidateMapper.updateById(candidateEntity);
        } else {
            approvalFlowCandidateMapper.insert(candidateEntity);
        }
    }

    /**
     * 设置审批候选人：发起人自选
     */
    private void setCandidateBySelection(ApprovalFlowNodeCandidatePayload payload) {
        ApprovalFlowNodeCandidateEntity candidateEntity = new ApprovalFlowNodeCandidateEntity();
        candidateEntity.setNodeId(payload.getNodeId());
        CandidateSelectionType candidateSelectionType = EnumUtils.getEnumByValue(CandidateSelectionType.class, payload.getCandidateSelectionType());
        if (Objects.isNull(candidateSelectionType)) {
            throw new InvalidParameterException("无效的候选人自选类型");
        }
        // TODO: 发起人自选
    }

}