package com.service;

import com.exception.CommonException;
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
    public Resource getOrCreateMd(String name) throws CommonException {
        try {
            return hexoCmdOp.createOrGetHexoMd(name);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonException("未能正确创建");
        }
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
    public void upDataMd(String context, String fileName) throws CommonException {
        hexoCmdOp.upDataMd(context,fileName);
    }
}
