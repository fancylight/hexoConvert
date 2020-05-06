package com.controller;

import com.exception.CommonException;
import com.pojo.MdCommitPojo;
import com.pojo.PicJson;
import com.pojo.ReturnPojo;
import com.service.HexoOPService;
import com.service.PicUpService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

/**
 * <h3>hexoConvert</h3>
 *
 * @author : ck
 * @date : 2020-04-30 18:12
 **/
@Controller
public class MdController {
    Log log = LogFactory.getLog(MdController.class);
    @Autowired
    private HexoOPService hexoOPService;
    @Autowired(required = false)
    private PicUpService picUpService;

    /**
     * 获取文章
     *
     * @param fileName
     * @return
     * @throws CommonException
     */
    @PostMapping(value = "getMd", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public Resource getMd(@RequestParam(value = "mdName") String fileName) throws CommonException {
        return hexoOPService.getOrCreateMd(fileName);
    }

    /**
     * 上传图片
     * <p>
     * 将上传的图片放置到临时目录,并返回临时url
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping(value = "picUpload", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PicJson upLoadPic(@RequestPart("editormd-image-file") MultipartFile file) throws IOException {
        log.info("上传图片");
        return new PicJson(1, "nm$l", picUpService.upLoad(file.getBytes(), file.getName()));
    }

    @PostMapping(value = "postMd", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ReturnPojo putMd(@Valid @RequestBody MdCommitPojo mdCommitPojo) throws CommonException {
        hexoOPService.upDataMd(mdCommitPojo.getContext(), mdCommitPojo.getFileName());
        return new ReturnPojo("success");
    }
}
