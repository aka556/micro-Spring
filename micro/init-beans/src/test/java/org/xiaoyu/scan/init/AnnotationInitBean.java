package org.xiaoyu.scan.init;

import jakarta.annotation.PostConstruct;
import org.xiaoyu.micro.annotation.Component;
import org.xiaoyu.micro.annotation.Value;

@Component
public class AnnotationInitBean {

    @Value("${app.title}")
    String appTitle;

    @Value("${app.version}")
    String appVersion;

    public String appName;

    @PostConstruct
    public void init() {
        this.appName = this.appTitle + " / " + this.appVersion;
    }
}
