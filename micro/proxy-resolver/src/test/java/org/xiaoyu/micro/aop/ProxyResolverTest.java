package org.xiaoyu.micro.aop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ProxyResolverTest {

    @Test
    public void testProxyResolver() {
        OriginBean origin = new OriginBean();
        origin.name = "xiaoyu";

        assertEquals("hello, xiaoyu.", origin.hello());

        // 创建代理对象
        OriginBean proxy = new ProxyResolver().createProxy(origin, new PoliteInvocationHandler());

        System.out.println(proxy.getClass().getName());

        // 代理类不是BeanOrigin
        assertNotSame(OriginBean.class, proxy.getClass());
        // 代理对象的name属性为空
        assertNull(proxy.name);

        // 带有@Polite注解的方法返回值被修改
        assertEquals("hello, xiaoyu!", proxy.hello());
        // 没有带有@Polite注解的方法返回值没有被修改
        assertEquals("morning, xiaoyu.", proxy.morning());
    }
}
