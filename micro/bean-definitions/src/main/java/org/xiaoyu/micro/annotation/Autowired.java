package org.xiaoyu.micro.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

    /**
     * Is required or not.
     */
    boolean value() default true;

    /**
     * Bean name if set.
     */
    String name() default "";
}
