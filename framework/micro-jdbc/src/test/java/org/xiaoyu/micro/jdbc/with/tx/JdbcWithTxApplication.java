package org.xiaoyu.micro.jdbc.with.tx;

import org.xiaoyu.micro.annotation.ComponentScan;
import org.xiaoyu.micro.annotation.Configuration;
import org.xiaoyu.micro.annotation.Import;
import org.xiaoyu.micro.jdbc.JdbcConfiguration;

@ComponentScan
@Configuration
@Import(JdbcConfiguration.class)
public class JdbcWithTxApplication {

}
