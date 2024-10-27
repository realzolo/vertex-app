package com.onezol.vertex.framework.security.api.model.pojo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.onezol.vertex.framework.common.constant.enums.AccountStatus;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.common.util.CodecUtils;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
     * 角色列表
     */
    private Set<String> roles = Collections.emptySet();

    /**
     * 权限列表
     */
    private Set<String> permissions = Collections.emptySet();

    /**
     * 用户信息
     */
    private UserEntity orgUser;

    /**
     * 权限列表
     */
    private Set<GrantedAuthority> authorities;

    public LoginUser(UserEntity orgUser) {
        this.orgUser = orgUser;
    }

    @Override
    @JSONField(serialize = false)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            authorities = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        }
        return authorities;
    }

    @Override
    @JSONField(serialize = false)
    public String getPassword() {
        String password = orgUser.getPassword();
        orgUser.setPassword(null);
        return password;
    }

    @Override
    public String getUsername() {
        return orgUser.getUsername();
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonExpired() {
        return !Integer.valueOf(AccountStatus.EXPIRED.getCode()).equals(orgUser.getStatus());
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonLocked() {
        return !Integer.valueOf(AccountStatus.LOCKED.getCode()).equals(orgUser.getStatus());
    }

    @Override
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {
        LocalDate pwdExpDate = orgUser.getPwdExpDate();
        return pwdExpDate == null || pwdExpDate.isAfter(LocalDate.now());
    }

    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return !Integer.valueOf(AccountStatus.DISABLED.getCode()).equals(orgUser.getStatus());
    }

    public String getKey() {
        return orgUser.getCode() + "@" + orgUser.getUsername();
    }

    public void setRoles(Set<String> roles) {
        this.roles = Objects.isNull(roles) ? Collections.emptySet() : roles;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = Objects.isNull(permissions) ? Collections.emptySet() : permissions;
    }

    public User getUser() {
        User user = BeanUtils.toBean(orgUser, User.class);
        user.setRoles(roles);
        user.setPermissions(permissions);
        return user;
    }

}
