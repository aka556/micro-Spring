package org.xiaoyu.scan.nested;

import org.xiaoyu.micro.annotation.Component;

@Component
public class OuterBean {

    @Component
    public static class NestedBean {
    }
}
