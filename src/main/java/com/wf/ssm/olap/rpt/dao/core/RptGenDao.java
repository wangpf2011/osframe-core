/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 严娜 2015-11-27
 */
package com.wf.ssm.olap.rpt.dao.core;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.wf.ssm.common.config.Global;
import com.wf.ssm.common.persistence.BaseDao;
import com.wf.ssm.common.persistence.Parameter;
import com.wf.ssm.common.utils.DateUtils;
import com.wf.ssm.olap.rpt.entity.core.RptGen;

/**
 * 生成报表DAO接口
 *
 * @version 1.0
 * @author 严娜 2015-11-27
 * @since JDK 1.6
 */
@Repository
public class RptGenDao extends BaseDao<RptGen> {
	public int doDelete(String stats_date,String category){
		String dbType = Global.getConfig("jdbc.type");
		String statsdate=DateUtils.formatDate(DateUtils.parseDate(stats_date),"yyyy-MM-dd");
		StringBuffer sql = new StringBuffer("DELETE FROM LN_RPT_GEN WHERE RPT_CATEGORY=:p2 ");
		if("oracle".equals(dbType)){
			sql.append("AND TO_CHAR(RPT_DATE,'yyyy-mm-dd')=:p1");
		}else if("mysql".equals(dbType)){
			sql.append("AND DATE_FORMAT(RPT_DATE,'%Y-%m-%d')=:p1");
		}
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("p1", statsdate);
		parameters.put("p2", category);
		return this.updateBySql(sql.toString(),new Parameter(parameters));
	}
	//查询统计表中最新记录时间,根据报表类型
		public List<String> findLastDate(String type) {
			String dbType = Global.getConfig("jdbc.type");
			List<String> list = Lists.newArrayList();	
	      	StringBuffer sb=new StringBuffer("");
	      	if("oracle".equals(dbType)){
				sb.append("SELECT TO_CHAR(RPT_DATE, 'YYYY-MM-dd') FROM LN_RPT_GEN WHERE DEL_FLAG='0' AND RPT_CATEGORY=:p1 AND ROWNUM=1 ORDER BY RPT_DATE DESC");
			}else if("mysql".equals(dbType)){
				sb.append("SELECT DATE_FORMAT(RPT_DATE, '%Y-%m-%d') FROM LN_RPT_GEN WHERE DEL_FLAG='0' AND RPT_CATEGORY=:p1 ORDER BY RPT_DATE DESC LIMIT 1");
			}
	      	HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("p1", type);
	      	list = this.findBySql(sb.toString(),new Parameter(parameters));
	      	return list;
		 }
}