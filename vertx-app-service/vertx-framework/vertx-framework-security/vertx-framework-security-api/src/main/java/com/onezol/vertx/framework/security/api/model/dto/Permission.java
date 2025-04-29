package com.onezol.vertx.framework.security.api.model.dto;

import com.onezol.vertx.framework.common.model.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class Permission extends BaseDTO {

    /**
     * 标题
     */
    private String title;

    /**
     * 上级菜单 ID
     */
    private Long parentId;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件名称
     */
    private String name;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 重定向地址
     */
    private String redirect;

    /**
     * 图标
     */
    private String icon;

    /**
     * 是否外链
     */
    private Boolean isExternal;

    /**
     * 是否缓存
     */
    private Boolean isCache;

    /**
     * 是否隐藏
     */
    private Boolean isHidden;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态
     */
    private Integer status;

}
