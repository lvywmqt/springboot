package com.cgltech.cat_conn.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//import net.sf.json.JSONObject;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientCommon {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientCommon.class);

    /**
     * post接口
     *
     * @param csUrl         url
     * @param obj           参数
     * @param Authorization requestProperty值
     * @return
     */
    public static String httpPost(String csUrl, JSONObject obj, String Authorization) {
        StringBuffer sb = new StringBuffer("");
        try {
            //创建连接
            URL url = new URL(csUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("accept", "application/json;");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Authorization", Authorization);
            connection.connect();
            //POST请求
            DataOutputStream out = new DataOutputStream(
                    connection.getOutputStream());
            // out.writeBytes(obj.toString());
            out.write(obj.toString().getBytes("UTF-8"));
            out.flush();
            out.close();
            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "UTF-8");
                sb.append(lines);
            }
            LOGGER.info("提交结果：[{}]", sb);
            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (MalformedURLException e) {
            LOGGER.error("发送http请求报错:[{}]", e);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("发送http请求报错:[{}]", e);
        } catch (IOException e) {
            LOGGER.error("发送http请求报错:[{}]", e);
        }
        return sb.toString();
    }

    private static int READ_TIMEOUT = 10000;
    private static int CNNECTION_TIMEOUT = 1000;

    public static String httpPost(String csUrl, String body) {
        StringBuffer sb = new StringBuffer("");
        try {
            //创建连接
            URL url = new URL(csUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();

            connection.setConnectTimeout(CNNECTION_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("accept", "application/json;");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Authorization", null);
            connection.connect();

            //POST请求
            DataOutputStream out = new DataOutputStream(
                    connection.getOutputStream());
            // out.writeBytes(obj.toString());
            out.write(body.getBytes("UTF-8"));
            out.flush();
            out.close();
            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "UTF-8");
                sb.append(lines);
            }
            LOGGER.info("提交结果：[{}]", sb);
            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (MalformedURLException e) {
            LOGGER.error("发送http请求报错:[{}]", e);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("发送http请求报错:[{}]", e);
        } catch (IOException e) {
            LOGGER.error("发送http请求报错:[{}]", e);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        httpPost("http://192.168.0.1:9999/sale", "");
    }
}
