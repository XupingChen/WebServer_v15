package com.tedu.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;

/**
 * 处理注册业务
 * @author Administrator
 *
 */
public class RegServlet extends HttpServlet {
	public void service(HttpRequest request,HttpResponse response){
        try{		
		System.out.println("开始处理注册业务！！");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		int age = Integer.parseInt(request.getParameter("age"));
		//还要对用户数据进行验证
		try(RandomAccessFile raf = new RandomAccessFile("user.dat","rw");){
			//写用户名
			byte[] data = username.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			//写密码
			data = password.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			//写昵称
			data = nickname.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			//写年龄
			raf.writeInt(age);
			raf.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		//响应客户端注册成功页面
		//forward("/myweb/reg_success.html",request,response);
		/*
		 * 设置重定向路径时注意，若使用相对路径，相对的应当是请求当前Servlet时的路径。
		 * 例如：请求RegServlet时，客户端路径为：
		 * http：//localhost：8088/myweb/reg
		 * 那么当前目录就是：
		 * http：//localhost：8088/myweb
		 * 所以，在设置重定向目录时只需要设置为：
		 * reg_success.html即可
		 * 那么浏览器得到这个路径后会拼在当前目录中：
		 * http：//localhost：8088/myweb/reg_success.html
		 */
		response.sendRedirect("reg_success.html");
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
}
