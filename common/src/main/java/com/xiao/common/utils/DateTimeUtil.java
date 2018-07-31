package com.xiao.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

/**
 * 时间工具类
 * Created by xiao on 2018/7/31.
 */

public class DateTimeUtil {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yy-MM-dd", Locale.ENGLISH);

    public static String getSampleDate(Date date) {
        return FORMAT.format(date);
    }
}
