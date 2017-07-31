package com.example.root.okfit.util;

import android.text.TextUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Zack on 2017/6/25.
 */

public class TimerUtils {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.CHINA);

    public static String getHowLongAgo(String timeString) {
        try {
            if (TextUtils.isEmpty(timeString)) {
                return "未知时间";
            }
            long time = sdf.parse(timeString).getTime();
            long now = System.currentTimeMillis();
            long minute = 60L * 1000L;
            long hour = 60L * 60L * 1000L;
            long day = 24L * 60L * 60L * 1000L;
            long month = 30L * 24L * 60L * 60L * 1000L;
            long interval = now - time;
            if (interval < minute) {
                return interval / 1000 + "秒前";
            } else if (interval < hour) {
                return interval / (minute) + "分前";
            } else if (interval < day) {
                return interval / (hour) + "小时前";
            } else if (interval < month) {
                return interval / (day) + "天前";
            } else {
                return interval / month + "月前";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "未知时间";
        }
    }
}
