package com.onezol.vertex.framework.security.biz.service;

import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.model.pojo.LoginUser;
import com.onezol.vertex.framework.security.api.service.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SecurityUserDetailsServiceImpl implements UserDetailsService {
    private final UserAuthService userAuthService;

    public SecurityUserDetailsServiceImpl(@Lazy UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }
//    private final MenuService menuService;
//    private final RoleService roleService;

//    @Autowired
//    public SecurityUserDetailsServiceImpl(@Lazy UserAuthService userAuthService, MenuService menuService, RoleService roleService) {
//        this.userAuthService = userAuthService;
//        this.menuService = menuService;
//        this.roleService = roleService;
//    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userAuthService.getUserByUsername(username);
        if (userEntity == null) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        LoginUser user = new LoginUser(userEntity);
//        Set<String> roles = roleService.getUserRoleKeys(userEntity.getId());
//        Set<String> perms = menuService.getUserPermKeys(userEntity.getId());
//        user.setRoles(roles);
//        user.setPermissions(perms);

        return user;
    }

}
