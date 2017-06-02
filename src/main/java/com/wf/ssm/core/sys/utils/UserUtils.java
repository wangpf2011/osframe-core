/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 位苏 2015-3-11 12:02:25
 */
package com.wf.ssm.core.sys.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.subject.Subject;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.google.common.collect.Maps;
import com.wf.ssm.common.service.BaseService;
import com.wf.ssm.common.utils.CacheUtils;
import com.wf.ssm.common.utils.SpringContextHolder;
import com.wf.ssm.core.sys.dao.AreaDao;
import com.wf.ssm.core.sys.dao.MenuDao;
import com.wf.ssm.core.sys.dao.OfficeDao;
import com.wf.ssm.core.sys.dao.RoleDao;
import com.wf.ssm.core.sys.dao.SysSequencesDao;
import com.wf.ssm.core.sys.dao.UserDao;
import com.wf.ssm.core.sys.entity.Area;
import com.wf.ssm.core.sys.entity.Menu;
import com.wf.ssm.core.sys.entity.Office;
import com.wf.ssm.core.sys.entity.Role;
import com.wf.ssm.core.sys.entity.SysSequences;
import com.wf.ssm.core.sys.entity.User;
import com.wf.ssm.core.sys.entity.UserRole;
import com.wf.ssm.core.sys.security.SystemAuthorizingRealm.Principal;

/**
 * 用户工具类
 * @version 1.0
 * @author weisu 2015-3-10
 * @since JDK 1.6
 */
public class UserUtils extends BaseService {

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);
	private static SysSequencesDao sysSequencesDao = SpringContextHolder.getBean(SysSequencesDao.class);
	
	
	public static final String USER_CACHE = "userCache";
	public static final String USER_CACHE_ID_ = "id_";
	public static final String USER_CACHE_LOGIN_NAME_ = "ln";
	public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";
	
	public static final String CACHE_USER = "user";
	public static final String CACHE_ROLE_LIST = "roleList";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_OFFICE_LIST = "officeList";

	/**
	 * 获得当前登录用户
	 */
	public static User getUser() {
		User user = (User) getCache(CACHE_USER);
		if (user == null) {
			try {
				Subject subject = SecurityUtils.getSubject();
				Principal principal = (Principal) subject.getPrincipal();
				if (principal != null) {
					user = userDao.get(principal.getId());
					// Hibernate.initialize(user.getRoleList());
					putCache(CACHE_USER, user);
				}
			} catch (UnavailableSecurityManagerException e) {

			} catch (InvalidSessionException e) {

			}
		}
		if (user == null) {
			user = new User();
			try {
				SecurityUtils.getSubject().logout();
			} catch (UnavailableSecurityManagerException e) {

			} catch (InvalidSessionException e) {

			}
		}
		return user;
	}
	public static User getByLoginName(String loginName){
		if (StringUtils.isNotBlank(loginName)) {
			return userDao.findByLoginName(loginName);
		} else {
			return null;
		}
	}
	/**
	 * 返回当前用户，并可以根据参数，决定是否刷新缓存信息
	 */
	public static User getUser(boolean isRefresh) {
		if (isRefresh) {
			removeCache(CACHE_USER);
		}
		return getUser();
	}
	/**
	 * 获取当前登录者对象
	 */
	public static Principal getPrincipal(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal)subject.getPrincipal();
			if (principal != null){
				return principal;
			}
//			subject.logout();
		}catch (UnavailableSecurityManagerException e) {
			
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	/**
	 * 获取当前用户角色
	 */
	public static List<Role> getRoleList() {
		@SuppressWarnings("unchecked")
		List<Role> list = (List<Role>) getCache(CACHE_ROLE_LIST);
		if (list == null) {
			User user = getUser();
			DetachedCriteria dc = roleDao.createDetachedCriteria();
			dc.createAlias("office", "office");
//			dc.createAlias("userList", "userList", JoinType.LEFT_OUTER_JOIN);
//			dc.add(dataScopeFilter(user, "office", "userList"));
			dc.add(dataScopeFilter(user, "office", null));
			dc.add(Restrictions.eq(Role.FIELD_DEL_FLAG, Role.DEL_FLAG_NORMAL));
			dc.addOrder(Order.asc("office.code")).addOrder(Order.asc("name"));
			list = roleDao.find(dc);
			putCache(CACHE_ROLE_LIST, list);
		}
		return list;
	}

	/**
	 * 判断当前用户是否有某个角色
	 */
	public static boolean hasRole(String roleId) {
		if (StringUtils.isEmpty(roleId))
			return false;

		User user = getUser();
		List<UserRole> list = roleDao.findRoles(user.getId());
		for (UserRole role : list) {
			if (roleId.equals(role.getRole_id()))
				return true;
		}

		return false;
	}

	/**
	 * 获取当前用户的菜单列表
	 */
	public static List<Menu> getMenuList() {
		@SuppressWarnings("unchecked")
		List<Menu> menuList = (List<Menu>) getCache(CACHE_MENU_LIST);
		if (menuList == null) {
			User user = getUser();
			if (user.isAdmin()) {
				menuList = menuDao.findAllList();
			} else {
				menuList = menuDao.findByUserId(user.getId());
			}
			putCache(CACHE_MENU_LIST, menuList);
		}
		return menuList;
	}

	/**
	 * 获取所有区域信息
	 */
	public static List<Area> getAreaList() {
		@SuppressWarnings("unchecked")
		List<Area> areaList = (List<Area>) getCache(CACHE_AREA_LIST);
		if (areaList == null) {
			// User user = getUser();
			// if (user.isAdmin()){
			areaList = areaDao.findAllList();
			// }else{
			// areaList = areaDao.findAllChild(user.getArea().getId(),
			// "%,"+user.getArea().getId()+",%");
			// }
			putCache(CACHE_AREA_LIST, areaList);
		}
		return areaList;
	}
	
	/**
	 * 查询机构信息，根据当前用户过滤
	 */
	public static List<Office> getOfficeList() {
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>) getCache(CACHE_OFFICE_LIST);
		if (officeList == null) {
			User user = getUser();
			// if (user.isAdmin()){
			// officeList = officeDao.findAllList();
			// }else{
			// officeList = officeDao.findAllChild(user.getOffice().getId(),
			// "%,"+user.getOffice().getId()+",%");
			// }
			DetachedCriteria dc = officeDao.createDetachedCriteria();
			dc.add(dataScopeFilter(user, dc.getAlias(), ""));
			dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
			dc.addOrder(Order.asc("code"));
			officeList = officeDao.find(dc);
			putCache(CACHE_OFFICE_LIST, officeList);
		}
		return officeList;
	}
	/**
	 * 查询机构信息，根据当前用户过滤
	 */
	public static List<Office> getOfficeListByCorp() {	
	List<Office> officeList = new ArrayList<Office>() ;
		
		User user = getUser();
		 if (user.isAdmin()){
			 officeList =(List<Office>) getCache(CACHE_OFFICE_LIST);
		 }else{
				DetachedCriteria dc = officeDao.createDetachedCriteria();
//				dc.add(dataScopeFilter(user, dc.getAlias(), ""));
				dc.createAlias("createBy", "user");
				dc.add(Restrictions.eq("user.id", user.getId()));//本用户自己的数据
				dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
				dc.addOrder(Order.asc("code"));
				officeList = officeDao.find(dc);
		 }
		return officeList;
	}
	//
	/**
	 * 查询用户所在的运营商 建立的 机构信息机构信息，根据当前用户过滤
	 */
	public static List<Office> getOfficeListByOnwerCorp() {
//		
		
		List<Office> officeList = new ArrayList<Office>() ;
		
		User user = getUser();
		 if (user.isAdmin()){
			 officeList=getOfficeList();
//			 officeList =(List<Office>) getCache(CACHE_OFFICE_LIST);
		 }else{
			 String  cor_off_id="";
				//获得用户所在的offiid
				Office curOfff=user.getOffice();
				//获得本用户的
				if(curOfff.getType().equals("2")){//此用户直接挂在运营商下面
					//此用户所属的运营商officeid 
					cor_off_id=curOfff.getId();
				}else{
					//此用户所属的运营商officeid 为数组的第三个数据 
					cor_off_id=curOfff.getParentIds().split(",")[2];
				}
				
				//根据得到的运营商offid 获得此运营商下的所有的 office孩子信息
				 officeList  = officeDao.findByParentIdsLike(cor_off_id);
		 }
		
			
		return officeList;
	}
	/**
	 * 查询用户根据当前用户过滤出用户本级别及其下级别所有的组织机构数据
	 */
	public static List<Office> findChildOfficeListByCorp() {
//		
		List<Office> officeList = new ArrayList<Office>() ;
		User user = getUser();
//		 if (user.isAdmin()){
////			 officeList =(List<Office>) getCache(CACHE_OFFICE_LIST);
//		 }else{
			 String  cor_off_id="";
				//获得用户所在的offiid
				Office curOfff=user.getOffice();
				cor_off_id=curOfff.getId();
				//获得本用户的
//				if(curOfff.getType().equals("2")){//此用户直接挂在运营商下面
//					//此用户所属的运营商officeid 
//					cor_off_id=curOfff.getId();
//				}else{
//					//此用户所属的运营商officeid 为数组的第三个数据 
//					cor_off_id=curOfff.getParentIds().split(",")[2];
//				}
				//根据得到的运营商offid 获得此运营商下的所有的 office孩子信息
				 officeList  = officeDao.findByParentIdsLike(cor_off_id);
//		 }
		return officeList;
	}
	/**
	 * 获得所有的组织机构
	 */
	public static List<Office> getOfficeListAll() {
//		@SuppressWarnings("unchecked")
		List<Office> officeList = new ArrayList<Office>();
//		User user = getUser();
		// if (user.isAdmin()){
		// officeList = officeDao.findAllList();
		// }else{
		// officeList = officeDao.findAllChild(user.getOffice().getId(),
		// "%,"+user.getOffice().getId()+",%");
		// }
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		// dc.add(dataScopeFilter(user, dc.getAlias(), ""));
		dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("code"));
		officeList = officeDao.find(dc);
		// putCache(CACHE_OFFICE_LIST, officeList);
		return officeList;
	}

	/**
	 * 获得所有用户信息
	 */
	public static List<User> getUserList() {
//		@SuppressWarnings("unchecked")
		List<User> userList = new ArrayList<User>();
		DetachedCriteria dc = userDao.createDetachedCriteria();
		dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		userList = userDao.find(dc);
		return userList;
	}

	/**
	 * 根据id获取用户信息
	 */
	public static User getUserById(String id) {
		if (StringUtils.isNotBlank(id)) {
			return userDao.get(id);
		} else {
			return null;
		}
	}

	/**
	 * 获取所有区域信息
	 */
	public static List<Area> getDownAreaList() {
		@SuppressWarnings("unchecked")
		List<Area> areaList = (List<Area>) getCache(CACHE_AREA_LIST);
		if (areaList == null) {
			 User user = getUser();
//			 if (user.isAdmin()){
//				 areaList = areaDao.findAllList();
//			 }else{
			 areaList = areaDao.findAllChild(user.getOffice().getArea().getId(),
			 "%,"+user.getOffice().getArea().getId()+",%");
//			 }
			putCache(CACHE_AREA_LIST, areaList);
		}
		return areaList;
	}
	
	/**
	 * 清除指定用户缓存
	 * @param user
	 */
	public static void clearCache(User user){
		CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName());
		if (user.getOffice() != null && user.getOffice().getId() != null){
			CacheUtils.remove(USER_CACHE, USER_CACHE_LIST_BY_OFFICE_ID_ + user.getOffice().getId());
		}
	}
	// ============== User Cache ==============

	public static Object getCache(String key) {
		return getCache(key, null);
	}

	public static Object getCache(String key, Object defaultValue) {
		Object obj = getCacheMap().get(key);
		return obj == null ? defaultValue : obj;
	}

	public static void putCache(String key, Object value) {
		getCacheMap().put(key, value);
	}

	public static void removeCache(String key) {
		getCacheMap().remove(key);
	}

	public static Map<String, Object> getCacheMap() {
		Map<String, Object> map = Maps.newHashMap();
		try {
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal) subject.getPrincipal();
			return principal != null ? principal.getCacheMap() : map;
		} catch (UnavailableSecurityManagerException e) {

		} catch (InvalidSessionException e) {

		}
		return map;
	}
	/**
	 * <P>根据序列名称获得序列值</P>
	 * @param seqName 自定义序列名称
	 * @return String 生成的下一个序列值
	 */
	public static String findNextSeqByName(String seqName){
		//根据序列名称查询序列实体
		String ret="";
		SysSequences sysSequences=sysSequencesDao.findSeqByName(seqName);
		//拼接序列格式化输出
		Long curval =sysSequences.getSeqVal();//当前序列值
		String seq ="0" ; 
		if(StringUtils.isNotBlank(sysSequences.getSeqWidth())){// 补充位数
			seq=String.format("%0"+sysSequences.getSeqWidth()+"d", curval);
		}else{
			seq=String.valueOf(curval);
		}
		String left ="" ; 
		if(StringUtils.isNotBlank(sysSequences.getLeftpad())){// 左前缀
			left=sysSequences.getLeftpad();
		}
		String right ="" ; 
		if(StringUtils.isNotBlank(sysSequences.getRightpad())){// 左前缀
			right=sysSequences.getRightpad();
		}
		ret=left+seq+right;
		//更新序列值
		sysSequences.setSeqVal((curval+1));
//		sysSequencesDao.clear();
		sysSequencesDao.save(sysSequences);
		sysSequencesDao.flush();
		return ret;
	}
}
