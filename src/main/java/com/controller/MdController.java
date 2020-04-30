package com.controller;

import com.exception.CommonExeption;
import com.pojo.MdValue;
import com.pojo.PicJson;
import com.pojo.ReturnPojo;
import com.service.HexoOPService;
import com.service.PicUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <h3>hexoConvert</h3>
 *
 * @author : ck
 * @date : 2020-04-30 18:12
 **/
@Controller
public class MdController {
    @Autowired
    private HexoOPService hexoOPService;
    @Autowired(required = false)
    private PicUpService picUpService;

    @GetMapping("getMd")
    public Resource getMd(@RequestParam(value = "mdName") String fileName, @RequestParam("isNew") boolean isNew) throws CommonExeption {
        if (isNew)
            hexoOPService.createMd(fileName);
        return hexoOPService.getMdFileByName(fileName);
    }

    @PostMapping(value = "picUpload", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PicJson upLoadPic(@RequestPart("editormd-image-file") MultipartFile file) throws IOException {
        return new PicJson(1, "nm$l", picUpService.upLoad(file.getBytes(), file.getName()));
    }

    @PostMapping("postMd")
    @ResponseBody
    public ReturnPojo putMd(@RequestParam String context, @RequestParam String fileName) {
        hexoOPService.upDataMd(context,fileName);
        return new ReturnPojo("success");
    }
}
