package com.pojo;

import java.util.List;

/**
 * <h3>hexoConvert</h3>
 *
 * @author : ck
 * @date : 2020-05-09 11:34
 **/
public class MdCountJsonPojo {
    private String status;
    private List<String> mdList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getMdList() {
        return mdList;
    }

    public void setMdList(List<String> mdList) {
        this.mdList = mdList;
    }
}
