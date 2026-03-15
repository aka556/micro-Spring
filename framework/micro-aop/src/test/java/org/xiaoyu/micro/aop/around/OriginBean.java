package org.xiaoyu.micro.aop.around;

import org.xiaoyu.micro.annotation.Component;
import org.xiaoyu.micro.annotation.Value;
import org.xiaoyu.micro.anntation.Around;

@Component
@Around("aroundInvocationHandler")
public class OriginBean {

    @Value("${customer.name}")
    public String name;

    @Polite
    public String hello() {
        return "Hello, " + name + ".";
    }

    public String morning() {
        return "Morning, " + name + ".";
    }
}
