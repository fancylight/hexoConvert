package com.md;

import com.exception.CommonException;
import com.pojo.MdValue;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Stream;

@Component
public class HexoCmdOp {
    @Autowired
    private MdValue mdValue;
    private ConcurrentMap<String, Boolean> mdMap = new ConcurrentHashMap();
    private ConcurrentSkipListSet mdSet = new ConcurrentSkipListSet();
    private Object object = new Object();

    /**
     * 创建新的md文件或获取已经存在的md
     *
     * @param name
     */
    public Resource createOrGetHexoMd(String name) throws IOException {
        String hexoBlogDir = mdValue.getHexoDir();
        String hexoMdDir = mdValue.getHexoSrcDir();
        //初始化
        synchronized (object) {
            System.out.println("init-------------");
            if (mdSet.isEmpty()) {

                Stream.of(new File(hexoBlogDir).listFiles(pathname -> !pathname.isDirectory())).forEach(file -> {
                    mdSet.add(file.getAbsolutePath());
                    mdMap.put(file.getAbsolutePath(), false);
                });
            }
            System.out.println("init end-----------");
        }
        //尝试获取文档
        String mdName = hexoMdDir + File.separator + name;
        if (!mdSet.contains(mdName)) { //创建新文件
            System.out.println("shutdown hexo");
            String shutDownCmd = "sh " + mdValue.getHexoDir() + File.separator + "sh" + File.separator + "killProcess.sh hexo";
            shExec(shutDownCmd);
            System.out.println("shutdown hexo end");
            System.out.println("create md file------------");
            String cmd = "sh " + hexoBlogDir + File.separator + "sh" + File.separator + "hexoNew.sh " + name;
            System.out.println(cmd);
            shExec(cmd);
            mdSet.add(mdName);
            mdMap.put(mdName, true);
            System.out.println("create md file end------------");
        }
        //返回md文件
        System.out.println("get md file-----------" + mdName);
        return new FileSystemResource(mdName + ".md");
    }

    /**
     * 提交文档
     * <p>
     * 方案1:
     * - 覆盖_post中文档
     * - 执行hexo clean
     * - 执行hexo g
     * - 执行hexo s
     * <p>
     * 方案2:
     * 使用gitlab ci,让gitlab来处理
     *
     * @param context
     * @param fileName
     */
    public void upDataMd(String context, String fileName) throws CommonException {
        // 方案1
        //[1] 处理context中的链接 即将 [](xxx/picTemp/xxx.png) 改为{%asset_img xxx xxx%}
        context = replaceToHexoPicLink(context);
        //[2] 替换hexo目录下文件
        try {
            IOUtils.write(context, new FileSystemResource(mdValue.getHexoSrcDir() + File.separator + fileName + ".md").getOutputStream());
            System.out.println("replace mdFile" + fileName);
        } catch (IOException e) {
            throw new CommonException(e.getMessage());
        }
        //[3] 执行脚本 hexo g hexo s
        System.out.println("shutdown hexo");
        String shutDownCmd = "sh " + mdValue.getHexoDir() + File.separator + "sh" + File.separator + "killProcess.sh hexo";
        shExec(shutDownCmd);
        System.out.println("shutdown hexo end");
        String cmd = "sh " + mdValue.getHexoDir() + File.separator + "sh" + File.separator + "hexoPut.sh ";
        shExec(cmd);
    }

    private String replaceToHexoPicLink(String context) {
        //todo:替换图片链接,首先先实现tempPic功能
        return context;
    }

    private void shExec(String sh) {
        try {
            Process process = Runtime.getRuntime().exec(sh);
            System.out.println("process.waitFor:" + process.waitFor());
            if (process.waitFor() == 0) {// 0 表示线程正常终止。
                System.out.println("线程正常");
            }
            System.out.println(new String(IOUtils.toByteArray(process.getInputStream()), "UTF-8"));
            System.out.println(new String(IOUtils.toByteArray(process.getErrorStream()), "UTF-8"));
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
