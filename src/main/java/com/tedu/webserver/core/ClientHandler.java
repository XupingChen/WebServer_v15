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
 * ����ͻ���������߳�����
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
		 * ����ÿͻ��˵�����Ĵ��²���
		 * 1���������󣬴���HttpRequest
		 * 	    ������Ӧ����HttpResponse
		 * 2����������
		 * 3��������Ӧ
		 */
		try {
			//1.������������HttpRequest����
			HttpRequest request = new HttpRequest(socket);
			HttpResponse response = new HttpResponse(socket);
			//2��������
			/*
			 * ͨ��request��ȡ�������Դ·������webapps��Ѱ�Ҷ�Ӧ��Դ��
			 */
			String url = request.getRequestURI();
			System.out.println("url="+url);
			/*
			 * �ж��Ƿ�������
			 * 1.�ȸ����û������ȡ��Ӧ��Servlet����
			 * 2.���õ������ֲ�Ϊnull��˵���Ƕ�Ӧ��ҵ��
			 */
			String servletName = ServletContext.getServletName(url);
			System.out.println("servletName"+servletName);
			if(servletName != null){
				//���ظ�Servlet
				Class cls = Class.forName(servletName);
				System.out.println("cls="+cls);
				System.out.println("��������"+url+"����ʵ������Ӧ��"+servletName);
				//ʵ����
				HttpServlet servlet = (HttpServlet)cls.newInstance();
				servlet.service(request, response);
			}else{
				File file = new File("webapps"+url);
				if(file.exists()){
					System.out.println("��Դ���ҵ���");
					/*
					 * ��һ����׼��HTTP��Ӧ��ʽ�ظ��ͻ��˸���Դ
					 */
					OutputStream out = socket.getOutputStream();
					//����״̬��
					response.setStatusCode(200);
					response.setEntity(file);
				}else{
					System.out.println("��Դδ�ҵ���");
					file  = new File("webapps/myweb/404.html");
					response.setStatusCode(404);
					response.setEntity(file);
				}
			}
			
			//3��Ӧ�ͻ���
			response.flush();
		} catch (EmptyRequestException e) {
			System.out.println("������");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//��Ӧ����ͻ��˶Ͽ�����
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
