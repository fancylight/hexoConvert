package com.regex;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h3>hexoConvert</h3>
 *
 * @author : ck
 * @date : 2020-05-07 10:43
 **/
public class RegexReplace {
    private static Log log = LogFactory.getLog(RegexReplace.class);

    public static String replace(String inputStr, String regex, ReplaceHandler replaceHandler) {
        StringBuffer stringBuffer = new StringBuffer();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputStr);
        int findIndex = 0;
        boolean notMather = true;
        log.info("开始替换---" + inputStr);
        log.info("正则---" + regex);
        while (matcher.find(findIndex)) {
            notMather = false;
            int matcherStart = matcher.start();
            int matcherEnd = matcher.end();
            String replacedStr = replaceHandler.test(matcher.group());
            stringBuffer.append(inputStr, findIndex, matcherStart);
            stringBuffer.append(replacedStr);
            findIndex = matcherEnd;
        }
        if (!notMather) {
            log.info("正则替换后行");
            log.info(stringBuffer.toString());
            return stringBuffer.toString();
        } else {
            log.info("未有匹配项目,返回原值");
            return inputStr;
        }
    }
}
