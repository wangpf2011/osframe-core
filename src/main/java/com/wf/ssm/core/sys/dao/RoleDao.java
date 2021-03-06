package com.wf.ssm.core.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wf.ssm.common.persistence.BaseDao;
import com.wf.ssm.common.persistence.Parameter;
import com.wf.ssm.core.sys.entity.Role;
import com.wf.ssm.core.sys.entity.UserRole;

/**
 * <P>角色DAO接口</P>
 * @version 1.0
 * @author wangpf 2015-3-10
 * @since JDK 1.6
 */
@Repository
public class RoleDao extends BaseDao<Role> {

	public Role findByName(String name) {
		return getByHql("from Role where delFlag = :p1 and name = :p2", new Parameter(Role.DEL_FLAG_NORMAL, name));
	}
	public  List<Role> findByCorp() {
		return find("from Role where delFlag = :p1 and id in('201','202','203') ", new Parameter(Role.DEL_FLAG_NORMAL));
	}
	public List<UserRole> findRoles(String userId) {
		return find("from UserRole where user_id = :p1", new Parameter(userId));
	}
	//------配置工作流方法添加start：是否同步Activiti需要进一步测试，tiancd
	public List<Role> findRoleList(String userId) {
		String sql = "select * from sys_role r where r.id in (select role_id from sys_user_role where user_id=:p1)";
		return this.findBySql(sql, new Parameter(userId), Role.class);
	}
	//------配置工作流方法添加end：是否同步Activiti需要进一步测试，tiancd	
	 
}
