package org.xiaoyu.imported;

import org.xiaoyu.micro.annotation.Bean;
import org.xiaoyu.micro.annotation.Configuration;

import java.time.ZonedDateTime;

@Configuration
public class ZonedDateConfiguration {

    @Bean
    ZonedDateTime startZonedDateTime() {
        return ZonedDateTime.now();
    }
}
