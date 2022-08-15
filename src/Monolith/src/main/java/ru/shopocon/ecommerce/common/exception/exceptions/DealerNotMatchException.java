package ru.shopocon.ecommerce.common.exception.exceptions;

public class DealerNotMatchException extends RuntimeException {

    public DealerNotMatchException(String msg) {
        super(msg);
    }

    public DealerNotMatchException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
