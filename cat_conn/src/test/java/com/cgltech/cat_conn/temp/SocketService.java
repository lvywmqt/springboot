package com.cgltech.cat_conn.temp;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketService {

    public static void main(String[] args) {
       Socket socket = null;
       ServerSocket serviceSocket =null;
        try {
            //服务器端接收消息的类。定制端口号为8888
             serviceSocket = new ServerSocket(10000);
             System.out.println("服务器已经启动");
             while(true){
                    //获取socket。这个方法是阻塞式的
                    socket = serviceSocket.accept();
                    //一但获取连接后就开子线程来处理
                  new LogicThread(socket);
             }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
             //释放资源
              try {
                serviceSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}