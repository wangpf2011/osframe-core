/**
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wf.ssm.core.sys.interceptor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wf.ssm.common.service.BaseService;
import com.wf.ssm.common.utils.IdGen;
import com.wf.ssm.common.utils.SpringContextHolder;
import com.wf.ssm.common.utils.StringUtils;

/**
 * 系统拦截器(拦截用户输入中的特殊字符)
 * @version 2015-9-1
 */
public class CharacterInterceptor extends BaseService implements HandlerInterceptor {
	
//	private static ExpMonitorDao expMonitorDao = SpringContextHolder.getBean(ExpMonitorDao.class);
	
	@Override
	//Action之前执行:可以进行编码、安全控制等处理； 
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
           
			int num=request.getParameterMap().keySet().size();
			int i=0;
			//判断请求的url是否是白名单URL，准许放行
			//加入lsp缓存中最好
//			List<SysParameter> lsp= sysParameterService.queryParamsValList("url_white_list");
//			for(SysParameter el:lsp){
//				//遍历白名单是否包含
//				String whiteUrl= el.getParaValue();
//				if(request.getRequestURI().contains(whiteUrl)){//符合任何一个白名单，则不过滤
//					return true;
//				}
//			}
			for (Object param : request.getParameterMap().keySet()){ 
				String paramvalue=request.getParameter((String)param);
				if(filterDangerString(paramvalue)){
					 request.getRequestDispatcher("/WEB-INF/views/error/401.jsp").forward(request, response);  
//					 //保存到系统异常信息
//					 ExpMonitor expMonitor =new ExpMonitor();
//					 expMonitor.setExpNo(IdGen.getRandomWithZero(8));
//					 expMonitor.setType("03");//sms频繁 
//					 expMonitor.setUrlAddr(request.getRequestURI() );
//					 expMonitor.setIpAddr(StringUtils.getRemoteAddr(request));
//					 expMonitor.setContent("用户输入非法字符");
//					 expMonitorDao.save(expMonitor);
//					 expMonitorDao.flush();
//					 sysParameterService.saveExp(request);
					 break;
			    }
				i++;
			}
			if(i==num){
				return true;
			}else{
				return false;
			}
	}
	public boolean filterDangerString(String qString) {
		//qString = decode( qString );  
		qString = qString.trim().toLowerCase();  
		
	        if ( qString.contains( "javascript:" )) {  
	            return true;  
	        }  
	        if ( qString.contains( "mocha:" )) {  
	            return true;  
	        }  
	        if ( qString.contains( "eval" )) {  
	            return true;  
	        }  
	        if ( qString.contains( "vbscript:" )) {  
	            return true;  
	        }  
	        if ( qString.contains( "livescript:" )) {  
	            return true;  
	        }  
	        if ( qString.contains( "expression(" )) {  
	            return true;  
	        }  
	        if ( qString.contains( "url(" )) {  
	            return true;  
	        }  
	        if ( qString.contains( "&{" )) {  
	            return true;  
	        }  
	        if ( qString.contains( "&#" )) {  
	            return true;  
	        }  
//	        String regx="(\\|)|(\\&)|;|(\\$)|%|'|(\\()|(\\))|(\\+)|<|>|(\\\\)";
	        String regx="(\\|)|(\\&)|;|(\\$)|%|'|(\\+)|<|>|(\\\\)";
			Pattern p = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(qString);
			return m.find();
    }
private String decode( String value ) {  
    value = value.replace("\u0000", "" );  
    value = value.replace("\u0001", "" );  
    value = value.replace("\u0002", "" );  
    value = value.replace("\u0003", "" );  
    value = value.replace("\u0004", "" );  
    value = value.replace("\u0005", "" );  
    value = value.replace("\u0006", "" );  
    value = value.replace("\u0007", "" );  
    value = value.replace("\u0008", "" );  
    value = value.replace("\u0009", "" );  
    value = value.replace("\n", "" );  
    value = value.replace("\u000B", "" );  
    value = value.replace("\u000C", "" );  
    value = value.replace("\r", "" );  
    value = value.replace("\u000E", "" );  
    value = value.replace("\u000F", "" );  
    value = value.replace("\u0010", "" );  
    value = value.replace("\u0011", "" );  
    value = value.replace("\u0012", "" );  
    value = value.replace("\u0013", "" );  
    value = value.replace("\u0014", "" );  
    value = value.replace("\u0015", "" );  
    value = value.replace("\u0016", "" );  
    value = value.replace("\u0017", "" );  
    value = value.replace("\u0018", "" );  
    value = value.replace("\u0019", "" );  
    value = value.replace("\u001A", "" );  
    value = value.replace("\u001B", "" );  
    value = value.replace("\u001C", "" );  
    value = value.replace("\u001D", "" );  
    value = value.replace("\u001E", "" );  
    value = value.replace("\u001F", "" );  
    return value;  
}  
	@Override
	//生成视图之前执行 有机会修改ModelAndView； 
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, 
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	@Transactional(readOnly = false)
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
			Object handler, Exception ex) throws Exception {
		}
}
