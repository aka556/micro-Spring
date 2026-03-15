package org.xiaoyu.scan.init;

import org.xiaoyu.micro.annotation.Bean;
import org.xiaoyu.micro.annotation.Configuration;
import org.xiaoyu.micro.annotation.Value;

@Configuration
public class SpecifyInitConfiguration {

    @Bean(initMethod = "init")
    SpecifyInitBean createSpecifyInitBean(@Value("${app.title}") String appTitle, @Value("${app.version}") String appVersion) {
        return new SpecifyInitBean(appTitle, appVersion);
    }
}
