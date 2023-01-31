package com.xinder.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dong
 * @since 2022-09-07 09:20
 */
public class CookieUtils {

    /**
     * 设置cookie
     *
     * @param response
     * @param name     cookie名字
     * @param value    cookie值
     * @param maxAge   cookie生命周期 以秒为单位
     */
    public static void addCookie(HttpServletResponse response, String domain, String path, String name,
                                 String value, int maxAge, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
    }

    /**
     * 根据cookie名称读取cookie
     *
     * @param request
     * @return map<cookieName, cookieValue>
     */
    public static Map<String, String> readCookie(HttpServletRequest request, String... cookieNames) {
        Map<String, String> cookieMap = new HashMap<String, String>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                String cookieValue = cookie.getValue();
                for (int i = 0; i < cookieNames.length; i++) {
                    if (cookieNames[i].equals(cookieName)) {
                        cookieMap.put(cookieName, cookieValue);
                    }
                }
            }
        }
        return cookieMap;

    }

    /**
     * 删除cookie
     *
     * @param response
     * @param domain
     * @param path
     * @param name
     * @param httpOnly
     */
    public static void deleteCookie(HttpServletResponse response, String domain, String path, String name, boolean httpOnly) {
        Cookie cookie = new Cookie(name, null);
        cookie.setDomain(domain);//保存cookie的IP地址,则是删除这个IP的cookie
        cookie.setPath(path);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
    }
}
