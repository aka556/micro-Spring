package org.xiaoyu.micro.exception;

public class ServerErrorException extends ErrorResponseException {


    public ServerErrorException() {
        super(500);
    }

    public ServerErrorException(String message) {
        super(500, message);
    }

    public ServerErrorException(String message, Throwable cause) {
        super(500, message, cause);
    }

    public ServerErrorException(Throwable cause) {
        super(500, cause);
    }
}
