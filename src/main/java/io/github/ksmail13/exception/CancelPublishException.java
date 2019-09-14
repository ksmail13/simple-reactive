package io.github.ksmail13.exception;

public class CancelPublishException extends RuntimeException {
    public CancelPublishException() {
        super();
    }

    public CancelPublishException(String s) {
        super(s);
    }

    public CancelPublishException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public CancelPublishException(Throwable throwable) {
        super(throwable);
    }

    protected CancelPublishException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
