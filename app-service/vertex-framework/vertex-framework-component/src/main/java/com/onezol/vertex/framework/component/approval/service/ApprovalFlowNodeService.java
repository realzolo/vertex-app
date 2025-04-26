package com.onezol.vertex.framework.component.approval.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.common.util.JsonUtils;
import com.onezol.vertex.framework.common.util.StringUtils;
import com.onezol.vertex.framework.component.approval.mapper.ApprovalFlowNodeMapper;
import com.onezol.vertex.framework.component.approval.model.dto.ApprovalFlowNode;
import com.onezol.vertex.framework.component.approval.model.entity.ApprovalFlowNodeEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ApprovalFlowNodeService {

    private final ApprovalFlowNodeMapper approvalFlowNodeMapper;

    public ApprovalFlowNodeService(ApprovalFlowNodeMapper approvalFlowNodeMapper) {
        this.approvalFlowNodeMapper = approvalFlowNodeMapper;
    }


    /**
     * 更新审批流程节点
     * @param templateId 流程模板ID
     * @param flowContent 流程图内容
     */
    @Transactional
    public void updateFlowNodes(Long templateId, String flowContent) {
        // 删除原审批流程节点
        approvalFlowNodeMapper.delete(
                Wrappers.<ApprovalFlowNodeEntity>lambdaQuery()
                        .eq(ApprovalFlowNodeEntity::getTemplateId, templateId)
        );

        if (StringUtils.isBlank(flowContent)) {
            return;
        }

        // 创建审批流程节点
        JSONObject flowContentJson = JsonUtils.parseJsonObject(flowContent);
        JSONArray nodes = flowContentJson.getJSONArray("nodes");
        List<ApprovalFlowNodeEntity> flowNodes = new ArrayList<>(nodes.size());
        for (Object nodeObject : nodes) {
            JSONObject node = (JSONObject) nodeObject;
            ApprovalFlowNodeEntity nodeEntity = new ApprovalFlowNodeEntity();
            nodeEntity.setTemplateId(templateId);
            nodeEntity.setNodeId(node.getString("id"));
            nodeEntity.setLabel(node.getString("label"));
            JSONObject nodeData = node.getJSONObject("data");
            if (Objects.nonNull(nodeData)) {
                nodeEntity.setType(nodeData.getString("type"));
                nodeEntity.setPrevNodeId(nodeData.getString("source"));
                nodeEntity.setNextNodeId(nodeData.getString("target"));
            }
            flowNodes.add(nodeEntity);
        }

        // 保存流程节点
        for (ApprovalFlowNodeEntity flowNode : flowNodes) {
            approvalFlowNodeMapper.insert(flowNode);
        }
    }


    /**
     * 获取流程节点详情
     * @param nodeId 节点ID
     */
    public ApprovalFlowNode getNodeDetails(String nodeId) {
        ApprovalFlowNodeEntity nodeEntity = approvalFlowNodeMapper.selectOne(
                Wrappers.<ApprovalFlowNodeEntity>lambdaQuery()
                        .eq(ApprovalFlowNodeEntity::getNodeId, nodeId)
        );
        return BeanUtils.toBean(nodeEntity, ApprovalFlowNode.class);
    }

}
