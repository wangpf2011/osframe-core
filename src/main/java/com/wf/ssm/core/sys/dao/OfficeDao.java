/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 位苏 2015-3-11 12:02:25
 */
package com.wf.ssm.core.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.wf.ssm.common.persistence.BaseDao;
import com.wf.ssm.common.persistence.Parameter;
import com.wf.ssm.core.sys.entity.Office;

/**
 * 机构DAO接口
 * @version 1.0
 * @author weisu 2015-3-10
 * @since JDK 1.6
 */
@Repository
public class OfficeDao extends BaseDao<Office> {
	
	/**
	 * 根据父机构id模糊查询机构信息
	 */
	public List<Office> findByParentIdsLike(String parentIds) {
		List<Office> os = Lists.newArrayList();
		try {
			os = find("from Office where (parentIds like :p1 or id=:p2) and delFlag='0' ", new Parameter("%,"+parentIds+",%",parentIds));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return os;
	}
	/**
	 * 根据父机构id查询机构信息
	 */
	public List<Office> findByParentId(String parentId){
		return find("from Office where parent.id =:p1 and delFlag='0' ", new Parameter(parentId));
	}
}
