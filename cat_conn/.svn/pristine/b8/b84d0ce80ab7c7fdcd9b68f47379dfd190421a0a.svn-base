package com.cgltech.cat_conn.temp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class LogicThread extends Thread {

    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;

    public LogicThread(Socket socket) {
        this.socket = socket;
        start(); // 启动线程
    }
    public void run() {
        byte[] buf = new byte[1024];
        try {
            // 初始化流
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            for (int i = 0; i < 3; i++) {
                // 读取数据
                int len = inputStream.read(buf);
                System.out.println(new String(buf,0,len));
                // 反馈数据
                outputStream.write("收到".getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }
    /**
     * 
     * 关闭流和连接
     * 
     */
    private void close() {
        try {
            // 关闭流和连接
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (Exception e) {
        }
    }
}