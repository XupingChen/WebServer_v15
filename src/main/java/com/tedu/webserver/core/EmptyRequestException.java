package com.tedu.webserver.core;
/**
 * �������쳣
 * ���ͻ������Ӻ��Ϳ�����ʱ��HttpRequest�Ĺ��췽��
 * ���׳����쳣��
 * @author Administrator
 *
 */
public class EmptyRequestException extends Exception{
	private static final long serialVersionUID = 1L;

	public EmptyRequestException() {
		super();
	}

	public EmptyRequestException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EmptyRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmptyRequestException(String message) {
		super(message);
	}

	public EmptyRequestException(Throwable cause) {
		super(cause);
	}
	
}
