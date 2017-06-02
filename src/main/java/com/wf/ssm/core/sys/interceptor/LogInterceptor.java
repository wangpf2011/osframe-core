/**
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wf.ssm.core.sys.interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.NamedThreadLocal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wf.ssm.common.config.Global;
import com.wf.ssm.common.service.BaseService;
import com.wf.ssm.common.utils.SpringContextHolder;
import com.wf.ssm.common.utils.StringUtils;
import com.wf.ssm.core.sys.dao.LogDao;
import com.wf.ssm.core.sys.dao.MenuDao;
import com.wf.ssm.core.sys.entity.Log;
import com.wf.ssm.core.sys.entity.Menu;
import com.wf.ssm.core.sys.entity.User;
import com.wf.ssm.core.sys.utils.UserUtils;

/**
 * 系统拦截器
 * @version 2013-6-6
 */
public class LogInterceptor extends BaseService implements HandlerInterceptor {

	private static LogDao logDao = SpringContextHolder.getBean(LogDao.class);
	
	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
	
	private static final ThreadLocal<Long> startTimeThreadLocal =
			new NamedThreadLocal<Long>("ThreadLocal StartTime");
	
	@Override
	//Action之前执行:可以进行编码、安全控制等处理； 
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
		if (logger.isDebugEnabled()){
			long beginTime = System.currentTimeMillis();//1、开始时间  
	        startTimeThreadLocal.set(beginTime);		//线程绑定变量（该数据只有当前请求的线程可见）  
	        logger.debug("开始计时: {}  URI: {}", new SimpleDateFormat("hh:mm:ss.SSS")
	        	.format(beginTime), request.getRequestURI());
		}
		return true;
	}

	@Override
	//生成视图之前执行 有机会修改ModelAndView； 
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, 
			ModelAndView modelAndView) throws Exception {
		if(modelAndView!=null) {
			/*String viewName = modelAndView.getViewName();
			UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent")); 
			if(viewName.startsWith("modules/") && DeviceType.MOBILE.equals(userAgent.getOperatingSystem().getDeviceType())){
//				modelAndView.setViewName(viewName.replaceFirst("modules", "mobile"));
			}*/
		}
	}

	@Override
	@Transactional(readOnly = false)
	//最后执行，可用于释放资源 可以根据ex是否为null判断是否发生了异常，进行日志记录。 
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
			Object handler, Exception ex) throws Exception {
		
		String requestRri = request.getRequestURI();
		String uriPrefix = request.getContextPath() + Global.getAdminPath();
		
		if ((StringUtils.startsWith(requestRri, uriPrefix) && (StringUtils.endsWith(requestRri, "/save")
				|| StringUtils.endsWith(requestRri, "/delete") || StringUtils.endsWith(requestRri, "/import")
				|| StringUtils.endsWith(requestRri, "/updateSort"))) || ex!=null){///根据执行url级别，记录关键业务增删改log
		
			User user = UserUtils.getUser();
			if (user!=null && user.getId()!=null) {
				StringBuilder params = new StringBuilder();
				int index = 0;
				for (Object param : request.getParameterMap().keySet()){ 
					params.append((index++ == 0 ? "" : "&") + param + "=");
					params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase((String)param, "password")
							? "" : request.getParameter((String)param), 100));
				}
				
				//记录日志信息-LOG
				Log log = new Log();
				if(ex != null) {
				    log.setType(Log.TYPE_EXCEPTION);
				}else if(StringUtils.isEmpty((String)request.getAttribute("message"))) {
				    log.setType(Log.TYPE_SAVE);
				}else {
				    log.setType(Log.TYPE_ACCESS);
				}
				log.setCreateBy(user);
				log.setCreateDate(new Date());
				log.setRemoteAddr(StringUtils.getRemoteAddr(request));
				log.setUserAgent(request.getHeader("user-agent"));
				log.setRequestUri(request.getRequestURI());
				String requestURI = request.getRequestURI();
                if(requestURI.startsWith("/evms/a") || requestURI.startsWith("/evms/f")) {
                    requestURI = requestURI.substring(7, requestURI.length());
                    requestURI = requestURI.substring(0, requestURI.lastIndexOf("/"))+"/list";
                    Menu menu = menuDao.findByRequestURI(requestURI);
                    log.setRequestName(menu.getName());
                }
				log.setMethod(request.getMethod());
				log.setParams(params.toString());
				log.setException(ex != null ? ex.toString() : "");
				logDao.save(log);
				
				logger.info("save log {type: {}, loginName: {}, uri: {}}, ", log.getType(), user.getLoginName(), log.getRequestUri());
				
			}else{//非法登录-非法操作信息，需要报警和记录
				
			}
		}
		
		// 打印JVM信息。
		if (logger.isDebugEnabled()){
			long beginTime = startTimeThreadLocal.get();//得到线程绑定的局部变量（开始时间）  
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
	        logger.debug("计时结束：{}  耗时：{}  URI: {}  最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
	        		new SimpleDateFormat("hh:mm:ss.SSS").format(endTime), (endTime - beginTime),
					request.getRequestURI(), Runtime.getRuntime().maxMemory()/1024/1024, Runtime.getRuntime().totalMemory()/1024/1024, Runtime.getRuntime().freeMemory()/1024/1024, 
					(Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory())/1024/1024); 
		}
		
//		logger.debug("最大内存: {}, 已分配内存: {}, 已分配内存中的剩余空间: {}, 最大可用内存: {}", 
//				Runtime.getRuntime().maxMemory(), Runtime.getRuntime().totalMemory(), Runtime.getRuntime().freeMemory(), 
//				Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory()); 
		
	}

}
