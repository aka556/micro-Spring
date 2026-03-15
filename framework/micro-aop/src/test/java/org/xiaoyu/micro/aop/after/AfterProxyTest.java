package org.xiaoyu.micro.aop.after;

import org.junit.jupiter.api.Test;
import org.xiaoyu.micro.context.AnnotationConfigApplicationContext;
import org.xiaoyu.micro.io.PropertyResolver;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AfterProxyTest {

    @Test
    public void afterProxyTest() {
        try (var ctx = new AnnotationConfigApplicationContext(AfterApplication.class, createPropertyResolver())) {
            GreetingBean proxy = ctx.getBean(GreetingBean.class);

            // Should print: Hello, xiaoyu!
            assertEquals("Hello, Bob!", proxy.hello("Bob"));
            assertEquals("Morning, Alice!", proxy.morning("Alice"));
        }
    }

    PropertyResolver createPropertyResolver() {
        var ps = new Properties();
        var pr = new PropertyResolver(ps);
        return pr;
    }
}
