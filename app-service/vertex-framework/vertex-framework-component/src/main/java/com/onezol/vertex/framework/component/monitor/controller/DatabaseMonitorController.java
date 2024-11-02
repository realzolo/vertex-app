package com.onezol.vertex.framework.component.monitor.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.onezol.vertex.framework.common.constant.enumeration.BizHttpStatusEnum;
import com.onezol.vertex.framework.common.exception.RuntimeBizException;
import com.onezol.vertex.framework.common.helper.ResponseHelper;
import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.common.model.PlainPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Set;

@Tag(name = "数据库监控")
@Slf4j
@RestController
@RequestMapping("/monitor/database")
public class DatabaseMonitorController {

    /**
     * Druid资源路径
     */
    private final static Set<String> DRUID_RESOURCE_SET = Set.of("basic", "datasource", "webapp", "wall");
    /**
     * Druid资源路径(需分页)
     */
    private final static Set<String> DRUID_PAGE_RESOURCE_SET = Set.of("sql", "spring");

    private final RestTemplate restTemplate;

    @Value("${server.port:8080}")
    private String port;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Value("${spring.datasource.druid.stat-view-servlet.login-username:}")
    private String username;

    @Value("${spring.datasource.druid.stat-view-servlet.login-password:}")
    private String password;

    public DatabaseMonitorController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Operation(summary = "查询数据", description = "获取Druid数据库连接池监控")
    @GetMapping("/{key}")
    public GenericResponse<Object> getData(@PathVariable("key") String key) {
        if (!DRUID_RESOURCE_SET.contains(key) && !DRUID_PAGE_RESOURCE_SET.contains(key)) {
            throw new RuntimeBizException(BizHttpStatusEnum.NOT_FOUND, "未找到Druid资源");
        }
        String path = String.format("/%s.json", key);

        // 请求数据
        JSONArray jsonArray = this.request(path);

        if (jsonArray.isEmpty()) {
            return ResponseHelper.buildSuccessfulResponse(new JSONObject());
        }
        return ResponseHelper.buildSuccessfulResponse(jsonArray.get(0));
    }

    @Operation(summary = "分页查询数据", description = "分页获取Druid数据库连接池监控")
    @GetMapping("/{key}/{orderBy}/{orderType}/{page}/{perPageCount}")
    public GenericResponse<PlainPage<Object>> getPageData(
            @PathVariable("key") String key,
            @PathVariable("orderBy") String orderBy,
            @PathVariable("orderType") String orderType,
            @PathVariable("page") Integer page,
            @PathVariable("perPageCount") Integer perPageCount
    ) {
        if (!DRUID_RESOURCE_SET.contains(key) && !DRUID_PAGE_RESOURCE_SET.contains(key)) {
            throw new RuntimeBizException(BizHttpStatusEnum.NOT_FOUND, "未找到Druid资源");
        }
        String path = String.format("/%s.json?orderBy=%s&orderType=%s&page=1&perPageCount=99999", key, orderBy, orderType);
        // 请求数据
        JSONArray data = this.request(path);
        // 由于Druid返回的数据没有总条数, 所以这里需要手动计算总条数
        JSONArray pageData = new JSONArray();
        int start = (page - 1) * perPageCount;
        int end = page * perPageCount;
        if (data.size() < start) {
            return ResponseHelper.buildSuccessfulResponse(new PlainPage<>(data, data.size(), page, perPageCount));
        }
        for (int i = start; i < end; i++) {
            if (i >= data.size()) {
                break;
            }
            pageData.add(data.get(i));
        }
        PlainPage<Object> plainPage = new PlainPage<>(pageData, data.size(), page, perPageCount);
        return ResponseHelper.buildSuccessfulResponse(plainPage);
    }

    /**
     * 请求Druid数据
     *
     * @param path 请求路径
     * @return Druid数据
     */
    private JSONArray request(String path) {
        Objects.requireNonNull(path);

        // 登录Druid, 获取cookie
        String cookie = this.requestForCookie();

        // 构造请求地址
        String url = this.getRequestUrl(path);

        // 设置Header
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        headers.add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        // 构造实体对象
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        // 请求Druid数据
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        } catch (RestClientException e) {
            throw new RuntimeBizException(BizHttpStatusEnum.INTERNAL_SERVER_ERROR, "Druid请求失败, 请检查Druid配置服务状态");
        }

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeBizException(BizHttpStatusEnum.INTERNAL_SERVER_ERROR, "Druid请求失败, 请检查Druid配置服务状态");
        }

        JSONObject result = JSON.parseObject(response.getBody());
        assert result != null;
        String content = result.getString("Content");

        // 空指针处理
        boolean pageMetrics = this.isPageMetrics(path);
        if (Objects.isNull(content)) {
            content = pageMetrics ? "[]" : "{}";
        }

        return pageMetrics ? JSON.parseArray(content) : new JSONArray(JSON.parse(content));
    }


    /**
     * 登录Druid, 获取cookie
     *
     * @return cookie
     */
    private String requestForCookie() {
        String url = this.getRequestUrl("/submitLogin");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        // 接口参数
        map.add("loginUsername", username);
        map.add("loginPassword", password);
        // 头部类型
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 构造实体对象
        HttpEntity<MultiValueMap<String, Object>> param = new HttpEntity<>(map, headers);
        // 发起请求,服务地址，请求参数，返回消息体的数据类型
        ResponseEntity<String> response = restTemplate.postForEntity(url, param, String.class);

        String body = response.getBody();
        boolean isErrorMessage = JSON.isValid(body);
        if (response.getStatusCode() != HttpStatus.OK || isErrorMessage) {
            throw new RuntimeBizException(BizHttpStatusEnum.INTERNAL_SERVER_ERROR, "Druid登录失败, 请检查Druid是否开启了Web访问权限");
        }
        if (!Objects.equals(body, "success")) {
            throw new RuntimeBizException(BizHttpStatusEnum.INTERNAL_SERVER_ERROR, "Druid登录失败, 请检查用户名密码是否正确");
        }

        String cookieStr = response.getHeaders().getFirst("Set-Cookie");
        assert cookieStr != null;
        return cookieStr.substring(0, cookieStr.indexOf(";"));
    }

    /**
     * 获取请求地址
     *
     * @param path 请求路径
     * @return 请求地址
     */
    private String getRequestUrl(String path) {
        return String.format("http://127.0.0.1:%s%s/druid%s", port, contextPath, path);
    }

    /**
     * 是否为分页指标
     *
     * @param path 请求路径
     * @return 是否为分页指标
     */
    private boolean isPageMetrics(String path) {
        return path.contains("sql.json") || path.contains("spring.json");
    }
}
