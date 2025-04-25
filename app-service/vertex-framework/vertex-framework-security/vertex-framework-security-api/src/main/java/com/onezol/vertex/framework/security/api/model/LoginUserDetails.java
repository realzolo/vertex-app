package com.onezol.vertex.framework.security.api.model;

import com.alibaba.fastjson2.annotation.JSONField;
import com.onezol.vertex.framework.common.constant.enumeration.AccountStatus;
import com.onezol.vertex.framework.common.model.DataPairRecord;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
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

    public LoginUserDetails(UserEntity entity, DataPairRecord department, List<DataPairRecord> roles, List<String> permissions) {
        super.setId(entity.getId());
        super.setUsername(entity.getUsername());
        super.setNickname(entity.getNickname());
        super.setDescription(entity.getDescription());
        super.setAvatar(entity.getAvatar());
        super.setGender(entity.getGender().getValue());
        super.setBirthday(entity.getBirthday());
        super.setPhone(entity.getPhone());
        super.setEmail(entity.getEmail());
        super.setStatus(entity.getStatus().getValue());
        this.password = entity.getPassword();
        this.pwdExpDate = entity.getPwdExpDate();

        super.setDepartment(department);
        super.setRoles(roles);
        super.setPermissions(permissions);
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
