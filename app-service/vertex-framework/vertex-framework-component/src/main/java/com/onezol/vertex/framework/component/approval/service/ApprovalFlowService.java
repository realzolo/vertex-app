package com.onezol.vertex.framework.component.approval.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.exception.InvalidParameterException;
import com.onezol.vertex.framework.common.model.DictionaryEntry;
import com.onezol.vertex.framework.common.model.PagePack;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.common.util.EnumUtils;
import com.onezol.vertex.framework.common.util.MapUtils;
import com.onezol.vertex.framework.component.approval.constant.BusinessType;
import com.onezol.vertex.framework.component.approval.mapper.ApprovalFlowBindingRelationMapper;
import com.onezol.vertex.framework.component.approval.mapper.ApprovalFlowTemplateMapper;
import com.onezol.vertex.framework.component.approval.model.dto.ApprovalFlowBindingRelation;
import com.onezol.vertex.framework.component.approval.model.dto.ApprovalFlowTemplate;
import com.onezol.vertex.framework.component.approval.model.entity.ApprovalFlowBindingRelationEntity;
import com.onezol.vertex.framework.component.approval.model.entity.ApprovalFlowTemplateEntity;
import com.onezol.vertex.framework.component.approval.model.payload.ApprovalFlowBindingRelationPayload;
import com.onezol.vertex.framework.component.approval.model.payload.ApprovalFlowTemplateSavePayload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApprovalFlowService {

    private final ApprovalFlowTemplateMapper approvalFlowTemplateMapper;
    private final ApprovalFlowBindingRelationMapper approvalFlowBindingRelationMapper;

    public ApprovalFlowService(ApprovalFlowTemplateMapper approvalFlowTemplateMapper, ApprovalFlowBindingRelationMapper approvalFlowBindingRelationMapper) {
        this.approvalFlowTemplateMapper = approvalFlowTemplateMapper;
        this.approvalFlowBindingRelationMapper = approvalFlowBindingRelationMapper;
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
    public ApprovalFlowTemplate updateFlowTemplate(ApprovalFlowTemplateSavePayload payload) {
        ApprovalFlowTemplateEntity flowTemplate = BeanUtils.toBean(payload, ApprovalFlowTemplateEntity.class);
        approvalFlowTemplateMapper.updateById(flowTemplate);
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
}