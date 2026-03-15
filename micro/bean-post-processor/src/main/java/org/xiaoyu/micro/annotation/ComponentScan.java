package org.xiaoyu.micro.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ComponentScan {

    /**
     * Package name to scan for components.
     */
    String[] value() default {};
}
