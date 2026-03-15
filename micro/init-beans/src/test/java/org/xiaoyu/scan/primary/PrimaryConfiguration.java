package org.xiaoyu.scan.primary;

import org.xiaoyu.micro.annotation.Bean;
import org.xiaoyu.micro.annotation.Configuration;
import org.xiaoyu.micro.annotation.Primary;

@Configuration
public class PrimaryConfiguration {

    @Primary
    @Bean
    DogBean husky() {
        return new DogBean("Husky");
    }

    @Bean
    DogBean teddy() {
        return new DogBean("Teddy");
    }
}
