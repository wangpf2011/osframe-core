/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 严娜 2015-11-10
 */
package com.wf.ssm.olap.rpt.dao.core;

import org.springframework.stereotype.Repository;

import com.wf.ssm.common.persistence.BaseDao;
import com.wf.ssm.common.persistence.BaseEntity;
import com.wf.ssm.common.persistence.Parameter;
import com.wf.ssm.olap.rpt.entity.core.RptQuerycondition;

/**
 * 报表查询条件维护DAO接口
 *
 * @version 1.0
 * @author wangpf 2015-11-10
 * @since JDK 1.6
 */
@Repository
public class RptQueryconditionDao extends BaseDao<RptQuerycondition> {
	//根据报表模板主键删除查询条件
	public void deleteByCm(String cmId) {
		update("update RptQuerycondition set delFlag='" + BaseEntity.DEL_FLAG_DELETE + "' where rptCm.id=:p1",new Parameter(cmId));
	}
}