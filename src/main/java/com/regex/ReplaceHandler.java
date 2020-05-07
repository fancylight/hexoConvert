package com.regex;


@FunctionalInterface
public interface ReplaceHandler {
    /**
     * @param matchLine 匹配的数据
     * @return 替换后的结果
     */
    String test(String matchLine);
}
