package com.pojo;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class MdCommitPojo implements Serializable {
    @NotNull
    private String context;
    @NotNull
    private String fileName;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
