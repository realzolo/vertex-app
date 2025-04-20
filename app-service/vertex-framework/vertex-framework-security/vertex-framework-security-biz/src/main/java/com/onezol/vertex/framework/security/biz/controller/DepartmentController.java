package com.onezol.vertex.framework.security.biz.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.framework.common.constant.enumeration.DisEnableStatus;
import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.common.model.TreeNode;
import com.onezol.vertex.framework.common.mvc.controller.BaseController;
import com.onezol.vertex.framework.common.util.Asserts;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.common.util.TreeUtils;
import com.onezol.vertex.framework.security.api.model.dto.Department;
import com.onezol.vertex.framework.security.api.model.entity.DepartmentEntity;
import com.onezol.vertex.framework.security.api.model.payload.DepartmentSavePayload;
import com.onezol.vertex.framework.security.api.service.DepartmentService;
import com.onezol.vertex.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/department")
public class DepartmentController extends BaseController<DepartmentEntity> {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Operation(summary = "新增部门")
    @PostMapping
    public GenericResponse<Department> createDepartment(@RequestBody DepartmentSavePayload payload) {
        Department department = departmentService.createOrUpdate(payload);
        return ResponseHelper.buildSuccessfulResponse(department);
    }

    @Operation(summary = "更新部门")
    @PutMapping("/{id}")
    public GenericResponse<Department> updateDepartment(@RequestBody DepartmentSavePayload payload) {
        Department department = departmentService.createOrUpdate(payload);
        return ResponseHelper.buildSuccessfulResponse(department);
    }

    @Operation(summary = "删除部门")
    @DeleteMapping("/{id}")
    public GenericResponse<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.removeById(id);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @Operation(summary = "获取部门详情")
    @GetMapping("/{id}")
    public GenericResponse<Department> getDepartmentDetail(@PathVariable("id") Long id) {
        DepartmentEntity department = departmentService.getById(id);
        Asserts.notNull(department, "部门不存在");
        Department bean = BeanUtils.toBean(department, Department.class);
        return ResponseHelper.buildSuccessfulResponse(bean);
    }

    @Operation(summary = "获取部门列表")
    @GetMapping("/list")
    public GenericResponse<List<Department>> getDepartmentList() {
        List<DepartmentEntity> list = departmentService.list();
        List<Department> departments = BeanUtils.toList(list, Department.class);
        return ResponseHelper.buildSuccessfulResponse(departments);
    }

    @Operation(summary = "获取部门树")
    @GetMapping("/tree")
    public GenericResponse<List<TreeNode>> getDepartmentTree() {
        List<DepartmentEntity> list = departmentService.list();
        list.sort(Comparator.comparing(DepartmentEntity::getSort));
        List<Department> departments = BeanUtils.toList(list, Department.class);
        List<TreeNode> nodes = departments.stream().map(department -> {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(department.getId());
            treeNode.setKey(department.getId());
            treeNode.setParentId(department.getParentId());
            treeNode.setTitle(department.getName());
            treeNode.setData(department);
            return treeNode;
        }).toList();
        List<TreeNode> treeNodes = TreeUtils.buildTree(nodes, 0L);
        return ResponseHelper.buildSuccessfulResponse(treeNodes);
    }

    @Operation(summary = "获取部门树")
    @GetMapping("/public/tree")
    public GenericResponse<List<TreeNode>> getPublicDepartmentTree() {
        List<DepartmentEntity> list = departmentService.list(
                Wrappers.<DepartmentEntity>lambdaQuery()
                        .eq(DepartmentEntity::getStatus, DisEnableStatus.ENABLE.getValue())
        );
        list.sort(Comparator.comparing(DepartmentEntity::getSort));
        List<Department> departments = BeanUtils.toList(list, Department.class);
        List<TreeNode> nodes = departments.stream().map(department -> {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(department.getId());
            treeNode.setKey(department.getId());
            treeNode.setParentId(department.getParentId());
            treeNode.setTitle(department.getName());
            treeNode.setData(department);
            return treeNode;
        }).toList();
        List<TreeNode> treeNodes = TreeUtils.buildTree(nodes, 0L);
        return ResponseHelper.buildSuccessfulResponse(treeNodes);
    }

}
