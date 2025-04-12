package com.onezol.vertex.framework.security.api.model.pojo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.onezol.vertex.framework.common.constant.enumeration.AccountStatusEnum;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.security.api.model.dto.SimpleDepartment;
import com.onezol.vertex.framework.security.api.model.dto.SimpleRole;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
public class LoginUser implements UserDetails {

    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime loginTime;

    /**
     * 登录IP地址
     */
    private String ip;

    /**
     * 登录地点
     */
    private String location;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 部门
     */
    private SimpleDepartment department;

    /**
     * 角色列表
     */
    private List<SimpleRole> roles = Collections.emptyList();

    /**
     * 权限列表
     */
    private List<String> permissions = Collections.emptyList();

    /**
     * 用户信息
     */
    private UserEntity details;


    public LoginUser(UserEntity userDetails) {
        this.details = userDetails;
    }

    @Override
    @JSONField(serialize = false)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return authorities;
    }

    @Override
    @JSONField(serialize = false)
    public String getPassword() {
        String password = details.getPassword();
        details.setPassword(null);
        return password;
    }

    @Override
    public String getUsername() {
        return details.getUsername();
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonExpired() {
        return !AccountStatusEnum.EXPIRED.equals(details.getStatus());
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonLocked() {
        return !AccountStatusEnum.LOCKED.equals(details.getStatus());
    }

    @Override
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {
        LocalDate pwdExpDate = details.getPwdExpDate();
        return pwdExpDate == null || pwdExpDate.isAfter(LocalDate.now());
    }

    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return !AccountStatusEnum.DISABLED.equals(details.getStatus());
    }

    public User getDetails() {
        User user = BeanUtils.toBean(details, User.class);
        user.setDepartment(department);
        user.setRoles(roles);
        user.setPermissions(permissions);
        return user;
    }

}
