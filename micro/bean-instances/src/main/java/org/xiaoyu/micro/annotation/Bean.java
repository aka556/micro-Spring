package org.xiaoyu.micro.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

    /**
     * The name of the bean. If left blank, the name will be the name of the method.
     */
    String value() default "";

    String initMethod() default "";

    String destroyMethod() default "";
}
