/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 位苏 2015-3-11 12:02:25
 */
package com.wf.ssm.core.sys.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.wf.ssm.common.servlet.ValidateCodeServlet;
import com.wf.ssm.common.utils.Encodes;
import com.wf.ssm.common.utils.SpringContextHolder;
import com.wf.ssm.common.utils.StringUtils;
import com.wf.ssm.core.sys.entity.Menu;
import com.wf.ssm.core.sys.entity.User;
import com.wf.ssm.core.sys.service.SystemService;
import com.wf.ssm.core.sys.utils.UserUtils;
import com.wf.ssm.core.sys.web.LoginController;

/**
 * <P>系统安全认证实现类</P>
 * @version 1.0
 * @author weisu 2015-3-10
 * @since JDK 1.6
 */
@Service
@DependsOn({"userDao","roleDao","menuDao"})
public class SystemAuthorizingRealm extends AuthorizingRealm {

	private SystemService systemService;

	/**
	 * <P>认证回调函数, 登录时调用</P>
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		
// 		if (LoginController.isValidateCodeLogin(token.getUsername(), false, false)){
//			// 判断验证码
//			Session session = SecurityUtils.getSubject().getSession();
//			String code = (String)session.getAttribute(ValidateCodeServlet.VALIDATE_CODE);
//			if (token.getCaptcha() == null || !token.getCaptcha().toUpperCase().equals(code)){
//				throw new CaptchaException("验证码错误.");
//			}
//		}

		User user = getSystemService().getUserByLoginName(token.getUsername());
		if (user != null) {
			byte[] salt = Encodes.decodeHex(user.getPassword().substring(0,16));
			return new SimpleAuthenticationInfo(new Principal(user), 
					user.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());
		} else {
			return null;
		}
	}

	/**
	 * <P>授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用</P>
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Principal principal = (Principal) getAvailablePrincipal(principals);
		User user = getSystemService().getUserByLoginName(principal.getLoginName());
		if (user != null) {
			UserUtils.putCache("user", user);
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			List<Menu> list = UserUtils.getMenuList();
			for (Menu menu : list){
				if (StringUtils.isNotBlank(menu.getPermission())){
					// 添加基于Permission的权限信息
					for (String permission : StringUtils.split(menu.getPermission(),",")){
						info.addStringPermission(permission);
					}
				}
			}
			// 更新登录IP和时间
			getSystemService().updateUserLoginInfo(user.getId());
			return info;
		} else {
			return null;
		}
	}
	
	/**
	 * <P>设定密码校验的Hash算法与迭代次数</P>
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(SystemService.HASH_ALGORITHM);
		matcher.setHashIterations(SystemService.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}
	
	/**
	 * <P>清空用户关联权限认证，待下次使用时重新加载</P>
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * <P>清空所有关联认证</P>
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

	/**
	 * <P>获取系统业务对象</P>
	 */
	public SystemService getSystemService() {
		if (systemService == null){
			systemService = SpringContextHolder.getBean(SystemService.class);
		}
		return systemService;
	}
	
	/**
	 * <P>授权用户信息</P>
	 */
	public static class Principal implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String id;
		private String loginName;
		private String name;
		private String depart;// 所在上级部门-
		private Map<String, Object> cacheMap;
		private boolean mobileLogin; // 是否手机登录
		
		public Principal(User user, boolean mobileLogin) {
			this.id = user.getId();
			this.loginName = user.getLoginName();
			this.name = user.getName();
			this.mobileLogin = mobileLogin;
		}
		
		public Principal(User user) {
			this.id = user.getId();
			this.loginName = user.getLoginName();
			this.name = user.getName();
			this.depart = user.getOffice().getName();
			
		}
		public boolean isMobileLogin() {
			return mobileLogin;
		}
		public String getId() {
			return id;
		}

		public String getLoginName() {
			return loginName;
		}

		public String getName() {
			return name;
		}
		public String getDepart() {
			return depart;
		}
		
		public Map<String, Object> getCacheMap() {
			if (cacheMap==null){
				cacheMap = new HashMap<String, Object>();
			}
			return cacheMap;
		}

	}
}
