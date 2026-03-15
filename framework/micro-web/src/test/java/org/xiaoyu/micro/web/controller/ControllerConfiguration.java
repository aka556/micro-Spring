package org.xiaoyu.micro.web.controller;

import org.xiaoyu.micro.annotation.Configuration;
import org.xiaoyu.micro.annotation.Import;
import org.xiaoyu.micro.web.WebMvcConfiguration;

@Configuration
@Import(WebMvcConfiguration.class)
public class ControllerConfiguration {
}
