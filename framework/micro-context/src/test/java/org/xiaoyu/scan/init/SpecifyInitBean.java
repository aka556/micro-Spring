package org.xiaoyu.scan.init;

public class SpecifyInitBean {

    String appTitle;

    String appVersion;

    public String appName;

    public SpecifyInitBean(String appTitle, String appVersion) {
        this.appTitle = appTitle;
        this.appVersion = appVersion;
    }

    void init() {
        this.appName = this.appTitle + " / " + this.appVersion;
    }
}
