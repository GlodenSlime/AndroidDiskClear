package com.goldslime.diskclean.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ScreenUtil {

    // 关闭键盘输入法
    public static void closeSoftInput(Context context, View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
