package org.xiaoyu.scan.custom.annotation;

import org.xiaoyu.micro.annotation.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface CustomAnnotation {

    String value() default "";
}
