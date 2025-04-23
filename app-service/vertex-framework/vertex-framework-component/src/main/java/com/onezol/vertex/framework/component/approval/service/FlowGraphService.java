package com.onezol.vertex.framework.component.approval.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.model.PageModel;
import com.onezol.vertex.framework.component.approval.mapper.ApprovalFlowTemplateMapper;
import com.onezol.vertex.framework.component.approval.model.FlowGraph;
import org.springframework.stereotype.Service;

@Service
public class FlowGraphService {

    private final ApprovalFlowTemplateMapper approvalFlowTemplateMapper;

    public FlowGraphService(ApprovalFlowTemplateMapper approvalFlowTemplateMapper) {
        this.approvalFlowTemplateMapper = approvalFlowTemplateMapper;
    }

    /**
     * 创建流程图
     *
     * @param flowGraph 流程图
     */
    public FlowGraph createFlowGraph(FlowGraph flowGraph) {
        this.approvalFlowTemplateMapper.insert(flowGraph);
        return flowGraph;
    }

    public FlowGraph getFlowGraph(Long id) {
        return this.approvalFlowTemplateMapper.selectById(id);
    }

    public FlowGraph updateFlowGraph(FlowGraph flowGraph) {
        this.approvalFlowTemplateMapper.updateById(flowGraph);
        return flowGraph;
    }

    public void deleteFlowGraph(Long id) {
        this.approvalFlowTemplateMapper.deleteById(id);
    }

    public PageModel<FlowGraph> pageFlowGraph(Page<FlowGraph> page) {
        return PageModel.from(this.approvalFlowTemplateMapper.selectPage(page, null));
    }
}
