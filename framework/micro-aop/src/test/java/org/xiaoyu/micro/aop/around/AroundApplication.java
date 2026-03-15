package org.xiaoyu.micro.aop.around;

import org.xiaoyu.micro.annotation.Bean;
import org.xiaoyu.micro.annotation.ComponentScan;
import org.xiaoyu.micro.annotation.Configuration;
import org.xiaoyu.micro.aop.AroundProxyBeanPostProcessor;

@Configuration
@ComponentScan
public class AroundApplication {

    @Bean
    AroundProxyBeanPostProcessor aroundProxyBeanPostProcessor() {
        return new AroundProxyBeanPostProcessor();
    }
}
