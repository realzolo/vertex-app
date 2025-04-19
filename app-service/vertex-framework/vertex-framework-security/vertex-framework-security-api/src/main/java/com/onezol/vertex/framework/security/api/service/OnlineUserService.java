package com.onezol.vertex.framework.security.api.service;

import com.onezol.vertex.framework.security.api.model.LoginUserDetails;
import com.onezol.vertex.framework.security.api.model.dto.OnlineUser;

import java.util.List;

public interface OnlineUserService {

    /**
     * 添加在线用户
     *
     * @param loginUserDetails 登录用户信息
     * @param token            token
     */
    void addOnlineUser(LoginUserDetails loginUserDetails, String token);

    /**
     * 移除在线用户
     *
     * @param userId 用户ID
     */
    void removeOnlineUser(Long userId);


    /**
     * 分页查询在线用户
     *
     * @param pageNumber 页码
     * @param pageSize   每页大小
     * @return 在线用户信息
     */
    List<OnlineUser> getOnlineUsers(int pageNumber, int pageSize);

    /**
     * 获取在线用户总数
     *
     * @return 在线用户总数
     */
    Long getOnlineUserCount();

}
