package com.tedu.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;

/**
 * �����¼����
 * @author Administrator
 *
 */
public class LoginServlet extends HttpServlet {
	public void service(HttpRequest request,HttpResponse response){
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		try(RandomAccessFile raf = new RandomAccessFile("user.dat","r")) {
			boolean check = false;
			for(int i=0;i<raf.length()/100;i++){
				//ÿ�ζ�ȡǰ�Ƚ�ָ���Ƶ�������¼�Ŀ�ʼλ��
				raf.seek(i*100);
				byte[] data = new byte[32];
				raf.read(data);
				String name = new String(data,"UTF-8").trim();
				if(name.equals(username)){
					raf.read(data);
					String pwd = new String(data,"UTF-8").trim();
					if(pwd.equals(password)){
						check = true;
						forward("/myweb/login_success.html",request,response);
						break;
					}
				}
			}
		if(!check){
			forward("/myweb/login_fail.html",request,response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
