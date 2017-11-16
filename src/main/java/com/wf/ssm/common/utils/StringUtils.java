/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 赵庆辉 2015-3-12 9:06:26
 */
package com.wf.ssm.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import com.wf.ssm.common.utils.SpringContextHolder;

/**
 * <P>字符串工具类, 继承org.apache.commons.lang3.StringUtils类</P>
 * 
 * @version 1.0
 * @author wangpf 2015-3-12 9:06:26
 * @since JDK 1.6
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
	
	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的<a target="_blank" style="color: #0000F0; display:inline; position:static; background:none;" href="http://www.so.com/s?q=%E6%AD%A3%E5%88%99%E8%A1%A8%E8%BE%BE%E5%BC%8F&ie=utf-8&src=se_lighten_f">正则表达式</a>
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String regEx_space = "\\s*|\\t|\\r|\\n";//定义空格回车 和& 符号
    private static final char SEPARATOR = '_';
    private static final String CHARSET_NAME = "UTF-8";
    
    
    /**
     * 转换为字节数组
     * @param str
     * @return
     */
    public static byte[] getBytes(String str){
    	if (str != null){
    		try {
				return str.getBytes(CHARSET_NAME);
			} catch (UnsupportedEncodingException e) {
				return null;
			}
    	}else{
    		return null;
    	}
    }
    
    /**
     * 转换为字节数组
     * @param str
     * @return
     */
    public static String toString(byte[] bytes){
    	try {
			return new String(bytes, CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			return EMPTY;
		}
    }
    
    
    /**
     * <P>删除Html标签</P>
     * 
     * @param htmlStr
     * @return 文本字符串
     */
    public static String delHTMLTag(String htmlStr) {
    	if(htmlStr==null){
    		return " ";
    	}
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr= m_space.replaceAll(""); // 过滤空格回车标签
        //替换掉&符号，产生的word生成的bug
        htmlStr=htmlStr.replaceAll("&", "");  
        
        return htmlStr.trim(); // 返回文本字符串
    }
	
    /**
     * <P>字符串第一个字符小写</P>
     * 
     * @param str
     * @return 字符串
     */
	public static String lowerFirst(String str){
		if(StringUtils.isBlank(str)) {
			return "";
		} else {
			return str.substring(0,1).toLowerCase() + str.substring(1);
		}
	}
	
	/**
     * <P>字符串第一个字符大写</P>
     * 
     * @param str
     * @return 字符串
     */
	public static String upperFirst(String str){
		if(StringUtils.isBlank(str)) {
			return "";
		} else {
			return str.substring(0,1).toUpperCase() + str.substring(1);
		}
	}

	/**
	 * <P>替换掉HTML标签方法</P>
	 * 
	 * @param html 
	 * @return 文本字符串
	 */
	public static String replaceHtml(String html) {
		if (isBlank(html)){
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}

	/**
	 * <P>缩略字符串（不区分中英文字符）</P>
	 * @param str 目标字符串
	 * @param length 截取长度
	 * @return 
	 */
	public static String abbr(String str, int length) {
		if (str == null) {
			return "";
		}
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if (currentLength <= length - 3) {
					sb.append(c);
				} else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * <P>缩略字符串（替换html）</P>
	 * @param str 目标字符串
	 * @param length 截取长度
	 * @return
	 */
	public static String rabbr(String str, int length) {
        return abbr(replaceHtml(str), length);
	}
		
	
	/**
	 * <P>转换为Double类型</P>
	 * 
	 * @param val 目标对象
	 * @return Double
	 */
	public static Double toDouble(Object val){
		if (val == null){
			return 0D;
		}
		try {
			return Double.valueOf(trim(val.toString()));
		} catch (Exception e) {
			return 0D;
		}
	}

	/**
	 * <P>转换为Float类型</P>
	 * 
	 * @param val 目标对象
	 * @return Float
	 */
	public static Float toFloat(Object val){
		return toDouble(val).floatValue();
	}

	/**
	 * <P>转换为Long类型</P>
	 * 
	 * @param val 目标对象
	 * @return Long
	 */
	public static Long toLong(Object val){
		return toDouble(val).longValue();
	}

	/**
	 * <P>转换为Integer类型</P>
	 * 
	 * @param val 目标对象
	 * @return Integer
	 */
	public static Integer toInteger(Object val){
		return toLong(val).intValue();
	}
	
	/**
	 * <P>获得i18n字符串，本地化字符串</P>
	 * 
	 * @param code 目标字符串
	 * @param args 目标字符串参数
	 * @return 
	 */
	public static String getMessage(String code, Object[] args) {
		LocaleResolver localLocaleResolver = SpringContextHolder.getBean(LocaleResolver.class);
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
		Locale localLocale = localLocaleResolver.resolveLocale(request);
		return SpringContextHolder.getApplicationContext().getMessage(code, args, localLocale);
	}
	
	/**
	 * <P>获得用户远程地址</P>
	 * 
	 * @param request 用户请求
	 * @return 用户远程地址
	 */
	public static String getRemoteAddr(HttpServletRequest request){
		String remoteAddr = request.getHeader("X-Real-IP");
        if (isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("X-Forwarded-For");
        }else if (isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("Proxy-Client-IP");
        }else if (isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}
	 
	  /**
	     * 获取工程路径
	     * @return
	     */
	    public static String getProjectPath(){
			String projectPath = "";
			try {
				File file = new DefaultResourceLoader().getResource("").getFile();
				if (file != null){
					while(true){
						File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
						if (f == null || f.exists()){
							break;
						}
						if (file.getParentFile() != null){
							file = file.getParentFile();
						}else{
							break;
						}
					}
					projectPath = file.toString();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return projectPath;
	    }
	    /**
		 * 驼峰命名法工具
		 * @return
		 * 		toCamelCase("hello_world") == "helloWorld" 
		 * 		toCapitalizeCamelCase("hello_world") == "HelloWorld"
		 * 		toUnderScoreCase("helloWorld") = "hello_world"
		 */
	    public static String toCapitalizeCamelCase(String s) {
	        if (s == null) {
	            return null;
	        }
	        s = toCamelCase(s);
	        return s.substring(0, 1).toUpperCase() + s.substring(1);
	    }
	    
	    /**
		 * 驼峰命名法工具
		 * @return
		 * 		toCamelCase("hello_world") == "helloWorld" 
		 * 		toCapitalizeCamelCase("hello_world") == "HelloWorld"
		 * 		toUnderScoreCase("helloWorld") = "hello_world"
		 */
	    public static String toUnderScoreCase(String s) {
	        if (s == null) {
	            return null;
	        }

	        StringBuilder sb = new StringBuilder();
	        boolean upperCase = false;
	        for (int i = 0; i < s.length(); i++) {
	            char c = s.charAt(i);

	            boolean nextUpperCase = true;

	            if (i < (s.length() - 1)) {
	                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
	            }

	            if ((i > 0) && Character.isUpperCase(c)) {
	                if (!upperCase || !nextUpperCase) {
	                    sb.append(SEPARATOR);
	                }
	                upperCase = true;
	            } else {
	                upperCase = false;
	            }

	            sb.append(Character.toLowerCase(c));
	        }

	        return sb.toString();
	    }
	    /**
		 * 驼峰命名法工具
		 * @return
		 * 		toCamelCase("hello_world") == "helloWorld" 
		 * 		toCapitalizeCamelCase("hello_world") == "HelloWorld"
		 * 		toUnderScoreCase("helloWorld") = "hello_world"
		 */
	    public static String toCamelCase(String s) {
	        if (s == null) {
	            return null;
	        }

	        s = s.toLowerCase();

	        StringBuilder sb = new StringBuilder(s.length());
	        boolean upperCase = false;
	        for (int i = 0; i < s.length(); i++) {
	            char c = s.charAt(i);

	            if (c == SEPARATOR) {
	                upperCase = true;
	            } else if (upperCase) {
	                sb.append(Character.toUpperCase(c));
	                upperCase = false;
	            } else {
	                sb.append(c);
	            }
	        }

	        return sb.toString();
	    }
	    public static Object depthClone(Object srcObj){ 
	    	 Object cloneObj = null; 
	    	 try { 
	    	 ByteArrayOutputStream out = new ByteArrayOutputStream(); 
	    	 ObjectOutputStream oo = new ObjectOutputStream(out); 
	    	 oo.writeObject(srcObj); 
	    	 
	    	 ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray()); 
	    	 ObjectInputStream oi = new ObjectInputStream(in); 
	    	 cloneObj = oi.readObject(); 
	    	 } catch (IOException e) { 
	    	 e.printStackTrace(); 
	    	 } catch (ClassNotFoundException e) { 
	    	 e.printStackTrace(); 
	    	 } 
	    	 return cloneObj; 
	    	 }
	    
	    /**
		 * 去掉字符串前面的0
		 * @return
		 * 		delStringFrontZero("00000132000414") == "132000414" 
		 */
	    public static String delStringFrontZero(String srcObj){ 
		    String newStr = srcObj.replaceAll("^(0+)", "");
	    	return newStr; 
   	 	}
}
