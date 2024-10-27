package com.onezol.vertex.framework.security.api.service;

import com.onezol.vertex.framework.security.api.model.dto.OnlineUser;
import com.onezol.vertex.framework.security.api.model.pojo.LoginUser;

import java.util.Set;

public interface OnlineUserService {

    /**
     * 添加在线用户
     *
     * @param loginUser      登录用户
     * @param expirationTime 过期时间(秒)
     */
    void addOnlineUser(LoginUser loginUser, Integer expirationTime);

    /**
     * 移除在线用户
     *
     * @param userId 用户ID
     */
    void removeOnlineUser(Long userId);

    /**
     * 移除在线用户
     *
     * @param key MD5加密后的用户ID
     */
    void removeOnlineUser(String key);

    /**
     * 分页查询在线用户
     *
     * @param pageNo   页码
     * @param pageSize 每页大小
     * @return 在线用户信息
     */
    Set<OnlineUser> getOnlineUsers(int pageNo, int pageSize);

    /**
     * 获取在线用户总数
     *
     * @return 在线用户总数
     */
    Long getOnlineUserCount();

}
