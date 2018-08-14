package com.tedu.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;



/**
 * ���ඨ����HTTPЭ�������Ϣ
 * @author Administrator
 *
 */
public class HttpContext {
	private static final int i = 0;
	/*
	 * ״̬�������Ӧ״̬������ӳ���ϵ
	 * key:״̬����
	 * value��״̬����
	 */
	private static Map<Integer,String> STATUS_REASON_MAPPING = new HashMap<Integer,String>();
	/**
	 * ��Դ��׺��Content-Type֮���ӳ���ϵ
	 * key:��Դ�ĺ�׺��
	 * value:����Դ��Ӧ��Content-Type��ֵ
	 * ע����ͬ����Դ��Ӧ��Content-Type��ֵ��w3c�϶��ж��壬
	 * ��ǰ��w3c������ѯMIME����
	 */
	private static Map<String,String> MIME_MAPPING = new HashMap<String,String>();
	
	static{
		initStatusReasonMapping();
		initMimeMapping();
	}
	/**
	 * ��ʼ����Դ��׺��Content-Type��ӳ��Map
	 */
	private static void initMimeMapping(){
		/*
		 * ��ȡconf/web.xml�ļ�������Ԫ����������Ϊ
		 * <mime-mapping>����Ԫ�ض�ȡ������
		 * <extension>�м���ı���Ϊkey������Ԫ��
		 * <mime-type>�м���ı���Ϊvalue�����뵽
		 * MIME-MAPPING�У���ɳ�ʼ����
		 */
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new File("conf/web.xml"));
			Element root = doc.getRootElement();
			List<Element> mimeList = root.elements("mime-mapping");
			for(Element mimeEle : mimeList){
				String key = mimeEle.elementText("extension");
				String value = mimeEle.elementText("mime-type");
				MIME_MAPPING.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		try {
//			FileInputStream fis = new FileInputStream("conf/web.xml");
//			SAXReader reader = new SAXReader();
//			Document doc = reader.read(fis);
//			Element root = doc.getRootElement();
//			List list = root.elements("mime-mapping");
//			for(int i=0;i<list.size();i++){
//				Element e = (Element)list.get(i);
//				Element name = e.element("extension");
//				Element value = e.element("mime-type");
////				System.out.println(name);
////				System.out.println(value);
//				MIME_MAPPING.put(name.getText(), value.getText());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//		MIME_MAPPING.put("html", "text/html");
//		MIME_MAPPING.put("png", "image/png");
//		MIME_MAPPING.put("gif", "image/gif");
//		MIME_MAPPING.put("jpg", "image/jpeg");
//		MIME_MAPPING.put("css", "text/css");
//		MIME_MAPPING.put("js", "application/javascript");
	}
	/**
	 * ��ʼ��״̬������������ӳ��MAP
	 */
	private static void initStatusReasonMapping(){
		STATUS_REASON_MAPPING.put(200, "Ok");
		STATUS_REASON_MAPPING.put(302, "Move Temporaily");
		STATUS_REASON_MAPPING.put(404, "NOT FOUND");
		STATUS_REASON_MAPPING.put(500, "Internal Server Error");
	}
	public static String getMimeType(String ext){
		return MIME_MAPPING.get(ext);
	}
	/**
	 * ���ݸ�����״̬�����ȡ��Ӧ��״̬����
	 * @param statusCode
	 * @return
	 */
	public static String getStatusReason(int statusCode){
		return STATUS_REASON_MAPPING.get(statusCode);
	}
	public static void main(String[] args) {
		//����
		String type = getMimeType("js");
		System.out.println(type);
		String reason = getStatusReason(200);
//		System.out.println(reason);
//		String st = getMimeType("png");
//		System.out.println(st);
	}
}
