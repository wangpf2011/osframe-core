/**
 * Copyright &copy; 2011-2020 lnint Inc. All rights reserved.
 * 
 * 修改信息：v1.0 doc做成
 * A: 新增类  葛松  2015-03-11 15:20:21
 */
package com.wf.ssm.core.sys.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Maps;
import com.wf.ssm.common.config.Global;
import com.wf.ssm.common.utils.CacheUtils;
import com.wf.ssm.common.utils.CookieUtils;
import com.wf.ssm.common.utils.StringUtils;
import com.wf.ssm.common.web.BaseController;
import com.wf.ssm.core.sys.entity.User;
import com.wf.ssm.core.sys.security.SystemAuthorizingRealm.Principal;
import com.wf.ssm.core.sys.utils.UserUtils;

/**
 * <P>登录Controller</P>
 * 
 * @version 1.0
 * @author wangpf
 * @since JDK 1.6
 */
@Controller
public class LoginController extends BaseController{
	/**
	 * <p>管理登录<p>
	 * @param request
	 * @param response
	 * @param model 模型
	 * @return 已登录：视图名称"redirect:"+Global.getAdminPath(); 未登录；视图名称"core/sys/sysLogin"
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		// 如果已经登录，则跳转到管理首页
		if(user.getId() != null){
			return "redirect:"+Global.getAdminPath();
		}
		return "core/sys/sysLogin";
	}

	/**
	 * <p>登录失败，真正登录的POST请求由Filter完成<p>
	 * @param username 用户名
	 * @param request
	 * @param response
	 * @param model 模型
	 * @return 已登录：视图名称"redirect:"+Global.getAdminPath(); 未登录；视图名称"core/sys/sysLogin"
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.POST)
	public String login(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String username, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		// 如果已经登录，则跳转到管理首页
		if(user.getId() != null){
			return "redirect:"+Global.getAdminPath();
		}
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
		return "core/sys/sysLogin";
	}

	/**
	 * <p>登录成功，进入管理首页<p>
	 * @param request
	 * @param response
	 * @return @return 已登录："core/sys/sysIndex"; 未登录；视图名称"redirect:"+Global.getAdminPath()+"/login"
	 */
	@RequiresUser
	@RequestMapping(value = "${adminPath}")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		Principal principal = UserUtils.getPrincipal();
		User user = UserUtils.getUser();
		// 未登录，则跳转到登录页
		if(user.getId() == null){
			return "redirect:"+Global.getAdminPath()+"/login";
		}
		// 0:如果是手机APP用户---不能登陆pcweb网站，用户退出，返回
//		if(user.getRoleIdList() !=null && user.getRoleIdList().size()>0  && user.getRoleIdList().get(0).equals("12")){//如果是手机用户
//			//判断logout
//			SecurityUtils.getSubject().logout();
//			return "error/403";
//		}
		
		//登陆信息-鉴权
		
		// 99：如果是手机登录，则返回JSON字符串-废弃
//		if (principal.isMobileLogin()){
//			if (request.getParameter("login") != null){
//				return renderString(response, principal);
//			}
//			if (request.getParameter("index") != null){
//				return "modules/sys/sysIndex";
//			}
//			return "redirect:" + adminPath + "/login";
//		}
		
		// 登录成功后，验证码计算器清零
		isValidateCodeLogin(user.getLoginName(), false, true);
		// 登录成功后，获取上次登录的当前站点ID
		UserUtils.putCache("siteId", CookieUtils.getCookie(request, "siteId"));
		
		//登陆https请求 重定向为http协议..
		String retURL="redirect:"+Global.getAdminPath()+"/myservices.html";
		String NEW_SESSION_INDICATOR = "com.lnint.jess.common.filter.NewSessionFilter";
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			
			//取的url相对地址
	        String url = httpRequest.getRequestURI();  
	        if (httpRequest.getSession() != null) {
				//--------复制 session到临时变量
				HttpSession session = httpRequest.getSession();
				HashMap old = new HashMap();
				Enumeration keys = (Enumeration) session.getAttributeNames();
				
				while (keys.hasMoreElements()){
					String key = (String) keys.nextElement();
					if (!NEW_SESSION_INDICATOR.equals(key)){
						old.put(key, session.getAttribute(key));
						session.removeAttribute(key);
					}
				}
				
				if (httpRequest.getMethod().equals("GET") && httpRequest.getSession() != null 
						&& !httpRequest.getSession().isNew() && httpRequest.getRequestURI().endsWith(url)){
					SecurityUtils.getSubject().logout(); 
					session=httpRequest.getSession(true);
				}
				//-----------------复制session
				for (Iterator it = old.entrySet().iterator(); it.hasNext();) {
					Map.Entry entry = (Entry) it.next();
					session.setAttribute((String) entry.getKey(), entry.getValue());
				}
			}
		}
		
		if(request.getServerPort() == 8443){//登陆为https请求，则重定向为http协议
			String port="8080";
			String web_http_server_port =  Global.getConfig("web_http_server_port");
	    	if(web_http_server_port!=null && StringUtils.isNotEmpty(web_http_server_port)){
	    		port=web_http_server_port;
	    	}
	        Cookie cookie = new Cookie("JSESSIONID", request.getSession().getId());  
	        response.addCookie(cookie); 
	        retURL="redirect:http://"+request.getServerName()+":"+port+request.getContextPath()+"/"+Global.getAdminPath()+"/myservices.html";
		}
		//返回http主页
		return  retURL;
	}
	//重定向为http协议 欢迎主页
	@RequiresUser
	@RequestMapping(value = "${adminPath}/myservices.html")
	public String httpIndex(HttpServletRequest request, HttpServletResponse response) {
		return "core/sys/sysIndex";
	}
	
	/**
	 * <p>获取主题方案<p>
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response){
		if (StringUtils.isNotBlank(theme)){
			CookieUtils.setCookie(response, "theme", theme);
		}else{
			theme = CookieUtils.getCookie(request, "theme");
		}
		return "redirect:"+request.getParameter("url");
	}
	
	/**
	 * <p>是否是验证码登录<p>
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return 登陆失败次数是否大于三次
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean){
		Map<String, Integer> loginFailMap = (Map<String, Integer>)CacheUtils.get("loginFailMap");
		if (loginFailMap==null){
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum==null){
			loginFailNum = 0;
		}
		if (isFail){
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean){
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 3;
	}
	
	/**
	 * <p>下载<p>
	 * @param filePath 文件路径
	 * @param response 响应下载
	 * @return null
	 */
	@RequestMapping("${adminPath}/download")
	public String download(@RequestParam String filePath,HttpServletResponse response) {
//		File file = new File(filePath);
//		InputStream inputStream = null;
//		try {
//			inputStream = new FileInputStream(filePath);
//			response.reset();
//			response.setContentType("application/octet-stream;charset=UTF-8");
//			response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
//			OutputStream outputStream = new BufferedOutputStream(
//					response.getOutputStream());
//			byte data[] = new byte[1024];
//			while (inputStream.read(data, 0, 1024) >= 0) {
//				outputStream.write(data);
//			}
//			outputStream.flush();
//			outputStream.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				inputStream.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		return null;
	}
}
