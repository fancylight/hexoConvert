package com.controller;

import com.exception.CommonException;
import com.pojo.MdCommitPojo;
import com.pojo.MdCountJsonPojo;
import com.pojo.PicJson;
import com.pojo.ReturnPojo;
import com.service.HexoOPService;
import com.service.PicUpService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Autowired
    private PicUpService picUpService;

    @Value("${pic.dir}")
    private String picDir;

    /**
     * 获取文章
     *
     * @param fileName
     * @return
     * @throws CommonException
     */
    @PostMapping(value = "createMd", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public Resource createMd(@RequestParam(value = "mdName") String fileName, HttpServletResponse response) throws CommonException {
        if (fileName == null || fileName.equals(""))
            throw new CommonException("fileName不应为空");
        Resource resource = hexoOPService.createMd(fileName);
        if (resource.isReadable()) {
            response.addCookie(new Cookie("mdCurrent", fileName));
        }
        return resource;
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
    public PicJson upLoadPic(@RequestPart("editormd-image-file") MultipartFile file, @CookieValue String mdCurrent) throws
            IOException, CommonException {
        log.info("上传图片");
        //创建文件夹
        File file1 = new File(picDir);
        if (!file1.exists())
            file1.mkdirs();
        return new PicJson(1, "nm$l", picUpService.upLoad(file.getBytes(), mdCurrent + "__" + file.getOriginalFilename()));
    }

    @PostMapping(value = "postMd", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ReturnPojo putMd(@Valid @RequestBody MdCommitPojo mdCommitPojo) throws CommonException {
        hexoOPService.upDataMd(mdCommitPojo.getContext(), mdCommitPojo.getFileName());
        return new ReturnPojo("success");
    }

    @RequestMapping(value = "picUp/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public Resource getPic(@PathVariable String fileName) {
        log.info("获取本机图片---" + picDir + File.separator + fileName);
        return new FileSystemResource(picDir + File.separator + fileName);
    }

    /**
     * 获取当前存在的文档名
     *
     * @return
     */
    @PostMapping(value = "getListMd", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MdCountJsonPojo getListMd() {
//        List<String> mds = hexoOPService.getExistMd();
        List<String> mds = Stream.of("123","234234234").collect(Collectors.toList());
        MdCountJsonPojo mdCountJsonPojo = new MdCountJsonPojo();
        if (mds.size() > 0) {
            mdCountJsonPojo.setStatus("正常");
        } else {
            mdCountJsonPojo.setStatus("hexo未存在文档");
        }
        mdCountJsonPojo.setMdList(mds);
        return mdCountJsonPojo;
    }

    /**
     * 通过md文档名获取文档
     *
     * @param mdName
     * @return
     * @throws CommonException
     */
    @PostMapping(value = "getMd", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public Resource getMd(@RequestParam("mdName") String mdName) throws CommonException {
        return hexoOPService.getMd(mdName);
    }
}
