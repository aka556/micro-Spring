package org.xiaoyu.scan.proxy;

import org.xiaoyu.micro.annotation.Autowired;
import org.xiaoyu.micro.annotation.Component;

@Component
public class InjectProxyOnConstructorBean {

    public final OriginBean injected;

    public InjectProxyOnConstructorBean(@Autowired OriginBean injected) {
        this.injected = injected;
    }
}
