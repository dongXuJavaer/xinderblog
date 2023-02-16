package com.xinder.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * @author Xinder
 * @date 2023-02-15 22:19
 */
public class DateUtils {


    /**
     * 将指定日期对象转换成格式化字符串
     *
     * @param date
     *            Date XML日期对象
     * @param datePattern
     *            String 日期格式
     * @return String
     */
    public static String getDateStr(Date date, String datePattern) {
        SimpleDateFormat sd = new SimpleDateFormat(datePattern);
        return sd.format(date);
    }
}
