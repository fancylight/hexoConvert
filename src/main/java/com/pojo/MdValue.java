package com.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <h3>hexoConvert</h3>
 *
 * @author : ck
 * @date : 2020-04-30 18:17
 **/
@Component
public class MdValue {
    @Value("${hexoSrc}")
    private String hexoSrcDir;
    @Value("${hexoDir}")
    private String hexoDir;

    public String getHexoDir() {
        return hexoDir;
    }

    public void setHexoDir(String hexoDir) {
        this.hexoDir = hexoDir;
    }

    public String getHexoSrcDir() {
        return hexoSrcDir;
    }

    public void setHexoSrcDir(String hexoSrcDir) {
        this.hexoSrcDir = hexoSrcDir;
    }
}
