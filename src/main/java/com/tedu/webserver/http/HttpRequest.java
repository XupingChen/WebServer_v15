package com.tedu.webserver.http;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.tedu.webserver.core.EmptyRequestException;

/**
 * HttpRequest��ʾһ��HttpЭ��Ҫ���������Ϣ
 * һ��������������֣�
 * �����У���Ϣͷ����Ϣ����
 * 
 * @author Administrator
 *
 */
public class HttpRequest {
	//��Ӧ�ͻ��˵�Socket
	private Socket socket;
	//ͨ��Socket��ȡ�������������ڶ�ȡ�ͻ��˷��͵�����
	private InputStream in;
	/*
	 * �����������Ϣ����
	 */
	//����ʽ
	private String method;
	//��Դ·��
	private String url;
	//����ʹ�õ�Э��汾
	private String protocol;
	//url�е����󲿷�
	private String requestURI;
	//url�еĲ�������
	private String queryString;
	//url�е����в���
	//key:��������value:����ֵ
	private Map<String,String> parameters = new HashMap<String,String>();


	/*
	 * ��Ϣͷ�����Ϣ
	 */
	//����ʽ
	private Map<String,String> headers = new HashMap<String,String>();
	/**
	 * ʵ����HttpRequestʹ�õĹ��췽������Ҫ����Ӧ�ͻ��˵�Socket���룬�Ա��ȡ�ͻ��˷��͹�������������
	 * @param socket
	 * @throws EmptyRequestException 
	 */
	public HttpRequest(Socket socket) throws EmptyRequestException{
		System.out.println("HttpRequest:��ʼ��������");
		try{
			this.socket = socket;
			this.in  = socket.getInputStream();
			/*
			 * 1.����������
			 * 2.������Ϣͷ
			 * 3.������Ϣ����
			 */
			//1
			parseRequestLine();
			//2
			parseHeaders();
			//3
			parseContent();
		}catch(EmptyRequestException e){
			//���������׳���ClientHandler
			throw e;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * ����������
	 * @throws EmptyRequestException 
	 */
	private void parseRequestLine() throws EmptyRequestException{
		System.out.println("����������...");
		/*
		 * �������̣�
		 * 1.ͨ����������ȡ��һ���ַ���
		 * 2.�������а��տո���Ϊ����
		 * 3.����ֵ�����ֱ����õ�method,url,protocol����������
		 * localhost:8088/index.html
		 * 
		 * ����������ʱ���ڻ�ȡ��ֺ������Ԫ��ʱ���ܻ����������±�Խ�磬��������HttpЭ��
		 * ����ͻ��˷���һ��������������µġ����Ǻ�������
		 */
		String line = readLine();
		String[]data = line.split("\\s");
		//��������������Ƿ��ܴﵽ3��
		if(data.length<3){
			//����һ��������
			throw new EmptyRequestException();
		}
		this.method = data[0];
		this.url = data[1];
		//��һ������url����
		parseURL();
		this.protocol = data[2];
		System.out.println("method:"+method);
		System.out.println("url:"+url);
		System.out.println("protocol:"+protocol);
		System.out.println("�����н������");
	}
	/**
	 * ��һ����url���н���
	 * ��url�е����󲿷����õ�����requestURI��
	 * ��url�еĲ����������õ�����queryString��
	 * �ڶԲ������ֽ�һ����������ÿ�����������뵽����parameters��
	 * 
	 * ����url�����в������֣���ֱ�ӽ�url��ֵ��ֵ��requestURI���������ֲ����κδ���
	 */
	private void parseURL(){
		System.out.println("��ʼ����url��"+url);
		/*
		 * ˼·��
		 * url�Ƿ��в��������Ը��ݸ�url���Ƿ���?����������������?���Ϊ������
		 * ��һ����Ϊ���󲿷֣��ڶ�����Ϊ�������֣����õ���Ӧ���Լ��ɡ�Ȼ���ڶԲ������в��
		 * ���ս�ÿ��������������Ϊkey,ֵ��Ϊvalue���뵽parameter�С�
		 * �������в�������ֱ�ӽ�url��ֵ��requestURI���ɡ�
		 * /myweb/reg.html
		 * /myweb/reg?username=fan&password=123&...
		 */
		if(this.url.contains("?")){
			String[] data = this.url.split("\\?");
			this.requestURI = data[0];
			if(data.length>1){
				this.queryString = data[1];
				//��һ������queryString
				parseParameters(queryString);
			}
		}else{
			this.requestURI = this.url;
		}

		//		if(this.url.indexOf("?")!=-1){//this.url.contains("?")!=-1
		//			String[] data = this.url.split("\\?");
		//			requestURI = data[0];
		//			queryString = data[1];
		//			//��һ������queryString
		//			String[] st = queryString.split("&");
		//			for(int i=0;i<st.length;i++){
		//				String[] s = st[i].split("=");
		//				parameters.put(s[0], s[1]);
		//			}
		//		}else{
		//			requestURI = url;
		//		}
		System.out.println("requestURI:"+requestURI);
		System.out.println("queryString:"+queryString);
		System.out.println("parameters:"+parameters);
		System.out.println("url������ϣ�");
	}
	/**
	 * ���ͻ��˷��͹����Ĳ������н���
	 * ��ʽΪ��name1=value&name2=value&....
	 * @param line
	 */
	private void parseParameters(String line){
		/*
		 * ����ǰ��ת��
		 */
		try {
			line = URLDecoder.decode(line,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String[] str = line.split("&");
		for(String st : str){
			String[] s = st.split("=");
			if(s.length>1){
				this.parameters.put(s[0], s[1]);
			}else{
				this.parameters.put(s[0], "");
			}
		}
	}


	/**
	 * ������Ϣͷ
	 */
	private void parseHeaders(){
		System.out.println("������Ϣͷ...");
		/*
		 * ���²��裺
		 * 1.����ʹ��readLine������ȡ���������ݣ�ÿһ��Ӧ�ö���һ����Ϣͷ
		 * 2.��readLine��������ֵΪ���ַ���ʱ��ֹͣѭ����ȡ������������ȡ����CRLFʱ
		 * 	readLine��������ֵӦ��Ϊ���ַ�����
		 * 3.ÿ����ȡһ����Ϣͷ��ϢʱӦ������": "���Ϊ�����һ��Ϊ��Ϣͷ���֣��ڶ���Ϊ��Ϣͷ��Ӧ��ֵ����������Ϊkey��
		 * ��ֵ��Ϊvalue���뵽����headers���Map��
		 */
		//		String str;
		//		while((str=readLine())!=null&&!("".equals(str))){
		//			String[] array = str.split(":\\s");
		//			headers.put(array[0], array[1]);
		//		}
		//		System.out.println("------------------------");
		//		Set<Entry<String,String>> set = headers.entrySet();
		//		for(Entry<String,String> e :set){
		//			System.out.println(e.getKey()+","+e.getValue());
		//		}
		while(true){
			String line = readLine();
			if("".equals(line)){
				break;
			}
			String[] data = line.split(":\\s");
			headers.put(data[0], data[1]);
		}
		System.out.println("headers:"+headers);
		System.out.println("��Ϣͷ�������");
	}
	/**
	 * ������Ϣ����
	 */
	private void parseContent(){
		System.out.println("������Ϣ����...");
		/*
		 * ��һ��form����post��ʽ�ύʱ����ô���е����ݻᱻ��������Ϣ�����С�
		 * ͨ����Ϣͷ���Ƿ���Content-Type��Content-Length�Ϳ��Ե�֪�Ƿ���
		 * ��Ϣ�����Լ���Ϣ���ĵ�������ʲô���Ա㿪ʼ������Ϣ���ġ�
		 */

		//�Ƿ�����Ϣ����
		if(headers.containsKey("Content-Length")){
			int length = Integer.parseInt(headers.get("Content-Length"));
			System.out.println("��Ϣ���ĳ���Ϊ��"+length);
			String type = headers.get("Content-Type");
			System.out.println("��Ϣ��������Ϊ��"+type);
			/*
			 * ���������ж���������
			 */
			if("application/x-www-form-urlencoded".equals(type)){
				/*
				 * ͨ����������ȡ�ֽ�����ԭΪ�ַ���
				 */
				try {
					byte[] data = new byte[length];
					in.read(data);
					String line = new String(data,"ISO8859-1");
					System.out.println("�������ݣ�"+line);
					//ת��
					line = URLDecoder.decode(line,"UTF-8");
					//��������
					parseParameters(line);
				} catch (Exception e) {
					e.printStackTrace();
				}


			}
		}

		System.out.println("��Ϣ���Ľ������");
	}
	/**
	 * ͨ����������������ȡһ���ַ�������CRLF��β��
	 * 
	 * @return
	 */
	private String readLine( ){
		try {
			StringBuilder builder = new StringBuilder();
			int d = -1;
			//c1��ʾ�ϴζ������ַ���c2��ʾ���ζ������ַ�
			char c1 = 'a', c2 = 'a';
			while((d = in.read())!=-1){
				c2 = (char)d;
				/*
				 * ��ASC������CR�ı����Ӧ������Ϊ13
				 * LF�����Ӧ������Ϊ10
				 * �ͺñ��ַ�a�ı����Ӧ������Ϊ97
				 */
				if(c1==13 && c2==10){
					break;
				}
				builder.append(c2);
				c1 = c2;
			}
			return builder.toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public String getMethod() {
		return method;
	}
	public String getUrl() {
		return url;
	}
	public String getProtocol() {
		return protocol;
	}
	public String getHeader(String name){
		return headers.get(name);
	}
	public String getRequestURI() {
		return requestURI;
	}
	public String getQueryString() {
		return queryString;
	}
	/**
	 * ���ݸ����Ĳ�������ȡ��Ӧ�Ĳ���ֵ
	 * @param name
	 * @return
	 */
	public String getParameter(String name){
		return this.parameters.get(name);
	}
}




























