package org.xiaoyu.micro.aop.metric;

import org.xiaoyu.micro.annotation.Component;
import org.xiaoyu.micro.aop.AnnotationProxyBeanPostProcessor;

@Component
public class MetricProxyBeanPostProcessor extends AnnotationProxyBeanPostProcessor<Metric> {
}
