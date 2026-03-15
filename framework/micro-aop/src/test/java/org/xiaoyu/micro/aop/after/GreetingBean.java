package org.xiaoyu.micro.aop.after;

import org.xiaoyu.micro.annotation.Component;
import org.xiaoyu.micro.anntation.Around;

@Component
@Around("politeInvocationHandler")
public class GreetingBean {

    public String hello(String name) {
        return "Hello, " + name + ".";
    }

    public String morning(String name) {
        return "Good morning, " + name + ".";
    }
}
