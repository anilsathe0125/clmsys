package com.college.lm.Exception;

public class UserExitException extends Exception {

    public UserExitException() {
        super();
    }
    public UserExitException(String msg) {
        super(msg);
    }
    public UserExitException(String msg, Throwable cause) {
        super(msg,cause);
    }
}