package com.onezol.vertex.framework.support.aspect;

import com.onezol.vertex.framework.common.annotation.Dictionary;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Slf4j
@Aspect
@Component
public class DictionaryAspect {

    @Pointcut("@annotation(com.onezol.vertex.framework.common.annotation.UseDictionary)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object beforeSetMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取触发的对象实例
        Object target = joinPoint.getTarget();

        // 获取触发的方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getName();

        // 推断字段名（假设set方法遵循JavaBean命名规范）
        String fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);

        // 通过反射获取字段
        Field field = target.getClass().getDeclaredField(fieldName);

        // 检查字段上是否存在@Dictionary注解
        if (field.isAnnotationPresent(Dictionary.class)) {
            // 在这里添加你的字典处理逻辑，例如：
            // 你可以根据dictCode查询字典，然后处理value（例如校验、转换等）
        }

        return joinPoint.proceed();
    }

}
