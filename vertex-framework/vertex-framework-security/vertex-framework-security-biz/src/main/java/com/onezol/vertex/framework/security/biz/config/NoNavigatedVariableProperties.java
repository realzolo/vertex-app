package com.onezol.vertex.framework.security.biz.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * 此类声明的所有变量为配置文件中没有使用@Value或其它可被IDEA识别的方式注入的变量, 但在项目中使用了。<br>
 * 在此类中显示的注入这些变量，仅仅只是为了让application.yaml配置文件没有警告提示。<br><br>
 * <b>注意: 不要在业务中使用此类<b><br>
 * <b>注意: 不要在业务中使用此类<b><br>
 * <b>注意: 不要在业务中使用此类<b><br>
 */
//@Configuration
@SuppressWarnings("unused")
public class NoNavigatedVariableProperties {

    @Value("${application.jwt.secret-key:null}")
    private String secretKey;

    @Value("${application.jwt.expiration-time:null}")
    private String expirationTime;

}

