package com.onezol.vertex.framework.security.api.service;

import com.onezol.vertex.framework.common.model.PagePack;
import com.onezol.vertex.framework.security.api.model.LoginUserDetails;
import com.onezol.vertex.framework.security.api.model.dto.LoginUser;

public interface LoginUserService {

    /**
     * 添加登录用户
     *
     * @param loginUserDetails 登录用户信息
     * @param token            token
     */
    void addLoginUser(LoginUserDetails loginUserDetails, String token);

    /**
     * 移除登录用户
     *
     * @param userId 用户ID
     */
    void removeLoginUser(Long userId);


    /**
     * 分页查询登录用户
     *
     * @param pageNumber 页码
     * @param pageSize   每页大小
     * @return 登录用户信息
     */
    PagePack<LoginUser> getLoginUserPage(long pageNumber, long pageSize);

    /**
     * 获取登录用户总数
     *
     * @return 登录用户总数
     */
    Long getLoginUserCount();

}
