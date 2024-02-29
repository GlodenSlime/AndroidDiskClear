package com.goldslime.diskclean.utils;

import android.os.Environment;

import com.socks.library.KLog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class LogUtil {

    public static final String TAG = "LogUtil";

    static BufferedWriter logWriter;
    static BufferedWriter commandWriter;
    static BufferedWriter commandByteWriter;

    public static void init() {
        String fileName = TimeUtil.generateFileName();
        String logFile = fileName + ".log.txt";
        String commandFile = fileName + ".command.txt";
        String commandByteFile = fileName + ".bytes.txt";

        String directory = Environment.getExternalStorageDirectory() + "/ASLog";

        File baseDirectory = new File(directory);
        if (!baseDirectory.exists()) {
            boolean res = baseDirectory.mkdirs();
            if (!res) {
                return;
            }
        }

        //清理超过4天的日志文件
        File[] files = baseDirectory.listFiles();
        if (files != null) {
            long curTime = System.currentTimeMillis();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    if (curTime - files[i].lastModified() > 4 * 86400000 && files[i].getName().endsWith("txt")) {
                        if (files[i].canWrite()) {
                            files[i].delete();
                        }
                    }
                }
            }
        }

        String logFilePath = baseDirectory + "/" + logFile;
        String commandFilePath = baseDirectory + "/" + commandFile;
        String commandByteFilePath = baseDirectory + "/" + commandByteFile;
        try {
            File file = new File(logFilePath);
            FileOutputStream logOutputStream = new FileOutputStream(file, true);
            logWriter = new BufferedWriter(new OutputStreamWriter(logOutputStream));

            file = new File(commandFilePath);
            FileOutputStream commandOutputStream = new FileOutputStream(file, true);
            commandWriter = new BufferedWriter(new OutputStreamWriter(commandOutputStream));

            file = new File(commandByteFilePath);
            FileOutputStream commandByteOutputStream = new FileOutputStream(file, true);
            commandByteWriter = new BufferedWriter(new OutputStreamWriter(commandByteOutputStream));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void log(String msg) {
        KLog.i(TAG, msg);
    }

    public static void logCommand(String command) {
        if (commandWriter == null) return;
        try {
            commandWriter.write(TimeUtil.getCurrentTime() + ": " + command + "\n");
            commandWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logFile(String content) {
        KLog.i(TAG, content);
        if (logWriter == null) return;
        try {
            logWriter.write(TimeUtil.getCurrentTime() + ": " + content + "\n");
            logWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logBytes(String bytesString) {
        if (commandByteWriter == null) return;
        try {
            commandByteWriter.write(TimeUtil.getCurrentMilTime() + ": " + bytesString + "\n");
            commandByteWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
