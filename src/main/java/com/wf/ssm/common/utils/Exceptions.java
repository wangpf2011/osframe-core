/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 赵庆辉 2015-3-12 9:06:26
 */
package com.wf.ssm.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <P>关于异常的工具类.</P>
 * @version 1.0
 * @author 赵庆辉 2015-3-12 9:06:26
 * @since JDK 1.6
 */
public class Exceptions {

	/**
	 * <P>将检查型异常转变为费检查型异常(Exception-->RuntimeException)</P>
	 * @param e 需要进行转化的检查型异常
	 * @return RuntimeException 经过转换了的非检查型异常
	 */
	public static RuntimeException unchecked(Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else {
			return new RuntimeException(e);
		}
	}

	/**
	 * <P>将控制台的堆栈错误信息转化为字符串返回</P>
	 * @param e 需要进行转化的异常对象
	 * @return String 经过转换了的字符串
	 */
	public static String getStackTraceAsString(Exception e) {
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

	/**
	 * <P>判断异常是否由某些底层的异常引起.</P>
	 * @param ex 表示需要进行检查的异常对象
	 * @param causeExceptionClasses 表示需要进行匹配的一些底层的异常对象
	 * Class<? extends Exception>... causeExceptionClasses 这里依然采用了泛型的形式,表示继承自Exception的类的类型作为Class对象的泛型.
	 * @return boolean true or false
	 */
	public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses) {
		Throwable cause = ex.getCause();
		while (cause != null) {
			for (Class<? extends Exception> causeClass : causeExceptionClasses) {
				if (causeClass.isInstance(cause)) {
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}
}
