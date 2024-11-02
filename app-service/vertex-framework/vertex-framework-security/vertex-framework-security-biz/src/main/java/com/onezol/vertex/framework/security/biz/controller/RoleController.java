package com.onezol.vertex.framework.security.biz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.helper.ResponseHelper;
import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.common.model.LabelValue;
import com.onezol.vertex.framework.common.model.PlainPage;
import com.onezol.vertex.framework.common.mvc.controller.BaseController;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.common.util.JsonUtils;
import com.onezol.vertex.framework.security.api.annotation.RestrictAccess;
import com.onezol.vertex.framework.security.api.model.dto.Role;
import com.onezol.vertex.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertex.framework.security.api.service.PermissionService;
import com.onezol.vertex.framework.security.api.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


@RestrictAccess
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController<RoleEntity> {
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @GetMapping("/route")
    public GenericResponse<List<Object>> route() {
        String mockRoutes = "[{\"id\":1000,\"parentId\":0,\"title\":\"系统管理\",\"type\":1,\"path\":\"/system\",\"name\":\"System\",\"component\":\"Layout\",\"redirect\":\"/system/user\",\"icon\":\"settings\",\"isExternal\":false,\"isCache\":false,\"isHidden\":false,\"sort\":1,\"children\":[{\"id\":1010,\"parentId\":1000,\"title\":\"用户管理\",\"type\":2,\"path\":\"/system/user\",\"name\":\"SystemUser\",\"component\":\"system/user/index\",\"icon\":\"user\",\"isExternal\":false,\"isCache\":false,\"isHidden\":false,\"sort\":1},{\"id\":1030,\"parentId\":1000,\"title\":\"角色管理\",\"type\":2,\"path\":\"/system/role\",\"name\":\"SystemRole\",\"component\":\"system/role/index\",\"icon\":\"user-group\",\"isExternal\":false,\"isCache\":false,\"isHidden\":false,\"sort\":2},{\"id\":1050,\"parentId\":1000,\"title\":\"菜单管理\",\"type\":2,\"path\":\"/system/menu\",\"name\":\"SystemMenu\",\"component\":\"system/menu/index\",\"icon\":\"menu\",\"isExternal\":false,\"isCache\":false,\"isHidden\":false,\"sort\":3},{\"id\":1060,\"parentId\":1000,\"title\":\"部门管理\",\"type\":2,\"path\":\"/system/dept\",\"name\":\"SystemDept\",\"component\":\"system/dept/index\",\"icon\":\"mind-mapping\",\"isExternal\":false,\"isCache\":false,\"isHidden\":false,\"sort\":4},{\"id\":1070,\"parentId\":1000,\"title\":\"字典管理\",\"type\":2,\"path\":\"/system/dict\",\"name\":\"SystemDict\",\"component\":\"system/dict/index\",\"icon\":\"bookmark\",\"isExternal\":false,\"isCache\":false,\"isHidden\":false,\"sort\":5},{\"id\":1080,\"parentId\":1000,\"title\":\"字典项管理\",\"type\":2,\"path\":\"/system/dict/item\",\"name\":\"SystemDictItem\",\"component\":\"system/dict/item/index\",\"icon\":\"bookmark\",\"isExternal\":false,\"isCache\":false,\"isHidden\":true,\"sort\":5},{\"id\":1090,\"parentId\":1000,\"title\":\"通知公告\",\"type\":2,\"path\":\"/system/notice\",\"name\":\"SystemNotice\",\"component\":\"system/notice/index\",\"icon\":\"notification\",\"isExternal\":false,\"isCache\":false,\"isHidden\":false,\"sort\":6,\"children\":[{\"id\":1092,\"parentId\":1090,\"title\":\"详情\",\"type\":2,\"path\":\"/system/notice/detail\",\"name\":\"SystemNoticeDetail\",\"component\":\"system/notice/page/detail\",\"isExternal\":false,\"isCache\":false,\"isHidden\":true,\"permission\":\"system:notice:detail\",\"sort\":2},{\"id\":1093,\"parentId\":1090,\"title\":\"新增\",\"type\":2,\"path\":\"/system/notice/add\",\"name\":\"SystemNoticeAdd\",\"component\":\"system/notice/page/add\",\"isExternal\":false,\"isCache\":false,\"isHidden\":true,\"permission\":\"system:notice:add\",\"sort\":3}]},{\"id\":1100,\"parentId\":1000,\"title\":\"文件管理\",\"type\":2,\"path\":\"/system/file\",\"name\":\"SystemFile\",\"component\":\"system/file/index\",\"icon\":\"file\",\"isExternal\":false,\"isCache\":false,\"isHidden\":false,\"sort\":7},{\"id\":1110,\"parentId\":1000,\"title\":\"存储管理\",\"type\":2,\"path\":\"/system/storage\",\"name\":\"SystemStorage\",\"component\":\"system/storage/index\",\"icon\":\"storage\",\"isExternal\":false,\"isCache\":false,\"isHidden\":false,\"sort\":8},{\"id\":1190,\"parentId\":1000,\"title\":\"系统配置\",\"type\":2,\"path\":\"/system/config\",\"name\":\"SystemConfig\",\"component\":\"system/config/index\",\"icon\":\"config\",\"isExternal\":false,\"isCache\":false,\"isHidden\":false,\"sort\":999}]},{\"id\":2000,\"parentId\":0,\"title\":\"系统监控\",\"type\":1,\"path\":\"/monitor\",\"name\":\"Monitor\",\"component\":\"Layout\",\"redirect\":\"/monitor/online\",\"icon\":\"computer\",\"isExternal\":false,\"isCache\":false,\"isHidden\":false,\"sort\":2,\"children\":[{\"id\":2010,\"parentId\":2000,\"title\":\"在线用户\",\"type\":2,\"path\":\"/monitor/online\",\"name\":\"MonitorOnline\",\"component\":\"monitor/online/index\",\"icon\":\"user\",\"isExternal\":false,\"isCache\":false,\"isHidden\":false,\"sort\":1},{\"id\":2020,\"parentId\":2000,\"title\":\"系统日志\",\"type\":2,\"path\":\"/monitor/log\",\"name\":\"MonitorLog\",\"component\":\"monitor/log/index\",\"icon\":\"history\",\"isExternal\":false,\"isCache\":false,\"isHidden\":false,\"sort\":2}]},{\"id\":3000,\"parentId\":0,\"title\":\"任务调度\",\"type\":1,\"path\":\"/schedule\",\"name\":\"Schedule\",\"component\":\"Layout\",\"redirect\":\"/schedule/job\",\"icon\":\"schedule\",\"isExternal\":false,\"isCache\":false,\"isHidden\":false,\"sort\":3,\"children\":[{\"id\":3010,\"parentId\":3000,\"title\":\"任务管理\",\"type\":2,\"path\":\"/schedule/job\",\"name\":\"ScheduleJob\",\"component\":\"schedule/job/index\",\"icon\":\"select-all\",\"isExternal\":false,\"isCache\":false,\"isHidden\":false,\"sort\":1},{\"id\":3020,\"parentId\":3000,\"title\":\"任务日志\",\"type\":2,\"path\":\"/schedule/log\",\"name\":\"ScheduleLog\",\"component\":\"schedule/log/index\",\"icon\":\"find-replace\",\"isExternal\":false,\"isCache\":false,\"isHidden\":false,\"sort\":2}]},{\"id\":9000,\"parentId\":0,\"title\":\"代码生成\",\"type\":1,\"path\":\"/tool\",\"name\":\"Tool\",\"component\":\"Layout\",\"redirect\":\"/tool/generator\",\"icon\":\"tool\",\"isExternal\":false,\"isCache\":false,\"isHidden\":false,\"sort\":9,\"children\":[{\"id\":9010,\"parentId\":9000,\"title\":\"代码生成\",\"type\":2,\"path\":\"/tool/generator\",\"name\":\"ToolGenerator\",\"component\":\"tool/generator/index\",\"icon\":\"code\",\"isExternal\":false,\"isCache\":false,\"isHidden\":false,\"sort\":1}]},{\"id\":10000,\"parentId\":0,\"title\":\"关于项目\",\"type\":1,\"path\":\"/project\",\"name\":\"Project\",\"component\":\"Layout\",\"icon\":\"apps\",\"isExternal\":false,\"isCache\":false,\"isHidden\":false,\"sort\":999,\"children\":[{\"id\":10001,\"parentId\":10000,\"title\":\"接口文档\",\"type\":2,\"path\":\"https://api.continew.top/doc.html\",\"icon\":\"code-square\",\"isExternal\":true,\"isCache\":false,\"isHidden\":false,\"sort\":1},{\"id\":10002,\"parentId\":10000,\"title\":\"Gitee\",\"type\":2,\"path\":\"https://gitee.com/continew/continew-admin\",\"icon\":\"gitee\",\"isExternal\":true,\"isCache\":false,\"isHidden\":false,\"sort\":2},{\"id\":10003,\"parentId\":10000,\"title\":\"GitHub\",\"type\":2,\"path\":\"https://github.com/continew-org/continew-admin\",\"icon\":\"github\",\"isExternal\":true,\"isCache\":false,\"isHidden\":false,\"sort\":3}]}]";
        List<Object> routes = JsonUtils.jsonArrayToList(mockRoutes, Object.class);
        return ResponseHelper.buildSuccessfulResponse(routes);
    }

    @GetMapping("/page")
    public GenericResponse<PlainPage<RoleEntity>> getRolePage(
            @RequestParam(value = "page", required = false) Integer pageNumber,
            @RequestParam(value = "size", required = false) Integer pageSize
    ) {
        Page<RoleEntity> page = this.getPage(pageNumber, pageSize);
        Page<RoleEntity> quriedPage = roleService.page(page);
        PlainPage<RoleEntity> resultPage = PlainPage.from(quriedPage);
        return ResponseHelper.buildSuccessfulResponse(resultPage);
    }

    @GetMapping("/dict")
    public GenericResponse<List<LabelValue<String, Object>>> getRoleDict() {
        List<RoleEntity> roles = roleService.list();
        List<LabelValue<String, Object>> dictionaries = new ArrayList<>(roles.size());
        for (RoleEntity role : roles) {
            dictionaries.add(new LabelValue<>(role.getName(), role.getCode()));
        }
        return ResponseHelper.buildSuccessfulResponse(dictionaries);
    }

    @GetMapping("/{id}")
    public GenericResponse<Role> getRoleInfo(@PathVariable(value = "id") Long roleId) {
        RoleEntity entity = roleService.getById(roleId);
        Role role = BeanUtils.toBean(entity, Role.class);
        Set<Long> permissionIds = permissionService.getRolePermissionIds(Collections.singletonList(entity.getId()));
        role.setPermissionIds(permissionIds);
        return ResponseHelper.buildSuccessfulResponse(role);
    }

    @PutMapping("/{id}")
    public GenericResponse<Void> updateRole(@RequestBody Role role) {
        roleService.updateRole(role);
        return ResponseHelper.buildSuccessfulResponse();
    }
}
