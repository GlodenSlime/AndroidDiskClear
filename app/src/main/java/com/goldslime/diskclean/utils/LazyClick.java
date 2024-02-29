package com.goldslime.diskclean.utils;

import java.util.HashMap;
import java.util.Map;

public class LazyClick {
    static Map<Integer, Long> records = new HashMap<>();

    //避免1秒内重复点击多次，导致一段逻辑被多次执行
    public static void click(int id, Runnable runnable) {
        click(id, runnable, 1000);
    }

    //避免指定时间内重复点击，导致一段逻辑被多次执行
    public static void click(int id, Runnable runnable, long interval) {
        if (records.containsKey(id)) {
            long time = records.get(id);

            if (System.currentTimeMillis() - time < interval) {
                return;
            }
        }

        records.put(id, System.currentTimeMillis());
        runnable.run();
    }
}
