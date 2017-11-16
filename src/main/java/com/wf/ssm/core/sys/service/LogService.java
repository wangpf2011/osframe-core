/**
 * Copyright &copy; 2011-2020 lnint Inc. All rights reserved.
 * 
 * 修改信息：v1.0 doc做成
 * A: 新增类  葛松  2015-03-11 15:20:21
 */
package com.wf.ssm.core.sys.service;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wf.ssm.common.persistence.Page;
import com.wf.ssm.common.service.BaseService;
import com.wf.ssm.common.utils.DateUtils;
import com.wf.ssm.common.utils.StringUtils;
import com.wf.ssm.core.sys.dao.LogDao;
import com.wf.ssm.core.sys.entity.Log;

/**
 * <P>日志Service</P>
 * 
 * @version 1.0
 * @author wangpf 2015-03-12 00:00:00
 * @since JDK 1.6
 */
@Service
@Transactional(readOnly = true)
public class LogService extends BaseService {

	@Autowired
	private LogDao logDao;
	
	/**
	 * <p>根据ID 查询获取日志实体对象<p>
	 * @param id 日志实体数据表主键
	 * @return 日志实体对象
	 */
	public Log get(String id) {
		return logDao.get(id);
	}
	
	/**
	 * <p>使用检索标准对象Criteria，分页查询日志<p>
	 * @param page 分页类基础类
	 * @param paramMap Map对象
	 * @return Page 分页对象
	 */
	public Page<Log> find(Page<Log> page, Map<String, Object> paramMap) {
		DetachedCriteria dc = logDao.createDetachedCriteria();

		Long createById = StringUtils.toLong(paramMap.get("createById"));
		if (createById > 0){
			//查询createBy.id = createById的记录
			dc.add(Restrictions.eq("createBy.id", createById));
		}
		
		String requestUri = ObjectUtils.toString(paramMap.get("requestUri"));
		if (StringUtils.isNotBlank(requestUri)){
			//根据requestUri模糊查询requestUri
			dc.add(Restrictions.like("requestUri", "%"+requestUri+"%"));
		}

		String exception = ObjectUtils.toString(paramMap.get("exception"));
		if (StringUtils.isNotBlank(exception)){
			////查询type = Log.TYPE_EXCEPTION(错误)的记录
			dc.add(Restrictions.eq("type", Log.TYPE_EXCEPTION));
		}
		
		//指定日志开始时间
		Date beginDate = DateUtils.parseDate(paramMap.get("beginDate"));
		if (beginDate == null){
			beginDate = DateUtils.setDays(new Date(), 1);
			paramMap.put("beginDate", DateUtils.formatDate(beginDate, "yyyy-MM-dd"));
		}
		//指定日志结束时间
		Date endDate = DateUtils.parseDate(paramMap.get("endDate"));
		if (endDate == null){
			endDate = DateUtils.addDays(DateUtils.addMonths(beginDate, 1), -1);
			paramMap.put("endDate", DateUtils.formatDate(endDate, "yyyy-MM-dd"));
		}
		//查询开始时间结束时间之内的日志记录
		dc.add(Restrictions.between("createDate", beginDate, endDate));
		
		dc.addOrder(Order.desc("createDate"));
		return logDao.find(page, dc);
	}
	
}
