package org.xiaoyu.micro.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {

    /**
     * Bean name. default is empty string which means using the class name as bean name.
     */
    String value() default "";
}
