package com.onezol.vertex.framework.component.approval.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.model.PageModel;
import com.onezol.vertex.framework.component.approval.mapper.FlowGraphMapper;
import com.onezol.vertex.framework.component.approval.model.FlowGraph;
import org.springframework.stereotype.Service;

@Service
public class FlowGraphService {

    private final FlowGraphMapper flowGraphMapper;

    public FlowGraphService(FlowGraphMapper flowGraphMapper) {
        this.flowGraphMapper = flowGraphMapper;
    }

    /**
     * 创建流程图
     *
     * @param flowGraph 流程图
     */
    public FlowGraph createFlowGraph(FlowGraph flowGraph) {
        this.flowGraphMapper.insert(flowGraph);
        return flowGraph;
    }

    public FlowGraph getFlowGraph(Long id) {
        return this.flowGraphMapper.selectById(id);
    }

    public FlowGraph updateFlowGraph(FlowGraph flowGraph) {
        this.flowGraphMapper.updateById(flowGraph);
        return flowGraph;
    }

    public void deleteFlowGraph(Long id) {
        this.flowGraphMapper.deleteById(id);
    }

    public PageModel<FlowGraph> pageFlowGraph(Page<FlowGraph> page) {
        return PageModel.from(this.flowGraphMapper.selectPage(page, null));
    }
}
