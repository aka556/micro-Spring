package org.xiaoyu.micro.jdbc.tx;

import org.xiaoyu.micro.annotation.Transactional;
import org.xiaoyu.micro.aop.AnnotationProxyBeanPostProcessor;

public class TransactionalBeanPostProcessor extends AnnotationProxyBeanPostProcessor<Transactional> {
}
