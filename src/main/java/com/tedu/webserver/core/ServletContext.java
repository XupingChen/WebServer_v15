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
 * ��������ʹ�õ�һЩ������Ϣ
 * @author Administrator
 *
 */
public class ServletContext {
	/**
	 * ������ӳ�䡣
	 * key�� ����·��
	 * vlaue�� ��ӦServlet������
	 */
	private static  Map<String,String> servletMapping = new HashMap<String,String>();
	static{
		initServletMapping();
	}
	/**
	 * ��ʼ��������Servletӳ����Ϣ
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
	//����getServletName����
//	public static void main(String[] args) {
//		String servletName = getServletName("/myweb/reg");
//		System.out.println(servletName);
//	}

}
