package org.xiaoyu.micro.aop;

public class OriginBean {

    public String name;

    @Polite
    public String hello() {
        return "hello, " + name + ".";
    }

    public String morning() {
        return "morning, " + name + ".";
    }
}
