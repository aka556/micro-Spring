package org.xiaoyu.micro.aop.around;

import org.xiaoyu.micro.annotation.Autowired;
import org.xiaoyu.micro.annotation.Component;
import org.xiaoyu.micro.annotation.Order;

@Order(0)
@Component
public class OtherBean {

    public OriginBean origin;

    public OtherBean(@Autowired OriginBean origin) {
        this.origin = origin;
    }
}
