package com.onezol.vertex.framework.component.approval.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.common.model.PageModel;
import com.onezol.vertex.framework.common.mvc.controller.BaseController;
import com.onezol.vertex.framework.component.approval.model.FlowGraph;
import com.onezol.vertex.framework.component.approval.service.FlowGraphService;
import com.onezol.vertex.framework.support.support.ResponseHelper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flow-graph")
public class FlowGraphController extends BaseController<FlowGraph> {

    private final FlowGraphService flowGraphService;

    public FlowGraphController(FlowGraphService flowGraphService) {
        this.flowGraphService = flowGraphService;
    }

    @GetMapping("/{id}")
    public GenericResponse<FlowGraph> getFlowGraph(@PathVariable("id") Long id) {
        return ResponseHelper.buildSuccessfulResponse(flowGraphService.getFlowGraph(id));
    }

    @PostMapping
    public GenericResponse<FlowGraph> createFlowGraph(@RequestBody FlowGraph flowGraph) {
        flowGraph = flowGraphService.createFlowGraph(flowGraph);
        return ResponseHelper.buildSuccessfulResponse(flowGraph);
    }

    @PutMapping
    public GenericResponse<FlowGraph> updateFlowGraph(@RequestBody FlowGraph flowGraph) {
        flowGraph = flowGraphService.updateFlowGraph(flowGraph);
        return ResponseHelper.buildSuccessfulResponse(flowGraph);
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> deleteFlowGraph(@PathVariable("id") Long id) {
        flowGraphService.deleteFlowGraph(id);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @GetMapping("/page")
    public GenericResponse<PageModel<FlowGraph>> pageFlowGraph(@RequestParam("pageNumber") Long pageNumber,
                                                    @RequestParam("pageSize") Long pageSize
    ) {
        Page<FlowGraph> page = this.getPage(pageNumber, pageSize);
        return ResponseHelper.buildSuccessfulResponse(flowGraphService.pageFlowGraph(page));
    }

}
