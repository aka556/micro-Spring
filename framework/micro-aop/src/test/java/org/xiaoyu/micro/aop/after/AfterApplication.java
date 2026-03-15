package org.xiaoyu.micro.aop.after;

import org.xiaoyu.micro.annotation.Bean;
import org.xiaoyu.micro.annotation.ComponentScan;
import org.xiaoyu.micro.annotation.Configuration;
import org.xiaoyu.micro.aop.AroundProxyBeanPostProcessor;

@Configuration
@ComponentScan
public class AfterApplication {

    @Bean
    AroundProxyBeanPostProcessor createAroundProxyBeanPostProcessor() {
        return new AroundProxyBeanPostProcessor();
    }
}
