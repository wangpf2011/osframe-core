/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 赵庆辉 2015-3-12 9:06:26
 */
package com.wf.ssm.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <P>Cookie工具集.</P>
 * 
 * @version 1.0
 * @author 赵庆辉 2015-3-12 9:06:26
 * @since JDK 1.6
 */
public class CookieUtils {

	/**
	 * <P>设置 Cookie（生成时间为1天）</P>
	 * @param response HttpServletResponse实例,cookie对象的载体.
	 * @param name 名称
	 * @param value 值
	 * @return void
	 */
	public static void setCookie(HttpServletResponse response, String name, String value) {
		setCookie(response, name, value, 60*60*24);
	}
	
	/**
	 * <P>设置 Cookie</P>
	 * @param name 名称
	 * @param value 值
	 * @param maxAge 生存时间（单位秒）
	 * @return void
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, null);
        if(StringUtils.isNotBlank(SpringContextHolder.getApplicationContext().getApplicationName())){
            cookie.setPath(SpringContextHolder.getApplicationContext().getApplicationName());
        }else{
            cookie.setPath("/");
        }
		cookie.setMaxAge(maxAge);
		try {
			cookie.setValue(URLEncoder.encode(value, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.addCookie(cookie);
	}
	
	/**
	 * <P>获得指定Cookie的值</P>
	 * @param request 获取cookie的载体
	 * @param name cookie的名称
	 * @return String cookie的值
	 */
	public static String getCookie(HttpServletRequest request, String name) {
		return getCookie(request, null, name, false);
	}
	
	/**
	 * <P>获得指定Cookie的值，并删除</P>
	 * @param request 获取cookie的载体
	 * @param response 因为这里需要进行cookie的移除操作,所以需要加上response对象,方便调用如下方法:
	 * cookie.setMaxAge(0);response.addCookie(cookie);
	 * @param name cookie的名称
	 * @return String cookie的值
	 */
	public static String getCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		return getCookie(request, response, name, true);
	}
	
	/**
	 * <P> 获得指定Cookie的值</P>
	 * @param request 请求对象
	 * @param response 响应对象
	 * @param name 名字
	 * @param isRemove 是否移除
	 * @return String 值
	 */
	public static String getCookie(HttpServletRequest request, HttpServletResponse response, String name, boolean isRemove) {
		String value = null;
		// 获取cookie集合列表
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					try {
						// 对获取到的String对象进行解码操作
						value = URLDecoder.decode(cookie.getValue(), "utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					if (isRemove) {
						// 如果设置为删除的话,设置最大存活时间为0
						cookie.setMaxAge(0);
						response.addCookie(cookie);
					}
				}
			}
		}
		return value;
	}
}
