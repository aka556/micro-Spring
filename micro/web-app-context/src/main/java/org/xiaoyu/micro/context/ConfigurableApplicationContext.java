package org.xiaoyu.micro.context;

import jakarta.annotation.Nullable;

import java.util.List;

public interface ConfigurableApplicationContext extends ApplicationContext {

    List<BeanDefinition> findBeanDefinitions(Class<?> type);

    @Nullable
    BeanDefinition findBeanDefinition(Class<?> type);

    @Nullable
    BeanDefinition findBeanDefinition(String name);

    @Nullable
    BeanDefinition findBeanDefinition(String name, Class<?> requiredType);

    /**
     * 创建一个Bean实例，并将其注册为早期单例对象.
     */
    Object createBeanAsEarlySingleton(BeanDefinition def);
}
