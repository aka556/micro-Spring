package org.xiaoyu.scan.destroy;

import org.xiaoyu.micro.annotation.Bean;
import org.xiaoyu.micro.annotation.Configuration;
import org.xiaoyu.micro.annotation.Value;

@Configuration
public class SpecifyIDestroyConfiguration {

    @Bean(destroyMethod = "destroy")
    SpecifyDestroyBean createSpecifyDestroyBean(@Value("${app.title}") String appTitle) {
        return new SpecifyDestroyBean(appTitle);
    }
}
