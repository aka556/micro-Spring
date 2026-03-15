package org.xiaoyu.micro.exception;

public class ErrorResponseException extends NestedRuntimeException {

    public final int statusCode;

    public ErrorResponseException(int statusCode) {
        this.statusCode = statusCode;
    }

    public ErrorResponseException(int statusCode, final String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public ErrorResponseException(int statusCode, final String message, final Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public ErrorResponseException(int statusCode, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
    }
}
