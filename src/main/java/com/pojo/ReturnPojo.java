package com.pojo;


import java.io.Serializable;

public class ReturnPojo implements Serializable {
    private String desc;

    public ReturnPojo(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
