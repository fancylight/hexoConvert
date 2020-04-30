package com.service;

import com.exception.CommonExeption;
import com.md.HexoCmdEnum;
import org.springframework.core.io.Resource;

/**
 * 定义关于hexo操作的接口
 */
public interface HexoOPService {
    Resource getMdFileByName(String name) throws CommonExeption;

    boolean createMd(String name) throws CommonExeption;

    /**
     * 推送hexo源文件到指定的gitlab 仓库
     *
     * @return
     */
    boolean pushMdPost();

    /**
     * 拉取
     *
     * @return
     */
    boolean pullMdPost();

    void upDataMd(String context, String fileName);
}
