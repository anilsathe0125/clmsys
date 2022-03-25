package com.college.lm.Exception;

public class CustomExceotion extends Exception {
    public CustomExceotion() {
        super();
    }
    public CustomExceotion(String msg) {
        super(msg);
    }
    public CustomExceotion(String msg, Throwable cause) {
        super(msg,cause);
    }
}
