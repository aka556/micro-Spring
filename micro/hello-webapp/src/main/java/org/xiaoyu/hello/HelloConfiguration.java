package org.xiaoyu.hello;

import org.xiaoyu.micro.annotation.ComponentScan;
import org.xiaoyu.micro.annotation.Configuration;
import org.xiaoyu.micro.annotation.Import;
import org.xiaoyu.micro.jdbc.JdbcConfiguration;
import org.xiaoyu.micro.web.WebMvcConfiguration;

@ComponentScan
@Configuration
@Import({ JdbcConfiguration.class, WebMvcConfiguration.class})
public class HelloConfiguration {
}
