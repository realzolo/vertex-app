package com.onezol.vertx.framework.security.biz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertx.framework.common.util.BeanUtils;
import com.onezol.vertx.framework.common.util.NetworkUtils;
import com.onezol.vertx.framework.common.util.ServletUtils;
import com.onezol.vertx.framework.security.api.enumeration.LoginType;
import com.onezol.vertx.framework.security.api.model.dto.LoginUser;
import com.onezol.vertx.framework.security.api.model.dto.User;
import com.onezol.vertx.framework.security.api.model.entity.LoginHistoryEntity;
import com.onezol.vertx.framework.security.api.service.LoginHistoryService;
import com.onezol.vertx.framework.security.api.service.UserInfoService;
import com.onezol.vertx.framework.security.biz.mapper.LoginHistoryMapper;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LoginHistoryServiceImpl extends BaseServiceImpl<LoginHistoryMapper, LoginHistoryEntity> implements LoginHistoryService {

    private final UserInfoService userInfoService;

    public LoginHistoryServiceImpl(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * 创建登录记录
     *
     * @param user 用户信息
     */
    @Override
    public void createLoginRecord(User user, final LoginType loginType) {
        Long userId = user.getId();
        LocalDateTime loginTime = LocalDateTime.now();
        String ip = NetworkUtils.getHostIp();
        String location = NetworkUtils.getAddressByIP(ip);
        UserAgent userAgent = ServletUtils.getUserAgent();
        String browser = userAgent.getBrowser().getName();
        String os = userAgent.getOperatingSystem().getName();

        LoginHistoryEntity entity = new LoginHistoryEntity();
        entity.setUserId(userId);
        entity.setLoginTime(loginTime);
        entity.setLoginType(loginType);
        entity.setIp(ip);
        entity.setBrowser(browser);
        entity.setOs(os);
        entity.setLocation(location);
        entity.setCreator(userId);
        entity.setUpdater(userId);

        this.save(entity);
    }

    /**
     * 获取用户登录信息
     *
     * @param userIds 用户ID集合
     * @return 用户登录信息集合
     */
    @Override
    public List<LoginHistoryEntity> getUserLoginDetails(List<Long> userIds) {
        return this.baseMapper.queryUserLoginDetails(userIds);
    }

    /**
     * 获取登录记录分页列表
     *
     * @param page 分页对象
     * @return 登录记录
     */
    @Override
    public PagePack<LoginUser> getLoginHistoryPage(Page<LoginHistoryEntity> page) {
        page = this.page(page);

        List<Long> userIds = page.getRecords().stream().map(LoginHistoryEntity::getUserId).distinct().toList();

        List<User> usersInfo = userInfoService.getUsersInfo(userIds);
        Map<Long, User> userMap = usersInfo.stream().collect(Collectors.toMap(User::getId, Function.identity()));

        PagePack<LoginUser> pack = PagePack.from(page, LoginUser.class);
        pack.getItems().forEach(
                loginUser -> {
                    User user = userMap.get(loginUser.getUserId());
                    if (user != null) {
                        loginUser.setUsername(user.getUsername());
                        loginUser.setNickname(user.getNickname());
                        loginUser.setAvatar(user.getAvatar());
                    }
                }
        );
        return pack;
    }

    /**
     * 获取登录记录详情
     *
     * @param id 登录记录ID
     * @return 登录记录详情
     */
    @Override
    public LoginUser getLoginHistoryById(Long id) {
        LoginHistoryEntity loginHistory = this.getById(id);
        return BeanUtils.toBean(loginHistory, LoginUser.class);
    }

}
