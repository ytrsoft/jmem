package com.ytrsoft.utils;

public class TimeTransform implements Transform {
    @Override
    public String transform(Object value) {
        long seconds = (long) value;
        long[] units = {365 * 24 * 3600, 30 * 24 * 3600, 24 * 3600, 3600, 60};
        long[] timeValues = new long[units.length + 1];
        String[] labels = {"年", "月", "天", "时", "分", "秒"};

        for (int i = 0; i < units.length; i++) {
            timeValues[i] = seconds / units[i];
            seconds %= units[i];
        }
        timeValues[units.length] = seconds;

        StringBuilder timeBuilder = new StringBuilder();
        for (int i = 0; i < timeValues.length; i++) {
            if (timeValues[i] > 0) {
                timeBuilder.append(timeValues[i]).append(labels[i]);
            }
        }

        return timeBuilder.length() > 0 ? timeBuilder.toString().trim() : "0秒";
    }
}
