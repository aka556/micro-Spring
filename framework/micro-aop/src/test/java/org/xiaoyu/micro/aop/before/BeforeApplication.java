package org.xiaoyu.micro.aop.before;

import org.xiaoyu.micro.annotation.Bean;
import org.xiaoyu.micro.annotation.ComponentScan;
import org.xiaoyu.micro.annotation.Configuration;
import org.xiaoyu.micro.aop.AroundProxyBeanPostProcessor;

@Configuration
@ComponentScan
public class BeforeApplication {

    @Bean
    AroundProxyBeanPostProcessor createAroundProxyBeanPostProcessor() {
        return new AroundProxyBeanPostProcessor();
    }
}
