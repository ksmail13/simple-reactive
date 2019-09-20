package io.github.ksmail13.exception;

public class UncaughtErrorException extends RuntimeException {

    public UncaughtErrorException() {
    }

    public UncaughtErrorException(String s) {
        super(s);
    }

    public UncaughtErrorException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public UncaughtErrorException(Throwable throwable) {
        super(throwable);
    }

    public UncaughtErrorException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
