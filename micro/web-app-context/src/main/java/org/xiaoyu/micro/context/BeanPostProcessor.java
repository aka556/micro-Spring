package org.xiaoyu.micro.context;

public interface BeanPostProcessor {

    /**
     * invoked after new Bean().
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * invoked after Bean.init() called.
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * invoked before bean.setXyz() called.
     */
    default Object postProcessOnSetProperty(Object bean, String beanName) {
        return bean;
    }
}
