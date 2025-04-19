package com.onezol.vertex.framework.security.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.model.PageModel;
import com.onezol.vertex.framework.common.mvc.service.BaseService;
import com.onezol.vertex.framework.security.api.enumeration.LoginTypeEnum;
import com.onezol.vertex.framework.security.api.model.dto.LoginUser;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.model.entity.LoginHistoryEntity;

import java.util.List;

public interface LoginHistoryService extends BaseService<LoginHistoryEntity> {

    /**
     * 创建登录记录
     *
     * @param user 用户信息
     */
    void createLoginRecord(User user, LoginTypeEnum loginType);

    /**
     * 获取用户登录信息
     * @param userIds 用户ID集合
     * @return 用户登录信息集合
     */
    List<LoginHistoryEntity> getUserLoginDetails(List<Long> userIds);


    /**
     * 获取登录记录分页列表
     * @param page 分页对象
     * @return 登录记录
     */
    PageModel<LoginUser> getLoginHistoryPage(Page<LoginHistoryEntity> page);

    /**
     * 获取登录记录详情
     * @param id 登录记录ID
     * @return 登录记录详情
     */
    LoginUser getLoginHistoryById(Long id);
}
