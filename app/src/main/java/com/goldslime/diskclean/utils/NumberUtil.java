package com.goldslime.diskclean.utils;

import java.util.regex.Pattern;

public class NumberUtil {
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]*");
        return pattern.matcher(str).matches();
    }
}
