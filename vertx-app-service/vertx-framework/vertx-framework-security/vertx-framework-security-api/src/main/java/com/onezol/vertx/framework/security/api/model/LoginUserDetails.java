package com.onezol.vertx.framework.security.api.model;

import com.alibaba.fastjson2.annotation.JSONField;
import com.onezol.vertx.framework.common.constant.enumeration.AccountStatus;
import com.onezol.vertx.framework.security.api.model.dto.User;
import com.onezol.vertx.framework.security.api.model.dto.UserPassword;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoginUserDetails extends User implements UserDetails {

    @JSONField(serialize = false)
    private String password;

    @JSONField(serialize = false)
    private LocalDate pwdExpDate;

    public LoginUserDetails(User user, UserPassword userPassword) {
        super.setId(user.getId());
        super.setUsername(user.getUsername());
        super.setNickname(user.getNickname());
        super.setDescription(user.getDescription());
        super.setAvatar(user.getAvatar());
        super.setGender(user.getGender());
        super.setBirthday(user.getBirthday());
        super.setPhone(user.getPhone());
        super.setEmail(user.getEmail());
        super.setStatus(user.getStatus());
        super.setDepartment(user.getDepartment());
        super.setRoles(user.getRoles());
        super.setPermissions(user.getPermissions());
        this.password = userPassword.getPassword();
        this.pwdExpDate = userPassword.getPwdExpDate();
    }

    @Override
    @JSONField(serialize = false)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String permission : super.getPermissions()) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return authorities;
    }

    @Override
    @JSONField(serialize = false)
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonExpired() {
        return !Objects.equals(super.getStatus(), AccountStatus.EXPIRED.getValue());
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonLocked() {
        return !Objects.equals(super.getStatus(), AccountStatus.LOCKED.getValue());
    }

    @Override
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {
        return this.pwdExpDate == null || this.pwdExpDate.isAfter(LocalDate.now());
    }

    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return !Objects.equals(super.getStatus(), AccountStatus.DISABLED.getValue());
    }

}
