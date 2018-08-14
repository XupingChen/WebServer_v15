package com.tedu.webserver.core;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 定义服务端使用的一些配置信息
 * @author Administrator
 *
 */
public class ServletContext {
	/**
	 * 请求与映射。
	 * key： 请求路径
	 * vlaue： 对应Servlet的名字
	 */
	private static  Map<String,String> servletMapping = new HashMap<String,String>();
	static{
		initServletMapping();
	}
	/**
	 * 初始化请求与Servlet映射信息
	 */
	private static void initServletMapping(){
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new File("conf/server.xml"));
			Element el = doc.getRootElement().element("servlets");
			List<Element> list = el.elements();
			for(Element ele:list){
				Attribute a1 = ele.attribute("url");
				Attribute a2 = ele.attribute("className");
				String key = a1.getValue();
				String value = a2.getValue();
				servletMapping.put(key, value);
				System.out.println(key+","+value);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	public static String getServletName(String url){
		return servletMapping.get(url);
	}
	//测试getServletName方法
//	public static void main(String[] args) {
//		String servletName = getServletName("/myweb/reg");
//		System.out.println(servletName);
//	}

}
