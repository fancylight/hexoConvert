package com.controller;

import com.md.CmdUtil;
import com.pojo.MdValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <h3>hexoConvert</h3>
 *
 * @author : ck
 * @date : 2020-04-30 18:12
 **/
@Controller
public class MdController {
    @Autowired
    private MdValue mdValue;
    public Resource getMd(@RequestParam("mdName") String fileName, @RequestParam("isNew") boolean isNew) {
        if (isNew) {
            CmdUtil.getTargetFile(fileName,isNew,mdValue);
        } else {

        }
        return null;
    }
}
