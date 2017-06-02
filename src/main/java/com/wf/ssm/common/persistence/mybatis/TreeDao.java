package com.wf.ssm.common.persistence.mybatis;

import java.util.List;

import com.wf.ssm.common.persistence.mybatis.CrudDao;

/**
 * DAO支持类实现
 * @author wangpf
 * @version 2017-02-21
 */
public interface TreeDao<T extends TreeEntity<T>> extends CrudDao<T> {

	/**
	 * 找到所有子节点
	 * @param entity
	 * @return
	 */
	public List<T> findByParentIdsLike(T entity);

	/**
	 * 更新所有父节点字段
	 * @param entity
	 * @return
	 */
	public int updateParentIds(T entity);
	
}