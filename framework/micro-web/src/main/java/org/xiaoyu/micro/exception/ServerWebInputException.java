package org.xiaoyu.micro.exception;

public class ServerWebInputException extends ErrorResponseException {

    public ServerWebInputException() {
        super(400);
    }

    public ServerWebInputException(String message) {
        super(400, message);
    }

    public ServerWebInputException(String message, Throwable cause) {
        super(400, message, cause);
    }

    public ServerWebInputException(Throwable cause) {
        super(400, cause);
    }
}
