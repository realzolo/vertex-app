package com.onezol.vertx.framework.support.support;

import com.onezol.vertx.framework.support.event.ApplicationClassScanEvent;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 类扫描器<br>
 * SpringBoot 启动后，扫描当前项目下的所有类(不局限于Spring Bean)。 扫描完成后，发布一个 ApplicationClassScanEvent 事件，事件中包含所有扫描到的类，用于其它 Listener 使用。
 */
@Component
public class ApplicationClassesScanner implements ApplicationListener<ApplicationStartedEvent> {

    private final ApplicationEventPublisher eventPublisher;

    public ApplicationClassesScanner(ApplicationEventPublisher publisher) {
        this.eventPublisher = publisher;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationStartedEvent event) {
        Set<Class<?>> classes = this.scan(event);

        int size = classes.size();
        List<Class<?>> interfaces = new ArrayList<>(size / 4);
        List<Class<?>> enums = new ArrayList<>(size / 8);
        List<Class<?>> exceptions = new ArrayList<>(size / 16);
        List<Class<?>> records = new ArrayList<>(size / 8);
        List<Class<?>> annotations = new ArrayList<>(size / 8);
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotation()) {
                annotations.add(clazz);
            } else if (clazz.isEnum()) {
                enums.add(clazz);
            } else if (clazz.isInterface()) {
                interfaces.add(clazz);
            } else if (clazz.isRecord()) {
                records.add(clazz);
            } else if (Throwable.class.isAssignableFrom(clazz)){
                exceptions.add(clazz);
            }
        }

        ApplicationClassScanEvent applicationClassScanEvent = new ApplicationClassScanEvent(this);
        applicationClassScanEvent.setClasses(classes.toArray(new Class<?>[0]));
        applicationClassScanEvent.setInterfaces(interfaces.toArray(new Class<?>[0]));
        applicationClassScanEvent.setEnums(enums.toArray(new Class<?>[0]));
        applicationClassScanEvent.setExceptions(exceptions.toArray(new Class<?>[0]));
        applicationClassScanEvent.setRecords(records.toArray(new Class<?>[0]));
        applicationClassScanEvent.setAnnotations(annotations.toArray(new Class<?>[0]));
        eventPublisher.publishEvent(applicationClassScanEvent);
    }

    /**
     * 扫描自动配置基本包下所有类
     *
     * @param event 事件
     * @return 类集合
     */
    private Set<Class<?>> scan(ApplicationStartedEvent event) {
        List<String> packages = AutoConfigurationPackages.get(event.getApplicationContext());

        Set<Class<?>> classes = new HashSet<>();
        for (String pkg : packages) {
            classes.addAll(this.scan(pkg));
        }

        return classes;
    }

    /**
     * 扫描包下所有类
     *
     * @param packageName 包名
     * @return 类集合
     */
    private Set<Class<?>> scan(String packageName) {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        try {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    ClassUtils.convertClassNameToResourcePath(packageName) + "/**/*.class";
            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);

            Set<Class<?>> classes = new HashSet<>();
            for (Resource resource : resources) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                String className = metadataReader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(className);
                classes.add(clazz);
            }
            return classes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
