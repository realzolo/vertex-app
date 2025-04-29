package com.onezol.vertx.framework.support.aspect;

import com.onezol.vertx.framework.common.annotation.RateLimit;
import com.onezol.vertx.framework.common.constant.enumeration.ServiceStatus;
import com.onezol.vertx.framework.common.exception.RuntimeServiceException;
import com.onezol.vertx.framework.common.util.EncryptUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 接口限流切面
 */
@Slf4j
@Aspect
@Component
public class RateLimitAspect {

    private static final String RATE_LIMIT_LOCK_KEY = "RATE_LIMIT_LOCK_KEY";

    private final RedissonClient redissonClient;

    public RateLimitAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint point, RateLimit rateLimit) throws Throwable {
        String prefix = rateLimit.preKey();
        String key = generateRedisKey(point, prefix);

        //限制窗口时间
        int time = rateLimit.times();
        //获取注解中的令牌数
        int maxCount = rateLimit.maxCount();
        //获取注解中的时间单位
        TimeUnit timeUnit = rateLimit.timeUnit();

        //分布式计数器
        RAtomicLong atomicLong = redissonClient.getAtomicLong(key);

        if (!atomicLong.isExists() || atomicLong.remainTimeToLive() <= 0) {
            atomicLong.set(0);
            atomicLong.expire(time, timeUnit);
        }

        long count = atomicLong.incrementAndGet();
        if (count > maxCount) {
            throw new RuntimeServiceException(ServiceStatus.TOO_MANY_REQUESTS, rateLimit.message());
        }

        // 继续执行目标方法
        return point.proceed();
    }

    public String generateRedisKey(ProceedingJoinPoint point, String prefix) {
        //获取方法签名
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        //获取方法
        Method method = methodSignature.getMethod();
        //获取全类名
        String className = method.getDeclaringClass().getName();

        // 构建Redis中的key，加入类名、方法名以区分不同接口的限制
        return String.format("%s:%s:%s", RATE_LIMIT_LOCK_KEY, prefix, EncryptUtils.encryptWithMD5(String.format("%s-%s", className, method)));
    }

}
