package com.service;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <h3>hexoConvert</h3>
 *
 * @author : ck
 * @date : 2020-05-06 14:42
 **/
@Service
public class PicUpServiceImpl implements PicUpService {
    @Value("${pic.dir}")
    private String picDir;

    @Override
    public String upLoad(byte[] pic, String fileName) {
        try {
            IOUtils.write(pic, new FileOutputStream(picDir + File.separator + fileName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
