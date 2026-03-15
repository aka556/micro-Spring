package org.xiaoyu.micro.exception;

public class BeansException extends NestedRuntimeException {
    public BeansException() {}

    public BeansException(String massage) {
        super(massage);
    }

    public BeansException(String massage, Throwable cause) {
        super(massage, cause);
    }

    public BeansException(Throwable cause) {
        super(cause);
    }
}
