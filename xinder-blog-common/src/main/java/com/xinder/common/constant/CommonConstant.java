package com.xinder.common.constant;

/**
 * @author Xinder
 * @date 2023-02-02 16:39
 */
public class CommonConstant {

    // 请求头中Cookie中的Token
    public static final String HTTP_HEADER_TOKEN = "Authorization";

    // 请求头中Cookie中的浏览历史记录
    public static final String HTTP_HEADER_HISTORY = "history";

    //================= Token中的信息 ====================
    public static final String TOKEN_USERNAME = "user_name";
    public static final String TOKEN_AUTHORITIES = "authorities";

    //================= redis中的一些key
    public static final String REDIS_KEY_ARTICLE_READ_COUNT = "article:readCount"; // 帖子阅读量
    public static final String REDIS_KEY_ARTICLE_ZAN_COUNT = "article:zanCount"; // 帖子

    public static final String REDIS_KEY_MSG_GROUP = "msg:group"; // 群聊消息
    public static final String REDIS_KEY_MSG_PRIVATE = "msg:private"; // 群聊消息


}
