package com.onezol.vertx.framework.schedule.config;

import com.onezol.vertx.framework.schedule.constant.JobConstants;
import com.onezol.vertx.framework.schedule.http.JobBatchHttpService;
import com.onezol.vertx.framework.schedule.http.JobGroupHttpService;
import com.onezol.vertx.framework.schedule.http.JobHttpService;
import com.onezol.vertx.framework.schedule.support.SnailJobAuthenticator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * Snail Job Http Exchange 配置类，用于配置与 Snail Job 服务进行 HTTP 交互的客户端。
 * 该类会根据配置启用 Snail Job 功能，并创建不同类型的 HTTP 服务客户端。
 */
@Configuration
@ConditionalOnProperty(prefix = "snail-job", value = "enabled", havingValue = "true", matchIfMissing = true)
public class SnailJobHttpExchangeConfiguration {

    private final ConfigurableBeanFactory configurableBeanFactory;

    private final SnailJobAuthenticator snailJobAuthenticator;

    @Value("${snail-job.namespace}")
    private String namespace;

    public SnailJobHttpExchangeConfiguration(ConfigurableBeanFactory configurableBeanFactory, SnailJobAuthenticator snailJobAuthenticator) {
        this.configurableBeanFactory = configurableBeanFactory;
        this.snailJobAuthenticator = snailJobAuthenticator;
    }

    /**
     * 创建 JobHttpService 实例
     *
     * @param webClientBuilder WebClient 构建器
     * @return JobHttpService 实例
     */
    @Bean
    public JobHttpService jobHttpService(WebClient.Builder webClientBuilder) {
        WebClient webClient = createWebClient(webClientBuilder);
        return HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .embeddedValueResolver(configurableBeanFactory::resolveEmbeddedValue)
                .build()
                .createClient(JobHttpService.class);
    }

    /**
     * 创建 JobBatchHttpService 实例
     *
     * @param webClientBuilder WebClient 构建器
     * @return JobBatchHttpService 实例
     */
    @Bean
    public JobBatchHttpService jobBatchHttpService(WebClient.Builder webClientBuilder) {
        WebClient webClient = createWebClient(webClientBuilder);
        return HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .embeddedValueResolver(configurableBeanFactory::resolveEmbeddedValue)
                .build()
                .createClient(JobBatchHttpService.class);
    }

    /**
     * 创建 JobGroupHttpService 实例
     *
     * @param webClientBuilder WebClient 构建器
     * @return JobGroupHttpService 实例
     */
    @Bean
    public JobGroupHttpService jobGroupHttpService(WebClient.Builder webClientBuilder) {
        WebClient webClient = createWebClient(webClientBuilder);
        return HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .embeddedValueResolver(configurableBeanFactory::resolveEmbeddedValue)
                .build()
                .createClient(JobGroupHttpService.class);
    }

    /**
     * 创建并配置 WebClient 实例，设置默认请求头和认证过滤器。
     *
     * @param webClientBuilder WebClient 构建器
     * @return 配置好的 WebClient 实例
     */
    private WebClient createWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .defaultHeaders(headers -> {
                    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    headers.add(JobConstants.NAMESPACE_ID_HEADER, namespace);
                })
                .filter((request, next) -> {
                    String token = snailJobAuthenticator.getToken();
                    ClientRequest newRequest = ClientRequest.from(request)
                            .headers(httpHeaders -> httpHeaders.add(JobConstants.AUTH_TOKEN_HEADER, token))
                            .build();
                    return next.exchange(newRequest);
                })
                .build();
    }
}