/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 严娜 2015-11-10
 */
package com.wf.ssm.olap.rpt.service.core;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wf.ssm.common.persistence.Page;
import com.wf.ssm.common.persistence.Parameter;
import com.wf.ssm.common.service.BaseService;
import com.wf.ssm.common.utils.StringUtils;
import com.wf.ssm.core.sys.entity.User;
import com.wf.ssm.olap.rpt.dao.core.RptCmDao;
import com.wf.ssm.olap.rpt.dao.core.RptQueryconditionDao;
import com.wf.ssm.olap.rpt.entity.core.RptCm;
import com.wf.ssm.olap.rpt.entity.core.RptQuerycondition;

/**
 * <P>报表模板维护Service</P>
 *
 * @version 1.0
 * @author 严娜 2015-11-10
 * @since JDK 1.6
 */
@Service
@Transactional(readOnly = true)
public class RptCmService extends BaseService {

  	@Autowired
	private RptCmDao rptCmDao;
  	@Autowired
	private RptQueryconditionDao rptQueryconditionDao;
	
	/**
	 * <P>获得实体模型数据</P>
	 * @param id 实体主键;
	 * @return RptCm
	 */
	public RptCm get(String id) {
		return rptCmDao.get(id);
	}
	
	public RptCm get2(String id) {
		RptCm tst= rptCmDao.get(id);
		rptCmDao.evict(tst);
		return tst;
	}
	
	/**
	 * <P>获得分页数据</P>
	 * @param page 分页实体;
	 * @param rptCm 业务实体
	 * @return page 分页实体
	 */
	public Page<RptCm> find(Page<RptCm> page, RptCm rptCm,String type) {
	    //HQL查询条件
		DetachedCriteria dc = rptCmDao.createDetachedCriteria();
		   if (StringUtils.isNotEmpty(rptCm.getCmName())){
		     dc.add(Restrictions.like("cmName", "%"+rptCm.getCmName()+"%"));
		   }
		  if (StringUtils.isNotEmpty(rptCm.getRptType())){
		     dc.add(Restrictions.eq("rptType", rptCm.getRptType()));
		  }
		  if (StringUtils.isNotEmpty(rptCm.getRptCategory())){
		     dc.add(Restrictions.eq("rptCategory", rptCm.getRptCategory()));
		  }
		dc.add(Restrictions.eq(RptCm.FIELD_DEL_FLAG, RptCm.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		//报表模板审核
		if("2".equals(type)){
			dc.add(Restrictions.eq("flowStatus", "1"));
		//报表模板发布
		}else if("3".equals(type)){
			dc.add(Restrictions.eq("flowStatus", "2"));
		//报表模板查询
		}else if("4".equals(type)){
			dc.add(Restrictions.eq("flowStatus", "4"));
			dc.add(Restrictions.eq("status", "1"));
		}
		//查询处理
		return rptCmDao.find(page, dc);
	}
	/**
	 * <P>保存实体模型数据</P>
	 * @param rptCm 业务实体
	 * @return void
	 */	
	@Transactional(readOnly = false)
	public void save(RptCm rptCm) {
		//clear()清除级联缓存，防止保存关联表报错，使用时请打开
		//rptCmDao.clear();
		rptCmDao.save(rptCm);
	}
	/**
	 * <P>保存查询条件实体模型数据</P>
	 * @param rptCm 业务实体
	 * @return void
	 */	
	@Transactional(readOnly = false)
	public void saveQuery(RptCm rptCm) {
		List<RptQuerycondition> rptQueryconditionlist=rptCm.getRptQueryconditionList();
		for (RptQuerycondition rptQuerycondition : rptQueryconditionlist){
			rptQuerycondition.setRptCm(rptCm);
			rptQueryconditionDao.save(rptQuerycondition);
		}
	}
	/**
	 * <P>删除实体模型数据</P>
	 * @param id 实体模型主键
	 * @return void
	 */	
	@Transactional(readOnly = false)
	public void delete(String id) {
		rptCmDao.deleteById(id);
	}
	/**
	 * <P>把原有的已生效的报表模板置为作废</P>
	 * @param corporation 运营商主键
	 * @param rptType 报表类型
	 * @return void
	 */	
	@Transactional(readOnly = false)
	public void cancelUpdate(String corporation,String rptCategory,String rptType) {
		List<RptCm> rptlist=findByCorporationAndRptType(corporation,rptCategory,rptType);
		if(rptlist.size()>0){
			RptCm rptCm=rptlist.get(0);
			rptCm.setStatus("2");
			rptCmDao.save(rptCm);
		}
	}
	/**
	 * <P>根据运营商和报表分类、报表类型获得分页数据</P>
	 * @param page 分页实体;
	 * @param rptCm 业务实体
	 * @return page 分页实体
	 */
	public List<RptCm> findByCorporationAndRptType(String corporation,String rptCategory,String rptType) {
	    //HQL查询条件
		DetachedCriteria dc = rptCmDao.createDetachedCriteria();
		//数据过滤
		   if (StringUtils.isNotEmpty(corporation)){
			   dc.createAlias("corporation", "corporation");
			   dc.add(Restrictions.eq("corporation.id", corporation));
		   }
		   if (StringUtils.isNotEmpty(rptCategory)){
			   dc.add(Restrictions.eq("rptCategory", rptCategory));
		   }
		  if (StringUtils.isNotEmpty(rptType)){
		     dc.add(Restrictions.eq("rptType", rptType));
		  }
		dc.add(Restrictions.eq("status", "1"));
		dc.add(Restrictions.eq(RptCm.FIELD_DEL_FLAG, RptCm.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		//查询处理
		return rptCmDao.find(dc);
	}
	/**
	 * <P>查询数据库最新的数据信息（不从缓存获取）</P>
	 * @param id 实体模型主键
	 * @return rptCm 业务实体
	 */	
	public RptCm getDBLatestEntity(String id) {
		rptCmDao.evict(this.get(id));//清指定对象缓存
		return rptCmDao.getByHql("from RptCm where id = :p1", new Parameter(id));
	}
	/**
	 * <P>根据报表模板类型查询报表模板</P>
	 * @param page 分页实体;
	 * @param rptCm 业务实体
	 * @return page 分页实体
	 */
	public RptCm findByRptType(User corporation,String rptType) {
	    //HQL查询条件
		DetachedCriteria dc = rptCmDao.createDetachedCriteria();
		//数据过滤
		   if (corporation!=null){
				dc.createAlias("createBy", "createBy");
				dc.createAlias("createBy.office", "office");
	            dc.add(Restrictions.or(Restrictions. eq("office.id",corporation.getOffice().getId()),Restrictions.like("office.parentIds","%,"+corporation.getOffice().getId()+",%")));
		   }
		  if (StringUtils.isNotEmpty(rptType)){
		     dc.add(Restrictions.eq("rptType", rptType));
		  }
		dc.add(Restrictions.eq("status", "1"));
		dc.add(Restrictions.eq("rptCategory", "3"));
		dc.add(Restrictions.eq(RptCm.FIELD_DEL_FLAG, RptCm.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		//查询处理
		return rptCmDao.find(dc).get(0);
	}
}