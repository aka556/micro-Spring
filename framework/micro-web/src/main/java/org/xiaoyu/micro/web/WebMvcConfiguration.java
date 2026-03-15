package org.xiaoyu.micro.web;

import jakarta.servlet.ServletContext;
import org.xiaoyu.micro.annotation.Autowired;
import org.xiaoyu.micro.annotation.Bean;
import org.xiaoyu.micro.annotation.Configuration;
import org.xiaoyu.micro.annotation.Value;

import java.util.Objects;

@Configuration
public class WebMvcConfiguration {

    private static ServletContext servletContext;

    /**
     * set by web listener.
     */
    static void setServletContext(ServletContext ctx) {
        servletContext = ctx;
    }

    @Bean(initMethod = "init")
    ViewResolver viewResolver(
                               @Autowired ServletContext servletContext,
                               @Value("${summer.web.freemarker.template-path:/WEB-INF/templates}") String templatePath,
                               @Value("${summer.web.freemarker.template-encoding:UTF-8}") String templateEncoding) {
        return new FreeMarkerViewResolver(servletContext, templatePath, templateEncoding);
    }

    @Bean
    ServletContext servletContext() {
        return Objects.requireNonNull(servletContext, "ServletContext is not set.");
    }
}
