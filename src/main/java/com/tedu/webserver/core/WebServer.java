package com.tedu.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * WebServer����
 * @author Administrator
 *
 */
public class WebServer {
	private ServerSocket server;
	private ExecutorService threadpool;
	public WebServer(){
		try {
			//TomcatĬ�Ͽ����Ķ˿ھ���8080
			server = new ServerSocket(8088);
			threadpool = Executors.newFixedThreadPool(50);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void start(){
		try {
			//��ʱ�������οͻ�������
			while(true){
				System.out.println("�ȴ��ͻ�������...");
				Socket socket = server.accept();
				System.out.println("һ���ͻ��������ˣ�");
				
				//����һ���̣߳�����ÿͻ�������
				ClientHandler handler = new ClientHandler(socket);
				//�����񽻸��̳߳ش���
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



























