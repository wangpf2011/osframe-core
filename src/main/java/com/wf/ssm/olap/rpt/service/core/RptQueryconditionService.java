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
import com.wf.ssm.olap.rpt.dao.core.RptQueryconditionDao;
import com.wf.ssm.olap.rpt.entity.core.RptQuerycondition;

/**
 * <P>报表查询条件维护Service</P>
 *
 * @version 1.0
 * @author wangpf 2015-11-10
 * @since JDK 1.6
 */
@Service
@Transactional(readOnly = true)
public class RptQueryconditionService extends BaseService {

  	@Autowired
	private RptQueryconditionDao rptQueryconditionDao;
  	@Autowired
	private RptCmService rptCmService;
	
	/**
	 * <P>获得实体模型数据</P>
	 * @param id 实体主键;
	 * @return RptQuerycondition
	 */
	public RptQuerycondition get(String id) {
		return rptQueryconditionDao.get(id);
	}
	public RptQuerycondition getObj(String id) {
		if (StringUtils.isNotBlank(id)){
			return rptQueryconditionDao.get(id);
		}else{
			return new RptQuerycondition();
		}
	}
	/**
	 * <P>获得分页数据</P>
	 * @param page 分页实体;
	 * @param rptQuerycondition 业务实体
	 * @return page 分页实体
	 */
	public Page<RptQuerycondition> find(Page<RptQuerycondition> page, RptQuerycondition rptQuerycondition) {
	    //HQL查询条件
		DetachedCriteria dc = rptQueryconditionDao.createDetachedCriteria();
		if (!UserUtils.getUser().isAdmin()){//管理员查看全部数据，不做数据筛选
			dc.createAlias("createBy", "createBy");
			 //方式1:硬编码 查看本人数据
            dc.add(Restrictions.eq("createBy.id", UserUtils.getUser().getId()));
		}
		if(rptQuerycondition.getRptCm()!=null&&StringUtils.isNotEmpty(rptQuerycondition.getRptCm().getId())){
			dc.createAlias("rptCm", "rptCm");
	        dc.add(Restrictions.eq("rptCm.id",rptQuerycondition.getRptCm().getId()));
		}
		dc.add(Restrictions.eq(RptQuerycondition.FIELD_DEL_FLAG, RptQuerycondition.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		//查询处理
		return rptQueryconditionDao.find(page, dc);
	}
	/**
	 * <P>保存实体模型数据</P>
	 * @param rptQuerycondition 业务实体
	 * @return void
	 */	
	@Transactional(readOnly = false)
	public void save(RptQuerycondition rptQuerycondition) {
		//clear()清除级联缓存，防止保存关联表报错，使用时请打开
		//rptCmDao.clear();
		rptQueryconditionDao.save(rptQuerycondition);
	}
	/**
	 * <P>删除实体模型数据</P>
	 * @param id 实体模型主键
	 * @return void
	 */	
	@Transactional(readOnly = false)
	public void delete(String id) {
		rptQueryconditionDao.deleteById(id);
	}
	
	/**
	 * <P>查询数据库最新的数据信息（不从缓存获取）</P>
	 * @param id 实体模型主键
	 * @return rptQuerycondition 业务实体
	 */	
	public RptQuerycondition getDBLatestEntity(String id) {
		rptQueryconditionDao.evict(this.get(id));//清指定对象缓存
		return rptQueryconditionDao.getByHql("from RptQuerycondition where id = :p1", new Parameter(id));
	}
	/**
	 * <P>根据报表模板主键删除实体模型数据</P>
	 * @param CmId 报表模板主键
	 * @return void
	 */	
	@Transactional(readOnly = false)
	public void deleteByCm(String cmId) {
		rptQueryconditionDao.deleteByCm(cmId);
	}
}