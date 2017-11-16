/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 严娜 2015-11-25
 */
package com.wf.ssm.olap.rpt.service.core;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.wf.ssm.core.sys.utils.UserUtils;
import com.wf.ssm.common.persistence.Page;
import com.wf.ssm.common.persistence.Parameter;
import com.wf.ssm.common.service.BaseService;
import com.wf.ssm.common.utils.StringUtils;
import com.wf.ssm.olap.rpt.dao.core.RptCmChildrenDao;
import com.wf.ssm.olap.rpt.entity.core.RptCmChildren;

/**
 * <P>子报表模板Service</P>
 *
 * @version 1.0
 * @author wangpf 2015-11-25
 * @since JDK 1.6
 */
@Service
@Transactional(readOnly = true)
public class RptCmChildrenService extends BaseService {

  	@Autowired
	private RptCmChildrenDao rptCmChildrenDao;
	
	/**
	 * <P>获得实体模型数据</P>
	 * @param id 实体主键;
	 * @return RptCmChildren
	 */
	public RptCmChildren get(String id) {
		return rptCmChildrenDao.get(id);
	}
	/**
	 * <P>获得分页数据</P>
	 * @param page 分页实体;
	 * @param rptCmChildren 业务实体
	 * @return page 分页实体
	 */
	public Page<RptCmChildren> find(Page<RptCmChildren> page, RptCmChildren rptCmChildren) {
	    //HQL查询条件
		DetachedCriteria dc = rptCmChildrenDao.createDetachedCriteria();
		if (!UserUtils.getUser().isAdmin()){//管理员查看全部数据，不做数据筛选
			dc.createAlias("createBy", "createBy");
			 //方式1:硬编码 查看本人数据
            dc.add(Restrictions.eq("createBy.id", UserUtils.getUser().getId()));
		}
        dc.add(Restrictions.eq("cmId", rptCmChildren.getCmId()));
        if(StringUtils.isNotEmpty(rptCmChildren.getFileName())){
        	dc.add(Restrictions.eq("fileName", rptCmChildren.getFileName()));
        }
		dc.add(Restrictions.eq(RptCmChildren.FIELD_DEL_FLAG, RptCmChildren.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		//查询处理
		return rptCmChildrenDao.find(page, dc);
	}
	/**
	 * <P>保存实体模型数据</P>
	 * @param rptCmChildren 业务实体
	 * @return void
	 */	
	@Transactional(readOnly = false)
	public void save(RptCmChildren rptCmChildren) {
		//clear()清除级联缓存，防止保存关联表报错，使用时请打开
		//rptCmChildrenDao.clear();
		rptCmChildrenDao.save(rptCmChildren);
	}
	/**
	 * <P>删除实体模型数据</P>
	 * @param id 实体模型主键
	 * @return void
	 */	
	@Transactional(readOnly = false)
	public void delete(String id) {
		rptCmChildrenDao.deleteById(id);
	}
	
	/**
	 * <P>查询数据库最新的数据信息（不从缓存获取）</P>
	 * @param id 实体模型主键
	 * @return rptCmChildren 业务实体
	 */	
	public RptCmChildren getDBLatestEntity(String id) {
		rptCmChildrenDao.evict(this.get(id));//清指定对象缓存
		return rptCmChildrenDao.getByHql("from RptCmChildren where id = :p1", new Parameter(id));
	}
	
}