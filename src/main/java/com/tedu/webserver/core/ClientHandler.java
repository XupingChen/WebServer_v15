package com.tedu.webserver.core;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;
import com.tedu.webserver.servlet.HttpServlet;
import com.tedu.webserver.servlet.LoginServlet;
import com.tedu.webserver.servlet.RegServlet;

/**
 * 处理客户端请求的线程任务
 * @author Administrator
 *
 */
public class ClientHandler implements Runnable{
	private Socket socket;
	public ClientHandler(Socket socket){
		this.socket = socket;
	}
	public void run(){
		/*
		 * 处理该客户端的请求的大致步骤
		 * 1：解析请求，创建HttpRequest
		 * 	    创建响应对象HttpResponse
		 * 2：处理请求
		 * 3：给予响应
		 */
		try {
			//1.解析请求，生成HttpRequest对象
			HttpRequest request = new HttpRequest(socket);
			HttpResponse response = new HttpResponse(socket);
			//2处理请求
			/*
			 * 通过request获取请求的资源路径，从webapps中寻找对应资源。
			 */
			String url = request.getRequestURI();
			System.out.println("url="+url);
			/*
			 * 判断是否是请求“
			 * 1.先根据用户请求获取对应的Servlet名字
			 * 2.若得到的名字不为null，说明是对应的业务
			 */
			String servletName = ServletContext.getServletName(url);
			System.out.println("servletName"+servletName);
			if(servletName != null){
				//加载该Servlet
				Class cls = Class.forName(servletName);
				System.out.println("cls="+cls);
				System.out.println("正在请求"+url+"正在实例化对应的"+servletName);
				//实例化
				HttpServlet servlet = (HttpServlet)cls.newInstance();
				servlet.service(request, response);
			}else{
				File file = new File("webapps"+url);
				if(file.exists()){
					System.out.println("资源已找到！");
					/*
					 * 以一个标准的HTTP响应格式回复客户端该资源
					 */
					OutputStream out = socket.getOutputStream();
					//发送状态行
					response.setStatusCode(200);
					response.setEntity(file);
				}else{
					System.out.println("资源未找到！");
					file  = new File("webapps/myweb/404.html");
					response.setStatusCode(404);
					response.setEntity(file);
				}
			}
			
			//3响应客户端
			response.flush();
		} catch (EmptyRequestException e) {
			System.out.println("空请求！");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//响应后与客户端断开连接
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
