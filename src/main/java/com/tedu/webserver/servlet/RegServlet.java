package com.tedu.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;

/**
 * ����ע��ҵ��
 * @author Administrator
 *
 */
public class RegServlet extends HttpServlet {
	public void service(HttpRequest request,HttpResponse response){
        try{		
		System.out.println("��ʼ����ע��ҵ�񣡣�");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		int age = Integer.parseInt(request.getParameter("age"));
		//��Ҫ���û����ݽ�����֤
		try(RandomAccessFile raf = new RandomAccessFile("user.dat","rw");){
			//д�û���
			byte[] data = username.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			//д����
			data = password.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			//д�ǳ�
			data = nickname.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			//д����
			raf.writeInt(age);
			raf.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		//��Ӧ�ͻ���ע��ɹ�ҳ��
		//forward("/myweb/reg_success.html",request,response);
		/*
		 * �����ض���·��ʱע�⣬��ʹ�����·������Ե�Ӧ��������ǰServletʱ��·����
		 * ���磺����RegServletʱ���ͻ���·��Ϊ��
		 * http��//localhost��8088/myweb/reg
		 * ��ô��ǰĿ¼���ǣ�
		 * http��//localhost��8088/myweb
		 * ���ԣ��������ض���Ŀ¼ʱֻ��Ҫ����Ϊ��
		 * reg_success.html����
		 * ��ô������õ����·�����ƴ�ڵ�ǰĿ¼�У�
		 * http��//localhost��8088/myweb/reg_success.html
		 */
		response.sendRedirect("reg_success.html");
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
}
