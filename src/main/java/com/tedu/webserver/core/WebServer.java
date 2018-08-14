package com.tedu.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * WebServer主类
 * @author Administrator
 *
 */
public class WebServer {
	private ServerSocket server;
	private ExecutorService threadpool;
	public WebServer(){
		try {
			//Tomcat默认开启的端口就是8080
			server = new ServerSocket(8088);
			threadpool = Executors.newFixedThreadPool(50);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void start(){
		try {
			//暂时不处理多次客户端连接
			while(true){
				System.out.println("等待客户端连接...");
				Socket socket = server.accept();
				System.out.println("一个客户端连接了！");
				
				//启动一个线程，处理该客户端请求
				ClientHandler handler = new ClientHandler(socket);
				//将任务交给线程池处理
				threadpool.execute(handler);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//ddos
	public static void main(String[] args) {
		WebServer server = new WebServer();
		server.start();
	}
}



























