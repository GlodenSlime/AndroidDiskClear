package com.goldslime.diskclean.utils;

import com.socks.library.KLog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    public static final String PREFIX = "http://47.92.220.108:8886";

    //发送Post请求
    public static String SendPost(String url, String param) {
//        KLog.e("FFGG","SendPost");
        param = param.replace("+", "%2B");
        HttpURLConnection conn = null;
        OutputStream out = null;
        BufferedReader br = null;

        try {
            // 创建 URL
            URL restUrl = new URL(url);
            // 打开连接
            conn = (HttpURLConnection) restUrl.openConnection();
            //请求时间
            conn.setConnectTimeout(5000);
            // 请求方式为 POST
            conn.setRequestMethod("POST");

            //JSON数据
//            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 输入 输出
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //连接
            conn.connect();

            // 传递参数流的方式
            out = conn.getOutputStream();
            out.write(param.getBytes());
            out.flush();

            KLog.e("FFGG", "Send ok");
            // 读取数据
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;

            StringBuilder result = new StringBuilder();
            while (null != (line = br.readLine())) {
                KLog.e("FFGG", "append line");
                result.append(line);
            }
            KLog.e("FFGG", "Read ok");

            return result.toString();
        } catch (Exception e) {
            KLog.e("FFGG", e.getMessage());
            e.printStackTrace();
        } finally {
            // 关闭所有通道
            try {
                if (br != null)
                    br.close();
                if (out != null)
                    out.close();
                if (conn != null)
                    conn.disconnect();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return null;
    }
}
