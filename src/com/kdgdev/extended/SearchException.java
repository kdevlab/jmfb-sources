package com.kdgdev.extended;

/**
 * Created with IntelliJ IDEA.
 * User: kirill
 * Date: 29.12.12
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */
public class SearchException extends Exception {
    public SearchException(Throwable cause) {
        super(cause);
    }

    public SearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchException(String message) {
        super(message);
    }

    public SearchException() {
    }
}
