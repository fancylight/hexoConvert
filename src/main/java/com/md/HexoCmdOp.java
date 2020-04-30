package com.md;

import com.pojo.MdValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class HexoCmdOp {
    @Autowired
    private MdValue mdValue;

    /**
     * 创建新的md文件
     *
     * @param name
     */
    public void createHexoMd(String name) throws IOException {
        String hexoBlogDir = mdValue.getHexoDir();
        String cmd = "cd " + hexoBlogDir + " " + "hexo new " + name;
        Runtime.getRuntime().exec(cmd);
    }

    /**
     * 通过文件名获取资源
     *
     * @param name
     */
    public Resource getHexoFileByName(String name) {
        String hexoSrcDir = mdValue.getHexoSrcDir();
        String file = hexoSrcDir + File.separator + name + ".md";
        return new PathMatchingResourcePatternResolver().getResource(file);
    }
}
