package com.service;

import com.exception.CommonException;
import com.md.HexoCmdOp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class HexoOPServiceImpl implements HexoOPService {
    @Autowired
    private HexoCmdOp hexoCmdOp;

    @Override
    public List<String> getExistMd() {
        return hexoCmdOp.getExistMd();
    }

    @Override
    public Resource createMd(String name) throws CommonException {
        return hexoCmdOp.createOrGetHexoMd(name);
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
        hexoCmdOp.upDataMd(context, fileName);
    }

    @Override
    public Resource getMd(String mdName) throws CommonException {
        return hexoCmdOp.getMdByName(mdName);
    }

}
