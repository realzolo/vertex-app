package com.onezol.vertx.framework.support.support;

import com.onezol.vertx.framework.common.util.SpringUtils;
import com.onezol.vertx.framework.common.util.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Controller 请求路径工具类
 */
public final class RequestPathHelper {

    private RequestPathHelper() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    /**
     * 将路径转换为SpringSecurity的mvcMatchers格式<br/>
     * 需要将Restful风格的路径转换为SpringSecurity的mvcMatchers格式，例如：/user/{id}转换为/user/{*}
     */
    public static String convertPathToMvcMatcher(String path) {
        if (StringUtils.isBlank(path)) {
            return "/";
        }
        // 去掉前后的斜杠
        String trimmedPath = StringUtils.strip(path, "/");
        StringBuilder mvcMatcher = new StringBuilder();
        // 拆分路径，处理动态参数
        String[] pathSegments = trimmedPath.split("/");
        for (String segment : pathSegments) {
            if (segment.startsWith("{") && segment.endsWith("}")) {
                // 处理动态参数
                mvcMatcher.append("/{").append(segment, 1, segment.length() - 1).append("}");
            } else {
                // 普通路径段
                mvcMatcher.append("/").append(segment);
            }
        }
        return mvcMatcher.toString();
    }

    /**
     * 将路径转换为SpringSecurity的mvcMatchers格式<br/>
     * 需要将Restful风格的路径转换为SpringSecurity的mvcMatchers格式，例如：/user/{id}转换为/user/**
     */
    public static Set<String> convertPathToMvcMatcher(Collection<String> paths) {
        Set<String> mvcMatchers = new HashSet<>();
        for (String path : paths) {
            mvcMatchers.add(convertPathToMvcMatcher(path));
        }
        return mvcMatchers;
    }

    /**
     * 获取controller中存在特定注解的方法的路径
     *
     * @param clazz 注解类
     */
    public static Set<String> getControllerPaths(Class<? extends Annotation> clazz) {
        Set<String> controllerPaths = new HashSet<>();

        // RequestMappingHandlerMapping handlerMapping = SpringUtils.getBean(RequestMappingHandlerMapping.class);
        // 👆👉 根据beanName获取，解决引入spring-boot-starter-actuator后根据beanType获取多个bean报错的问题
        RequestMappingHandlerMapping handlerMapping = SpringUtils.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        for (RequestMappingInfo mappingInfo : handlerMethods.keySet()) {
            HandlerMethod handlerMethod = handlerMethods.get(mappingInfo);
            // 获取类上的注解
            Annotation annotation = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), clazz);
            if (annotation != null) {
                Set<String> patterns = getPatterns(mappingInfo);
                controllerPaths.addAll(patterns);
                continue;  // 类上有注解，不需要再判断方法上的注解
            }

            // 获取方法上的注解
            Method method = handlerMethod.getMethod();
            annotation = AnnotationUtils.findAnnotation(method, clazz);
            if (annotation != null) {
                Set<String> patterns = getPatterns(mappingInfo);
                controllerPaths.addAll(patterns);
            }
        }

        return controllerPaths;
    }

    /**
     * 获取所有controller的路径
     *
     * @return 所有controller的路径
     */
    public static Set<String> getAllControllerPaths() {
        Set<String> controllerPaths = new HashSet<>();

        RequestMappingHandlerMapping handlerMapping = SpringUtils.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        for (RequestMappingInfo mappingInfo : handlerMethods.keySet()) {
            Set<String> patterns = getPatterns(mappingInfo);
            controllerPaths.addAll(patterns);
        }

        return controllerPaths;
    }

    /**
     * 获取所有controller的方法
     *
     * @param mappingInfo mappingInfo
     * @return 所有controller的方法
     */
    private static Set<String> getPatterns(RequestMappingInfo mappingInfo) {
        Set<String> patterns = new HashSet<>();
        PathPatternsRequestCondition pathPatternsCondition = mappingInfo.getPathPatternsCondition();
        PatternsRequestCondition patternsCondition = mappingInfo.getPatternsCondition();
        if (pathPatternsCondition != null) {
            Set<PathPattern> pathPatterns = pathPatternsCondition.getPatterns();
            for (PathPattern pathPattern : pathPatterns) {
                patterns.add(pathPattern.getPatternString());
            }
        }
        if (patternsCondition != null) {
            patterns.addAll(patternsCondition.getPatterns());
        }
        return patterns;
    }

}
