package com.exception;

public class CommonException extends Exception {
    private String desc;

    public CommonException(String desc) {
        super();
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
