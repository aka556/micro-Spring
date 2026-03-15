package org.xiaoyu.micro.web.utils;

import org.xiaoyu.micro.annotation.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface CustomComponent {

    String value() default "";
}
