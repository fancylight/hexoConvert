package com.md;

import com.exception.CommonException;
import com.pojo.MdValue;
import com.regex.RegexReplace;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Stream;

@Component
public class HexoCmdOp {
    @Autowired
    private MdValue mdValue;
    private ConcurrentSkipListSet mdSet = new ConcurrentSkipListSet();
    private Object object = new Object();
    @Value("${pic.dir}")
    private String picUPLoadDir;
    private Log log = LogFactory.getLog(HexoCmdOp.class);

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
            log.info("init-------------");
            if (mdSet.isEmpty()) {

                Stream.of(new File(hexoBlogDir).listFiles(pathname -> !pathname.isDirectory())).forEach(file -> {
                    mdSet.add(file.getAbsolutePath());
                });
            }
            log.info("init end-----------");
        }
        //尝试获取文档
        String mdName = hexoMdDir + File.separator + name;
        if (!mdSet.contains(mdName)) {
            synchronized (object) {
                if (!mdSet.contains(mdName)) { //创建新文件
                    log.info("shutdown hexo");
                    String shutDownCmd = "sh " + mdValue.getHexoDir() + File.separator + "sh" + File.separator + "killProcess.sh hexo";
                    shExec(shutDownCmd);
                    log.info("shutdown hexo end");
                    log.info("create md file------------");
                    String cmd = "sh " + hexoBlogDir + File.separator + "sh" + File.separator + "hexoNew.sh " + name;
                    log.info(cmd);
                    shExec(cmd);
                    mdSet.add(mdName);
                    log.info("create md file end------------");
                }
            }
        }
        //返回md文件
        log.info("get md file-----------" + mdName);
        return replaceHexoPicLinkToLocal(new FileSystemResource(mdName + ".md"), name);
    }

    /**
     * 将hexo链接替换成本地链接
     * {%asset_img xx.xx xx%}
     *
     * @param fileSystemResource
     * @param mdName
     * @return
     */
    private Resource replaceHexoPicLinkToLocal(FileSystemResource fileSystemResource, String mdName) throws IOException {
        String regex = "\\{%asset_img .*?%\\}";
        //{%asset_img 无标题.png%}
        log.info("开始替换hexo传递到编辑器中的图片链接");
        StringBuilder stringBuilder0 = new StringBuilder();
        for (LineIterator it = IOUtils.lineIterator(fileSystemResource.getInputStream(), "UTF-8"); it.hasNext(); ) {
            String line = it.next();
            line = RegexReplace.replace(line, regex, (str) -> {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("![");
                String[] picInfos = str.split(" ");
                String picName = mdName + "__" + picInfos[1];
                String desc = "";               //{%asset_img 无标题.png 测试 %}
                if (picInfos.length > 3) { //说明存在title部分 {%asset_img 无标题.png 俄式 %}
                    desc = "\"" + picInfos[2] + "\"";
                }
                //{%asset_img 无标题.png %}
                stringBuilder.append("]").append("(/picUp/").append(picName).append(" " + desc).append(")");
                return stringBuilder.toString();
            });
            stringBuilder0.append(line).append(IOUtils.LINE_SEPARATOR);
        }
        log.info("替换hexo传递到编辑器中的图片链接结束");
        return new ByteArrayResource(stringBuilder0.toString().getBytes("UTF-8"), "UTF-8");
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
        //[1] 处理context中的链接
        log.info("开始替换本地提交的md图片链接---替换前");
        log.info(context);
        context = replaceToHexoPicLink(context);
        log.info("替换后");
        log.info(context);
        //[2] 替换hexo目录下文件
        try {
            String md = mdValue.getHexoSrcDir() + File.separator + fileName + ".md";
            log.info("上传编辑器md到hexo目录  " + md);
            File file = new File(md);
            if (file.exists()) {
                log.info("删除hexo md" + md + "结果:" + file.delete());
            }
            IOUtils.write(context, new FileSystemResource(md).getOutputStream());
            log.info("上传编辑器md到hexo目录结束");
        } catch (IOException e) {
            throw new CommonException(e.getMessage());
        }
        //[3] 执行脚本 hexo g hexo s
        log.info("shutdown hexo");
        String shutDownCmd = "sh " + mdValue.getHexoDir() + File.separator + "sh" + File.separator + "killProcess.sh hexo";
        shExec(shutDownCmd);
        log.info("shutdown hexo end");
        String cmd = "sh " + mdValue.getHexoDir() + File.separator + "sh" + File.separator + "hexoPut.sh ";
        shExec(cmd);
    }

    /**
     * 替换前台md编辑器中的图片链接格式
     * <p>
     * 将 ![](/picUp/xxx.xx)  hexo {%asset img.xx desc%}
     *
     * @param context
     * @return 转换之后的内容
     */
    private String replaceToHexoPicLink(String context) {
        StringBuilder StringBuilder0 = new StringBuilder();
        //[1] 按行读取
        String picRex = "!\\[.*?\\]\\(/picUp/.*?\\)"; //按行匹配的正则
        for (LineIterator it = IOUtils.lineIterator(new StringReader(context)); it.hasNext(); ) {
            String line = it.next();
            line = RegexReplace.replace(line, picRex, (str) -> {
                //[2] 将 ![](/picUp/xxx.xx xx)  hexo {%asset img.xx desc%}
                //![](/picUp/测试___无标题.png)
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("{%asset_img ");
                int leftSb = str.indexOf("[");
                int rightSb = str.indexOf("]");

                // 描述
                String desc = "";
                if (leftSb + 1 != rightSb) {
                    desc = str.substring(leftSb + 1, rightSb);
                }
                // 图片位置
                int lastSlash = str.lastIndexOf("/");
                int endIndex = str.length() - 1;
                String lastStr = str.substring(lastSlash + 1);
                String pic = str.substring(lastSlash + 1, endIndex); //![](/picUp/xxx.xx)
                if (lastStr.contains(" ")) {
                    pic = lastStr.split(" ")[0];//![](/picUp/xxx.xx xx)
                }
                stringBuilder.append(pic.split("__")[1] + " ");
                stringBuilder.append(desc);
                stringBuilder.append(" %}");
                //[3] 替换hexo中对应文档的图片
                String mdNameDir = pic.split("__")[0];
                String mdName = pic.split("__")[1];
                String hexoPicDir = mdValue.getHexoSrcDir() + File.separator + mdNameDir + File.separator + mdName;
                Resource resource = new FileSystemResource(picUPLoadDir + File.separator + pic);
                try {
                    log.info("拷贝本地图片到hexo目录:" + resource.getFile().getAbsolutePath() + "----" + hexoPicDir);
                    IOUtils.copy(resource.getInputStream(), new FileOutputStream(hexoPicDir));
                    log.info("拷贝结束");
                } catch (IOException e) {
                    log.warn(e.getMessage());
                }

                return stringBuilder.toString();
            });
            StringBuilder0.append(line).append(IOUtils.LINE_SEPARATOR);
        }
        return StringBuilder0.toString();
    }

    private void shExec(String sh) {
        try {
            Process process = Runtime.getRuntime().exec(sh);
            log.info("process.waitFor:" + process.waitFor());
            if (process.waitFor() == 0) {// 0 表示线程正常终止。
                log.info("线程正常");
            }
            log.info(new String(IOUtils.toByteArray(process.getInputStream()), "UTF-8"));
            log.info(new String(IOUtils.toByteArray(process.getErrorStream()), "UTF-8"));
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
