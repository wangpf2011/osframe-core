package com.wf.ssm.core.sys.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import com.wf.ssm.common.persistence.BaseDao;
import com.wf.ssm.common.persistence.Parameter;
import com.wf.ssm.core.sys.entity.User;

/**
 * <P>用户DAO接口</P>
 * @version 1.0
 * @author wangpf 2015-3-10
 * @since JDK 1.6
 */
@Repository
public class UserDao extends BaseDao<User> {
	/**
	 * <P>查询角色下的用户信息列表</P>
	 */	
	public List<User> findUserListByRole(String roleid) {
		String sql = "SELECT  *  FROM  SYS_USER U,  SYS_USER_ROLE R  WHERE U.ID = R.USER_ID   AND R.ROLE_ID=:roleid AND U.DEL_FLAG = '0' ";
		//HB查询标准原生sql
		HashMap<String,Object> paraMap =new HashMap<String,Object>();
		paraMap.put("roleid", roleid);
		List<User> userList = this.findBySql(sql, new Parameter(paraMap), User.class);
		return userList;
		
	}
	
	public List<User> findAllList() {
		return find("from User where delFlag=:p1 order by id", new Parameter(User.DEL_FLAG_NORMAL));
	}
	
	public User findByLoginName(String loginName){
		return getByHql("from User where loginName = :p1 and delFlag = :p2", new Parameter(loginName, User.DEL_FLAG_NORMAL));
	}
	
	public User checkName(String loginName, String name){
		return getByHql("from User where loginName = :p1 and name = :p2 and delFlag = :p3", new Parameter(loginName, name, User.DEL_FLAG_NORMAL));
	}

	public int updatePasswordById(String newPassword, String id){
		return update("update User set password=:p1 where id = :p2", new Parameter(newPassword, id));
	}
	
	public int updateLoginInfo(String loginIp, Date loginDate, String id){
		return update("update User set loginIp=:p1, loginDate=:p2 where id = :p3", new Parameter(loginIp, loginDate, id));
	}
	/**
	 * <P>查询注册用户是否正确存在是已经存在</P>
	 */		
	public User queryResIsExist(String loginName,String mobile,String id_card){
		return getByHql("from User where loginName = :p1 and name=:p2 and idCard=:p3 and delFlag = :p4", new Parameter(mobile,loginName,id_card, User.DEL_FLAG_NORMAL));
	}
	
	public int resetPwd(String name, String mobile, String pwd) {
		if(StringUtils.isEmpty(name)) {
			return update("update User set password=:p1 where loginName = :p2", new Parameter(pwd, mobile));
		}else {
			return update("update User set password=:p1 where loginName = :p2 and name = :p3", new Parameter(pwd, mobile, name));
		}
	}
	/**
	 * <P>根据根据用户登陆名，删除用户信息</P>
	 */		
	public int deleteUserByLogname(String mobile){
		return update("update User set delFlag='1' where loginName = :p1", new Parameter(mobile));
	}
	
	/**
	 * <P>根据运营商顶级组织机构ID 查询下面运营商信息</P>
	 */		
	public User findCorpUser(String officeId){
		
		String sql = "SELECT  *  FROM  SYS_USER U,SYS_OFFICE O,SYS_USER_ROLE R WHERE U.OFFICE_ID=O.ID  AND U.ID=R.USER_ID AND R.ROLE_ID='20' AND O.ID=:officeId AND U.DEL_FLAG = :p1";
		//HB查询标准原生sql
		HashMap<String,Object> paraMap =new HashMap<String,Object>();
		paraMap.put("officeId", officeId);
		paraMap.put("p1", User.DEL_FLAG_NORMAL);
		List<User> userList = this.findBySql(sql, new Parameter(paraMap), User.class);
		
		if(userList!=null && userList.size()>0){
			return userList.get(0);
		}
		return null;
	}
	
}
