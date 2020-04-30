package com.exception;

public class CommonExeption extends Exception {
    private String desc;

    public CommonExeption(String desc) {
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
