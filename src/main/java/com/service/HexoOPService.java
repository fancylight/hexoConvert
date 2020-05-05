package com.service;

import com.exception.CommonException;
import org.springframework.core.io.Resource;

/**
 * 定义关于hexo操作的接口
 */
public interface HexoOPService {

    Resource getOrCreateMd(String name) throws CommonException;

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

    void upDataMd(String context, String fileName) throws CommonException;
}
