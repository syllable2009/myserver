package com;

import java.util.Arrays;
import java.util.List;

/**
 * @author 2019-07-15 20:15
 **/
public class UserAgentUtils {

    private final static List<String> agents = Arrays.asList("Android", "iPhone", "iPod", "iPad",
            "Windows Phone", "MQQBrowser");

    public static boolean isMoblie(String ua) {
        boolean flag = false;
        if (!ua.contains("Windows NT")
                || (ua.contains("Windows NT") && ua.contains("compatible"))) {
            // 排除 苹果桌面系统
            if (!ua.contains("Windows NT") && !ua.contains("Macintosh")) {
                for (String item : agents) {
                    if (ua.contains(item)) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }
}
