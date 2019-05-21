package com.wf.ssm.core.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wf.ssm.common.persistence.mybatis.CrudService;
import com.wf.ssm.common.utils.CacheUtils;
import com.wf.ssm.core.sys.dao.DictDao;
import com.wf.ssm.core.sys.entity.Dict;
import com.wf.ssm.core.sys.utils.DictUtils;

/**
 * <P>字典Service</P>
 * 
 * @version 1.0
 * @author wangpf 2015-03-12 00:00:00
 * @since JDK 1.6
 */
@Service
@Transactional(readOnly = true)
public class DictService extends  CrudService<DictDao, Dict> {
//
//	@Autowired
//	private DictDao dictDao;
//	
//	@Autowired
//	private MyBatisDictDao myBatisDictDao;
//	
//	/**
//	 * <p>根据ID 查询获取字典实体对象<p>
//	 * @param id 字典实体数据表主键
//	 * @return 字典实体对象
//	 */
//	public Dict get(String id) {
//		// MyBatis 查询
//		return myBatisDictDao.get(id);
//		// Hibernate 查询
////		return dictDao.get(id);
//	}
//	
//	/**
//	 * <p>使用检索标准对象Criteria，分页查询字典<p>
//	 * @param page 分页类基础类
//	 * @param dict 字典类
//	 * @return Page 分页对象
//	 */
//	public Page<Dict> find(Page<Dict> page, Dict dict) {
//		// MyBatis 查询
////		dict.setPage(page);
////		page.setList(myBatisDictDao.find(dict));
////		return page;
//		// Hibernate 查询
//		DetachedCriteria dc = dictDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(dict.getType())){
//			dc.add(Restrictions.eq("type", dict.getType()));
//		}
//		if (StringUtils.isNotEmpty(dict.getDescription())){
//			dc.add(Restrictions.like("description", "%"+dict.getDescription()+"%"));
//		}
//		dc.add(Restrictions.eq(Dict.FIELD_DEL_FLAG, Dict.DEL_FLAG_NORMAL));
//		dc.addOrder(Order.asc("type")).addOrder(Order.asc("sort")).addOrder(Order.desc("id"));
//		return dictDao.find(page, dc);
//	}
//	
//	/**
//	 * <p>查询所有正常字典的类型<p>
//	 * @return 字典类型list对象
//	 */
//	public List<String> findTypeList(){
//		return dictDao.findTypeList();
//	}
//	
//	/**
//	 * <p>保存字典<p>
//	 * @param dict 字典Entity
//	 */
//	@Transactional(readOnly = false)
//	public void save(Dict dict) {
//		dictDao.save(dict);
//		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
//	}
//	
//	/**
//	 * <p>根据id,删除本实体数据和实体父ID下的子数据<p>
//	 * @param id 实体数据主键
//	 */
//	@Transactional(readOnly = false)
//	public void delete(String id) {
//		dictDao.deleteById(id);
//		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
//	}
//	
	
	/**
	 * 查询字段类型列表
	 * @return
	 */
	public List<String> findTypeList(){
		return dao.findTypeList(new Dict());
	}

	@Transactional(readOnly = false)
	public void save(Dict dict) {
		super.save(dict);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}

	@Transactional(readOnly = false)
	public void delete(Dict dict) {
		super.delete(dict);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}
}
