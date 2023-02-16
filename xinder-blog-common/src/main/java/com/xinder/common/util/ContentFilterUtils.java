package com.xinder.common.util;

/**
 * 文本过滤工具类
 * @author Xinder
 * @date 2023-02-15 21:20
 */
public class ContentFilterUtils {

    /**
     * 文本过滤
     *
     * @param content
     * @return
     */
    public static String filter(String content) {
        SensitiveWordsUtils filter = SensitiveWordsUtils.getInstance();
        return filter.replaceSensitiveWord(content, SensitiveWordsUtils.minMatchType, "*");
    }
}
