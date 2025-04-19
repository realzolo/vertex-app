package com.onezol.vertex.framework.security.biz.service;

import com.onezol.vertex.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertex.framework.common.util.NetworkUtils;
import com.onezol.vertex.framework.common.util.ServletUtils;
import com.onezol.vertex.framework.security.api.enumeration.LoginTypeEnum;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.model.entity.LoginHistoryEntity;
import com.onezol.vertex.framework.security.api.service.LoginHistoryService;
import com.onezol.vertex.framework.security.biz.mapper.LoginHistoryMapper;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginHistoryServiceImpl extends BaseServiceImpl<LoginHistoryMapper, LoginHistoryEntity> implements LoginHistoryService {

    /**
     * 创建登录记录
     *
     * @param user 用户信息
     */
    @Override
    public void createLoginRecord(User user, final LoginTypeEnum loginType) {
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

}
