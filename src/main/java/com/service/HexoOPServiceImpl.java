package com.service;

import com.exception.CommonExeption;
import com.md.HexoCmdEnum;
import com.md.HexoCmdOp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HexoOPServiceImpl implements HexoOPService {
    @Autowired
    private HexoCmdOp hexoCmdOp;

    @Override
    public Resource getMdFileByName(String name) throws CommonExeption {
        Resource resource = hexoCmdOp.getHexoFileByName(name);
        if (resource == null)
            throw new CommonExeption(name + "不存在请创建");
        return resource;
    }

    @Override
    public boolean createMd(String name) throws CommonExeption {
        try {
            hexoCmdOp.createHexoMd(name);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonExeption("未能正确创建");
        }
        return true;
    }

    @Override
    public boolean pushMdPost() {
        return false;
    }

    @Override
    public boolean pullMdPost() {
        return false;
    }

    @Override
    public void upDataMd(String context, String fileName) {

    }
}
