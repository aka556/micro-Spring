package org.xiaoyu.micro.exception;

public class AopConfigException extends NestedRuntimeException {
    public AopConfigException() {
        super();
    }

    public AopConfigException(Throwable cause) {
        super(cause);
    }

    public AopConfigException(String message) {
        super(message);
    }

    public AopConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
