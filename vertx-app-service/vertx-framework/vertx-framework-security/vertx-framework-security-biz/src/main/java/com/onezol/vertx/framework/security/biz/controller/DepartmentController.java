package com.onezol.vertx.framework.security.biz.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertx.framework.common.constant.enumeration.DisEnableStatus;
import com.onezol.vertx.framework.common.model.GenericResponse;
import com.onezol.vertx.framework.common.model.TreeNode;
import com.onezol.vertx.framework.common.mvc.controller.BaseController;
import com.onezol.vertx.framework.common.util.Asserts;
import com.onezol.vertx.framework.common.util.BeanUtils;
import com.onezol.vertx.framework.common.util.TreeUtils;
import com.onezol.vertx.framework.security.api.model.dto.Department;
import com.onezol.vertx.framework.security.api.model.entity.DepartmentEntity;
import com.onezol.vertx.framework.security.api.model.payload.DepartmentSavePayload;
import com.onezol.vertx.framework.security.api.service.DepartmentService;
import com.onezol.vertx.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController extends BaseController<DepartmentEntity> {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Operation(summary = "新增部门")
    @PostMapping
    @PreAuthorize("@Security.hasPermission('system:department:create')")
    public GenericResponse<Department> createDepartment(@RequestBody DepartmentSavePayload payload) {
        Department department = departmentService.createOrUpdate(payload);
        return ResponseHelper.buildSuccessfulResponse(department);
    }

    @Operation(summary = "更新部门")
    @PutMapping("/{id}")
    @PreAuthorize("@Security.hasPermission('system:department:update')")
    public GenericResponse<Department> updateDepartment(@RequestBody DepartmentSavePayload payload) {
        Department department = departmentService.createOrUpdate(payload);
        return ResponseHelper.buildSuccessfulResponse(department);
    }

    @Operation(summary = "删除部门")
    @DeleteMapping("/{id}")
    @PreAuthorize("@Security.hasPermission('system:department:delete')")
    public GenericResponse<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.removeById(id);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @Operation(summary = "获取部门详情")
    @GetMapping("/{id}")
    @PreAuthorize("@Security.hasPermission('system:department:detail')")
    public GenericResponse<Department> getDepartmentDetail(@PathVariable("id") Long id) {
        DepartmentEntity department = departmentService.getById(id);
        Asserts.notNull(department, "部门不存在");
        Department bean = BeanUtils.toBean(department, Department.class);
        return ResponseHelper.buildSuccessfulResponse(bean);
    }

    @Operation(summary = "获取部门列表")
    @GetMapping("/list")
    @PreAuthorize("@Security.hasPermission('system:department:list')")
    public GenericResponse<List<Department>> getDepartmentList() {
        List<DepartmentEntity> list = departmentService.list();
        List<Department> departments = BeanUtils.toList(list, Department.class);
        return ResponseHelper.buildSuccessfulResponse(departments);
    }

    @Operation(summary = "获取部门树")
    @GetMapping("/tree")
    @PreAuthorize("@Security.hasPermission('system:department:list')")
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
