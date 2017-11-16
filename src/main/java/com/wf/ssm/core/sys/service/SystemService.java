/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 位苏 2015-3-11 12:02:25
 */
package com.wf.ssm.core.sys.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.wf.ssm.common.config.Global;
import com.wf.ssm.common.persistence.Page;
import com.wf.ssm.common.security.Digests;
import com.wf.ssm.common.service.BaseService;
import com.wf.ssm.common.utils.Collections3;
import com.wf.ssm.common.utils.Encodes;
import com.wf.ssm.common.utils.StringUtils;
import com.wf.ssm.core.sys.dao.MenuDao;
import com.wf.ssm.core.sys.dao.RoleDao;
import com.wf.ssm.core.sys.dao.UserDao;
import com.wf.ssm.core.sys.entity.Menu;
import com.wf.ssm.core.sys.entity.Role;
import com.wf.ssm.core.sys.entity.User;
import com.wf.ssm.core.sys.security.SystemAuthorizingRealm;
import com.wf.ssm.core.sys.utils.UserUtils;


/**
 * <P>系统管理，安全相关实体的管理类,包括用户、角色、菜单.</P>
 * @version 1.0
 * @author wangpf 2015-3-10
 * @since JDK 1.6
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService  {
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private SystemAuthorizingRealm systemRealm;
	
//	@Autowired
//	private IdentityService identityService;

	//-- User Service --//
	
	public User getUser(String id) {
//		return userDao.get(id);
		return userDao.getPersistent(id);
	}
	//
	public Page<User> findUser(Page<User> page, User user) {
		DetachedCriteria dc = userDao.createDetachedCriteria();
		User currentUser = UserUtils.getUser();
		dc.createAlias("company", "company");
		if (user.getCompany()!=null && StringUtils.isNotBlank(user.getCompany().getId())){
			dc.add(Restrictions.or(
					Restrictions.eq("company.id", user.getCompany().getId()),
					Restrictions.like("company.parentIds", "%,"+user.getCompany().getId()+",%")
					));
		}
		dc.createAlias("office", "office");
		if (user.getOffice()!=null && StringUtils.isNotBlank(user.getOffice().getId())){
			dc.add(Restrictions.or(
					Restrictions.eq("office.id", user.getOffice().getId()),
					Restrictions.like("office.parentIds", "%,"+user.getOffice().getId()+",%")
					));
		}
		// 如果不是超级管理员，则不显示超级管理员用户
		if (!currentUser.isAdmin()){
			dc.add(Restrictions.ne("id", "1")); 
		}
		dc.add(dataScopeFilter(currentUser, "office", ""));
		//System.out.println(dataScopeFilterString(currentUser, "office", ""));
		if (StringUtils.isNotEmpty(user.getLoginName())){
			dc.add(Restrictions.like("loginName", "%"+user.getLoginName()+"%"));
		}
		if (StringUtils.isNotEmpty(user.getName())){
			dc.add(Restrictions.like("name", "%"+user.getName()+"%"));
		}
		dc.add(Restrictions.eq(User.FIELD_DEL_FLAG, User.DEL_FLAG_NORMAL));
		if (!StringUtils.isNotEmpty(page.getOrderBy())){
//			dc.addOrder(Order.asc("company.code")).addOrder(Order.asc("office.code")).addOrder(Order.desc("name"));
		}
		return userDao.find(page, dc);
	}
	/**
	 * <P>列表list查询函数-运营商查询子用户数据</P>
	 * @param cmsArticle 实体模型;
	 * @param request  请求;
	 * @param response 返回;
	 * @param model    视图模型;
	 * @return view URL
	 */
	public Page<User> findChildUser(Page<User> page, User user) {
		DetachedCriteria dc = userDao.createDetachedCriteria();
		if (!UserUtils.getUser().isAdmin()){//管理员查看全部数据，不做数据筛选
			dc.createAlias("createBy", "createBy");
			 //方式1:硬编码 查看本人数据
	        dc.add(Restrictions.eq("createBy.id", UserUtils.getUser().getId()));
		}
		dc.add(Restrictions.eq(User.FIELD_DEL_FLAG, User.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		return userDao.find(page, dc);
	}
	
	/**
	 * <P>取用户的数据范围</P>
	 */
	public String getDataScope(User user){
		return dataScopeFilterString(user, "office", "");
	}
	
	public User getUserByLoginName(String loginName) {
		return userDao.findByLoginName(loginName);
	}

	@Transactional(readOnly = false)
	public void saveUser(User user) {
		userDao.clear();
		userDao.save(user);
		systemRealm.clearAllCachedAuthorizationInfo();
//		// 同步到Activiti
//		saveActiviti(user);
	}

	@Transactional(readOnly = false)
	public void deleteUser(String id) {
		userDao.deleteById(id);
		// 同步到Activiti
//		deleteActiviti(userDao.get(id));
	}
	//------工作流方法添加start：是否同步Activiti需要进一步测试，tiancd
	@Transactional(readOnly = false)
	public void deleteUser(User user) {
		userDao.deleteById(user.getId());
		// 同步到Activiti
		//deleteActivitiUser(user);
		// 清除用户缓存
		UserUtils.clearCache(user);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
	}
	
	/**
	 * 根据登陆名获取角色列表
	 * @param userId
	 * @return
	 */
	public List<Role> findRole(String userId){
		User usr = userDao.findByLoginName(userId);
		if(usr == null) {
			return new ArrayList<Role>();
		}else {
			return roleDao.findRoleList(usr.getId());
		}
	}
	//------工作流方法添加end：是否同步Activiti需要进一步测试，tiancd
	@Transactional(readOnly = false)
	public void updatePasswordById(String id, String loginName, String newPassword) {
		userDao.updatePasswordById(entryptPassword(newPassword), id);
		systemRealm.clearCachedAuthorizationInfo(loginName);
	}
	
	@Transactional(readOnly = false)
	public void updateUserLoginInfo(String id) {
		userDao.updateLoginInfo(SecurityUtils.getSubject().getSession().getHost(), new Date(), id);
	}
	
	/**
	 * <P>生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash</P>
	 */
	public static String entryptPassword(String plainPassword) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
	}
	
	/**
	 * <P>验证密码</P>
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		byte[] salt = Encodes.decodeHex(password.substring(0,16));
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
	}
	
	//-- Role Service --//
	
	public Role getRole(String id) {
		return roleDao.get(id);
	}
	
	public Role getPersistent(String id) {
		return roleDao.getPersistent(id);
	}
	public Role findRoleByName(String name) {
		return roleDao.findByName(name);
	}
	
	public List<Role> findAllRole(){
		return UserUtils.getRoleList();
	}
	
	//获得运营商下的角色列表201.202.203
	public List<Role> findAllCorpChildRole(){
		return roleDao.findByCorp();
	}
	@Transactional(readOnly = false)
	public void saveRole(Role role) {
		roleDao.clear();
		roleDao.save(role);
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
		// 同步到Activiti
//		saveActiviti(role);
	}

	@Transactional(readOnly = false)
	public void deleteRole(String id) {
		roleDao.deleteById(id);
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
		// 同步到Activiti
//		deleteActiviti(roleDao.get(id));
	}
	
	@Transactional(readOnly = false)
	public Boolean outUserInRole(Role role, String userId) {
		User user = userDao.getPersistent(userId);
		List<String> roleIds = user.getRoleIdList();
		List<Role> roles = user.getRoleList();
		// 
		if (roleIds.contains(role.getId())) {
			roles.remove(role);
			saveUser(user);
			return true;
		}
		return false;
	}
	
	@Transactional(readOnly = false)
	public User assignUserToRole(Role role, String userId) {

		User user = getUser(userId);
		if(user==null ){
			return null;
		}
		
		List<String> roleIds = user.getRoleIdList();
		if (roleIds.contains(role.getId())) {
			return null;
		}
		user.getRoleList().add(role);
		saveUser(user);		
		return user;
	}

	//-- Menu Service --//
	
	public Menu getMenu(String id) {
		return menuDao.get(id);
	}

	public List<Menu> findAllMenu(){
		return UserUtils.getMenuList();
	}
	
	@Transactional(readOnly = false)
	public void saveMenu(Menu menu) {
		menu.setParent(this.getMenu(menu.getParent().getId()));
		String oldParentIds = menu.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		menu.setParentIds(menu.getParent().getParentIds()+menu.getParent().getId()+",");
		menuDao.clear();
		menuDao.save(menu);
		// 更新子节点 parentIds
		List<Menu> list = menuDao.findByParentIdsLike("%,"+menu.getId()+",%");
		for (Menu e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
		}
		menuDao.save(list);
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// 同步到Activiti
//		saveActiviti(menu);
	}

	@Transactional(readOnly = false)
	public void saveMenuSort(Menu menu) {
		menuDao.save(menu);
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
	}
	
	@Transactional(readOnly = false)
	public void deleteMenu(String id) {
		menuDao.deleteById(id, "%,"+id+",%");
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// 同步到Activiti
//		deleteActiviti(id);
	}

	///////////////// Synchronized to the Activiti //////////////////

	/**
	 * <P>手工同步所有Activiti数据</P>
	 */
	@Transactional(readOnly = false)
	public void synToActiviti()  {
		try{
			menuDao.updateBySql("delete from ACT_ID_MEMBERSHIP",null);
			menuDao.updateBySql("delete from ACT_ID_GROUP", null);
			menuDao.updateBySql("delete from ACT_ID_USER", null);
			
//			List<Group> activitiGroupList = identityService.createGroupQuery().list();
//			List<org.activiti.engine.identity.User> activitiUserList = identityService.createUserQuery().list();
//			if (activitiGroupList.size() == 0 &&activitiUserList.size() == 0){
//			 	//同步时候添加所有用户，所有组，以及关联关系，之后增删改用户，增删改角色时不需要判断用户，组是否存在。
//			 	List<User> userList = userDao.findAllList();
//			 	for(User user:userList){
//			 		org.activiti.engine.identity.User activitiUesr = identityService.newUser(ObjectUtils.toString(user.getId()));
//			 		identityService.saveUser(activitiUesr);
//			 	}
//			 	for(Menu menu:menuDao.findAllActivitiList()){
//			 		if (StringUtils.isNotEmpty(menu.getActivitiGroupId())){
//				 		Group group = identityService.newGroup(menu.getActivitiGroupId());
//				 		identityService.saveGroup(group);
//			 		}
//			 	}
//			 	//创建关联关系
//			 	for(User user:userList) {
//			 		List<Menu> menuList = menuDao.findAllActivitiList(user.getId());
//			 		if(!Collections3.isEmpty(menuList)){
//			 			for(Menu menu:menuList) {
//			 				if (StringUtils.isNotEmpty(menu.getActivitiGroupId())){
//			 					identityService.createMembership(ObjectUtils.toString(user.getId()), menu.getActivitiGroupId());
//			 				}
//			 			}
//			 		}
//			 	}
//			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void saveActiviti(Role role) {
		try{
			if(role!=null) {
//				List<User> userList = roleDao.get(role.getId()).getUserList();
				//获得角色下所有的用户列表，实体关联效率提升
				List<User> userList =userDao.findUserListByRole(role.getId());
				
				if(!Collections3.isEmpty(userList)) {
				 	for(User user:userList) {
//				 		String userId = ObjectUtils.toString(user.getId());
//						org.activiti.engine.identity.User activitiUser = identityService.createUserQuery().userId(userId).singleResult();
//						// 是新增用户
//						if (activitiUser == null) {
//							activitiUser = identityService.newUser(userId);
//							identityService.saveUser(activitiUser);
//						} 
						// 同步用户角色关联数据
				 		List<Menu> menuList = menuDao.findAllActivitiList(user.getId());
				 		merge(user, menuList);
				 	}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	private void deleteActiviti(Role role) {
		try{
			if(role!=null) {
//				List<User> userList = roleDao.get(role.getId()).getUserList();
				//获得角色下所有的用户列表，实体关联效率提升
				List<User> userList =userDao.findUserListByRole(role.getId());
				
				if(!Collections3.isEmpty(userList)) {
				 	for(User user:userList) {
				 		List<Menu> menuList = menuDao.findAllActivitiList(user.getId());
				 		merge(user, menuList);
				 	}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveActiviti(User user) {
		try{
			if(user!=null) {
//				String userId = ObjectUtils.toString(user.getId());
//				org.activiti.engine.identity.User activitiUser = identityService.createUserQuery().userId(userId).singleResult();
//				// 是新增用户
//				if (activitiUser == null) {
//					activitiUser = identityService.newUser(userId);
//					identityService.saveUser(activitiUser);
//				} 
				// 同步用户角色关联数据
		 		List<Menu> menuList = menuDao.findAllActivitiList(user.getId());
		 		merge(user, menuList);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteActiviti(User user) {
		try{
			if(user!=null) {
//				String userId = ObjectUtils.toString(user.getId());
//				identityService.deleteUser(userId);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveActiviti(Menu menu) {
		try{
			if(menu!=null){
//				Group group = identityService.createGroupQuery().groupId(menu.getActivitiGroupId()).singleResult();
//				if(group!=null) {
//					identityService.deleteGroup(group.getId());
//				}
//				if(Menu.YES.equals(menu.getIsActiviti()) && StringUtils.isNotBlank(menu.getActivitiGroupId())){
//					group = identityService.newGroup(menu.getActivitiGroupId());
//					group.setName(menu.getActivitiGroupName());
//					identityService.saveGroup(group);
//					List<Role> roleList = menuDao.get(menu.getId()).getRoleList();
//					if(!Collections3.isEmpty(roleList)) {
//						for(Role role:roleList) {
//							List<User> userList = role.getUserList();
//							if(!Collections3.isEmpty(userList)) {
//								for(User user:userList) {
//									identityService.createMembership(ObjectUtils.toString(user.getId()), menu.getActivitiGroupId());
//								}
//							}
//						}
//					}
//				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void deleteActiviti(String id) {
		try{
			if(id!=null) {
				Menu menu = menuDao.get(id);
				if(Menu.YES.equals(menu.getIsActiviti()) && StringUtils.isNotBlank(menu.getActivitiGroupId())){
//					identityService.deleteGroup(menu.getActivitiGroupId());
				}
				if(menu!=null) {
					List<Menu> menuList = menuDao.findByParentIdsLike("%,"+menu.getId()+",%");
					for(Menu m:menuList) {
						if(Menu.YES.equals(menu.getIsActiviti()) && StringUtils.isNotBlank(m.getActivitiGroupId())){
//							identityService.deleteGroup(m.getActivitiGroupId());
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void merge(User user,List<Menu> menuList) {
		try{
//			String userId = ObjectUtils.toString(user.getId());
//			List<Group> activitiGroupList = identityService.createGroupQuery().groupMember(userId).list();
			if(Collections3.isEmpty(menuList)) {
//				for(Group group:activitiGroupList) {
////					identityService.deleteMembership(userId, group.getId());
//				}
			} else {
				Map<String,String> groupMap =Maps.newHashMap();
				for(Menu menu:menuList) {
					groupMap.put(menu.getActivitiGroupId(), menu.getActivitiGroupName());
				}
//				Map<String,String> activitiGroupMap = Collections3.extractToMap(activitiGroupList, "id", "name");
//				for(String groupId:activitiGroupMap.keySet()) {
//					if(StringUtils.isNotBlank(groupId) && !groupMap.containsKey(groupId)) {
//						identityService.deleteMembership(userId, groupId);
//					}
//				}
//				for(String groupId:groupMap.keySet()) {
//					if(StringUtils.isNotBlank(groupId) && !activitiGroupMap.containsKey(groupId)) {
//						identityService.createMembership(userId, groupId);
//					}
//				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	///////////////// Synchronized to the Activiti end //////////////////
	/**
	 * 获取Key加载信息
	 */
	public static boolean printKeyLoadMessage(){
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n======================================================================\r\n");
		sb.append("\r\n    欢迎使用 "+Global.getConfig("productName")+"  - Powered By lnint \r\n");
		sb.append("\r\n======================================================================\r\n");
		System.out.println(sb.toString());
		return true;
	}
}
