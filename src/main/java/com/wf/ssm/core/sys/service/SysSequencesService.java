package com.wf.ssm.core.sys.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.wf.ssm.core.sys.entity.User;
import com.wf.ssm.core.sys.utils.UserUtils;
import com.wf.ssm.common.persistence.Page;
import com.wf.ssm.common.service.BaseService;
import com.wf.ssm.common.utils.StringUtils;
import com.wf.ssm.core.sys.entity.SysSequences;
import com.wf.ssm.core.sys.dao.SysSequencesDao;

/**
 * <P>序列管理Service</P>
 *
 * @version 1.0
 * @author 王磊 2015-06-05
 * @since JDK 1.6
 */
@Service
@Transactional(readOnly = true)
public class SysSequencesService extends BaseService {

  	@Autowired
	private SysSequencesDao sysSequencesDao;
	
	/**
	 * <P>获得实体模型数据</P>
	 * @param id 实体主键;
	 * @return SysSequences
	 */
	public SysSequences get(String id) {
		return sysSequencesDao.get(id);
	}
	/**
	 * <P>获得分页数据</P>
	 * @param page 分页实体;
	 * @param sysSequences 业务实体
	 * @return page 分页实体
	 */
	public Page<SysSequences> find(Page<SysSequences> page, SysSequences sysSequences) {
	    //HQL查询条件
		DetachedCriteria dc = sysSequencesDao.createDetachedCriteria();
		if(StringUtils.isNotBlank(sysSequences.getSeqName())){// 补充位数
			dc.add(Restrictions.like("seqName", "%"+sysSequences.getSeqName()+"%"));
		}
		dc.add(Restrictions.eq(SysSequences.FIELD_DEL_FLAG, SysSequences.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		//查询处理
		return sysSequencesDao.find(page, dc);
	}
	/**
	 * <P>保存实体模型数据</P>
	 * @param sysSequences 业务实体
	 * @return void
	 */	
	@Transactional(readOnly = false)
	public void save(SysSequences sysSequences) {
		//clear()清除级联缓存，防止保存关联表报错，使用时请打开
		//sysSequencesDao.clear();
		sysSequencesDao.save(sysSequences);
	}
	/**
	 * <P>删除实体模型数据</P>
	 * @param id 实体模型主键
	 * @return void
	 */	
	@Transactional(readOnly = false)
	public void delete(String id) {
		sysSequencesDao.deleteById(id);
	}
	/**
	 * <P>查询此序列名称是否存在</P>
	 * @param id 实体模型主键
	 * @return true:存在
	 */	
	public boolean findSeqIsExist(String seqName) {
		boolean ret=false;
		//HQL查询条件
		DetachedCriteria dc = sysSequencesDao.createDetachedCriteria();
		dc.add(Restrictions.eq("seqName", seqName));
		dc.add(Restrictions.eq(SysSequences.FIELD_DEL_FLAG, SysSequences.DEL_FLAG_NORMAL));
		//查询处理
		Page<SysSequences> page =new Page<SysSequences> ();
		Page<SysSequences> page2=sysSequencesDao.find(page, dc);
		if(page2.getList().size()>0){//存在此序列
			ret=true;
		}
		return ret;
	}
	/**
	 * <P>根据序列名称获得下一个序列值</P>
	 * @param id 实体模型主键
	 * @return true:存在
	 */	
	public String findNextSeqByName(String seqName) {
		//根据序列名称查询序列实体
		String ret="";
		SysSequences sysSequences=sysSequencesDao.findSeqByName(seqName);
		//拼接序列格式化输出
		Long curval =sysSequences.getSeqVal();//当前序列值
		String seq ="0" ; 
		if(StringUtils.isNotBlank(sysSequences.getSeqWidth())){// 补充位数
			seq=String.format("%0"+sysSequences.getSeqWidth()+"d", curval);
		}else{
			seq=String.valueOf(curval);
		}
		ret=sysSequences.getLeftpad()+seq+sysSequences.getRightpad();
		//更新序列值
		sysSequences.setSeqVal(curval++);
		sysSequencesDao.save(sysSequences);
		
		return ret;
	}
	
}