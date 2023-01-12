package com.xinder.common.util;

import com.alibaba.fastjson.JSONObject;
import com.xinder.api.response.base.BaseResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Xinder
 * @date 2023-01-09 10:23
 */
public class ResponseUtils {

    /**
     * 响应成功信息
     *
     * @param response 响应
     *
     */
    public static void respJsonSuccess(HttpServletResponse response) {
        BaseResponse result = BaseResponse.success();
        resp(response, result);
    }

    /**
     * 响应失败信息
     *
     * @param response 响应
     *
     */
    public static void respJsonFail(HttpServletResponse response) {
        BaseResponse result = BaseResponse.fail();
        resp(response, result);
    }

    /**
     * 返回自定义信息
     * @param response ...
     */
    public static void respJson(HttpServletResponse response, BaseResponse result) {
        resp(response, result);
    }


    private static void resp(HttpServletResponse response, BaseResponse result) {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(JSONObject.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }
}
