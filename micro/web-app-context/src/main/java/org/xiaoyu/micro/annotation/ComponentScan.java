package org.xiaoyu.micro.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ComponentScan {

    /**
     * Package name to scan for components.
     */
    String[] value() default {};
}
