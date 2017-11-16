/**
 * Copyright &copy; 2011-2020 lnint Inc. All rights reserved.
 * 
 * 修改信息：v1.0 doc做成
 * A: 新增类  葛松  2015-03-11 15:20:21
 */
package com.wf.ssm.core.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wf.ssm.common.service.BaseService;
import com.wf.ssm.core.sys.dao.AreaDao;
import com.wf.ssm.core.sys.entity.Area;
import com.wf.ssm.core.sys.utils.UserUtils;

/**
 * <P>区域Service</P>
 * 
 * @version 1.0
 * @author wangpf 2015-03-12 00:00:00
 * @since JDK 1.6
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends BaseService {

	@Autowired
	private AreaDao areaDao;
	
	/**
	 * <p>根据ID 查询获取区域实体对象<p>
	 * @param id 区域实体数据表主键
	 * @return 区域实体对象
	 */
	public Area get(String id) {
		return areaDao.get(id);
	}

	/**
	 * <p>获取正常区域list对象<p>
	 * @return 区域List对象
	 */
	public List<Area> findAll(){
		return UserUtils.getAreaList();
	}
	/**
	 * <p>获取正常区域list对象<p>
	 * @return 区域List对象
	 */
	public List<Area> findDown(){
		return UserUtils.getDownAreaList();
	}
	/**
	 * <p>保存区域<p>
	 * @param area 区域Entity
	 */
	@Transactional(readOnly = false)
	public void save(Area area) {
		area.setParent(this.get(area.getParent().getId()));
		String oldParentIds = area.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		area.setParentIds(area.getParent().getParentIds()+area.getParent().getId()+",");
		areaDao.clear();
		areaDao.save(area);
		// 更新子节点 parentIds
		List<Area> list = areaDao.findByParentIdsLike("%,"+area.getId()+",%");
		for (Area e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, area.getParentIds()));
		}
		areaDao.save(list);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}
	
	/**
	 * <p>根据id,删除本实体数据和实体父ID下的子数据<p>
	 * @param id 实体数据主键
	 */
	@Transactional(readOnly = false)
	public void delete(String id) {
		areaDao.deleteById(id, "%,"+id+",%");
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}
	
}
