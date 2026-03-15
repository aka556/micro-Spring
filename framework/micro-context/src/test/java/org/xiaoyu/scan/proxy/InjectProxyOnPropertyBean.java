package org.xiaoyu.scan.proxy;

import org.xiaoyu.micro.annotation.Autowired;
import org.xiaoyu.micro.annotation.Component;

@Component
public class InjectProxyOnPropertyBean {

    @Autowired
    public OriginBean injected;
}
