package org.xiaoyu.micro.aop.metric;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Metric {

    /**
     * metric name.
     */
    String value();
}
