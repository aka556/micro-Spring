package org.xiaoyu.scan.destroy;

import jakarta.annotation.PreDestroy;
import org.xiaoyu.micro.annotation.Component;
import org.xiaoyu.micro.annotation.Value;

@Component
public class AnnotationDestroyBean {

    @Value("${app.title}")
    public String appTitle;

    @PreDestroy
    void destroy() {
        this.appTitle = null;
    }
}
