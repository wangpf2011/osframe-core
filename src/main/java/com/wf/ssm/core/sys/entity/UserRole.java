/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 位苏 2015-3-11 12:02:25
 */
package com.wf.ssm.core.sys.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wf.ssm.common.persistence.BaseEntity;

/**
 * <P>sys_user_role 实体类</P>
 * @version 1.0
 * @author wangpf 2015-3-10
 * @since JDK 1.6
 */
@Entity
@Table(name = "sys_user_role")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserRole extends BaseEntity<UserRole> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String user_id;	// 用户id
	private String role_id; 	// 角色id
	
	public UserRole() {
		super();
	}
	@Id
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	@Id
	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}
}
