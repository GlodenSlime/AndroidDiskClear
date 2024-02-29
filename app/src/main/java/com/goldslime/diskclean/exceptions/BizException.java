package com.goldslime.diskclean.exceptions;

public class BizException extends Exception {
    public String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public BizException(String error) {
        this.error = error;
    }
}
