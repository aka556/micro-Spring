package org.xiaoyu.micro.exception;

public class TransactionException extends DataAccessException {

    public TransactionException() {}

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }
}
