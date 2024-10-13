package com.ytrsoft.core;

public final class TimeUtils {

    private TimeUtils() {
        throw new UnsupportedOperationException();
    }

    public static String formatter(long useTime) {
        long second = 1000;
        long minute = 60 * second;
        long hour = 60 * minute;
        long day = 24 * hour;
        long month = 30 * day;
        long year = 12 * month;
        long years = useTime / year;
        useTime %= year;
        long months = useTime / month;
        useTime %= month;
        long days = useTime / day;
        useTime %= day;
        long hours = useTime / hour;
        useTime %= hour;
        long minutes = useTime / minute;
        useTime %= minute;
        long seconds = useTime / second;
        StringBuilder timeString = new StringBuilder();
        if (years > 0) timeString.append(years).append("年");
        if (months > 0) timeString.append(months).append("月");
        if (days > 0) timeString.append(days).append("天");
        if (hours > 0) timeString.append(hours).append("小时");
        if (minutes > 0) timeString.append(minutes).append("分钟");
        if (seconds > 0) timeString.append(seconds).append("秒");
        return timeString.length() > 0 ? timeString.toString().trim() : "0秒";
    }
}
