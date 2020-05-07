package com.service;

import com.exception.CommonException;
import org.apache.commons.io.IOUtils;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <h3>hexoConvert</h3>
 *
 * @author : ck
 * @date : 2020-05-06 14:42
 **/
@Service
public class PicUpServiceImpl implements PicUpService {
    private Log log = LogFactory.getLog(PicUpServiceImpl.class);
    @Value("${pic.dir}")
    private String picDir;
    @Value("${server.port}")
    private String port;
    @Value("${server.local.host}")
    private String host;

    @Override
    public String upLoad(byte[] pic, String fileName) throws CommonException {
        log.info("开始写入图片" + picDir + File.separator + fileName);
        try {
            OutputStream outputStream = new FileOutputStream(picDir + File.separator + fileName);
            IOUtils.write(pic, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new CommonException(e.getMessage());
        }
        //todo:提交后记得处理此处
        String picAd = "/picUp/" + fileName;
        log.info("图片地址" + picAd);
        return picAd;
    }
}
