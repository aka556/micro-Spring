package org.xiaoyu.micro.aop.before;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xiaoyu.micro.annotation.Component;
import org.xiaoyu.micro.aop.BeforeInvocationHandlerAdapter;

import java.lang.reflect.Method;

@Component
public class LogInvocationHandler extends BeforeInvocationHandlerAdapter {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void before(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("[Before] {}()", method.getName());
    }
}
