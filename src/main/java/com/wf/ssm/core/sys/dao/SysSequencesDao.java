/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 王磊 2015-06-05
 */
package com.wf.ssm.core.sys.dao;

import org.springframework.stereotype.Repository;

import com.wf.ssm.common.persistence.BaseDao;
import com.wf.ssm.common.persistence.Parameter;
import com.wf.ssm.core.sys.entity.SysSequences;

/**
 * 序列管理DAO接口
 *
 * @version 1.0
 * @author 王磊 2015-06-05
 * @since JDK 1.6
 */
@Repository
public class SysSequencesDao extends BaseDao<SysSequences> {
	
	/**
	 * <p>查询所有正常字典<p>
	 * @return 字典list对象
	 */
	public SysSequences findSeqByName(String seqName){
		return getByHql("from SysSequences where delFlag=:p1 and seqName=:p2 ", new Parameter(SysSequences.DEL_FLAG_NORMAL,seqName));
	}
}