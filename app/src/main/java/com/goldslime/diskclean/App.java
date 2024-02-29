package com.goldslime.diskclean;

import android.app.Application;
import android.content.Context;

import com.goldslime.diskclean.utils.LogUtil;

import java.io.PrintWriter;
import java.io.StringWriter;

public class App extends Application {

    public static String versionName;//版本名称
    public static String buildDate;//构建日期
    public Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        registerExceptionHandler();
    }

    void registerExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
    }

    static class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            ex.printStackTrace(printWriter);

            LogUtil.logFile("thread: id:" + thread.getId() + " name:" + thread.getName());
            LogUtil.logFile("message: " + ex.getMessage());
            LogUtil.logFile("localizedMessage : " + ex.getLocalizedMessage());
            LogUtil.logFile("stackTrace: " + stringWriter.toString());
        }
    }
}
