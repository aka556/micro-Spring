package org.xiaoyu.micro.aop.before;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xiaoyu.micro.annotation.Component;
import org.xiaoyu.micro.anntation.Around;

@Component
@Around("logInvocationHandler")
public class BusinessBean {

    final Logger logger = LoggerFactory.getLogger(getClass());

    public String hello(String name) {
        logger.info("Hello, {}.", name);
        return "Hello, " + name + ".";
    }

    public String morning(String name) {
        logger.info("Morning, {}.", name);
        return "Morning, " + name + ".";
    }
}
