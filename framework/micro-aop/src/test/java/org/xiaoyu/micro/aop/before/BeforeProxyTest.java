package org.xiaoyu.micro.aop.before;

import org.junit.jupiter.api.Test;
import org.xiaoyu.micro.context.AnnotationConfigApplicationContext;
import org.xiaoyu.micro.io.PropertyResolver;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeforeProxyTest {

    @Test
    public void testBeforeProxy() {
        try (var ctx = new AnnotationConfigApplicationContext(BeforeApplication.class, createPropertyResolver())){
            BusinessBean proxy = ctx.getBean(BusinessBean.class);

            assertEquals("hello, xiaoyu.", proxy.hello("xiaoyu"));
            assertEquals("morning, yuxie.", proxy.morning("yuxie"));
        }
    }

    PropertyResolver createPropertyResolver() {
        var ps = new Properties();
        var pr = new PropertyResolver(ps);
        return pr;
    }
}
