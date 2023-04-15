package com.xinder.common.util;

import java.util.Set;

/**
 * 文本过滤工具类
 * @author Xinder
 * @date 2023-02-15 21:20
 */
public class ContentFilterUtils {

    private static int matchType = SensitiveWordsUtils.minMatchType;

    /**
     * 文本过滤
     *
     * @param content
     * @return
     */
    public static String filter(String content) {
        SensitiveWordsUtils filter = SensitiveWordsUtils.getInstance();
        return filter.replaceSensitiveWord(content, matchType, "*");
    }

    /**
     * 获取文本中包含的关键词
     *
     * @param content
     * @return
     */
    public static Set<String> getSensitiveWord(String content) {
        SensitiveWordsUtils filter = SensitiveWordsUtils.getInstance();
        return filter.getSensitiveWord(content, matchType);
    }
}
