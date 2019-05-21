package com.wf.ssm.core.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wf.ssm.common.persistence.BaseDao;
import com.wf.ssm.common.persistence.Parameter;
import com.wf.ssm.core.sys.entity.Area;

/**
 * <P>区域DAO接口</P>
 * 
 * @version 1.0
 * @author wangpf 2015-03-12 00:00:00
 * @since JDK 1.6
 */
@Repository
public class AreaDao extends BaseDao<Area> {
	
	/**
	 * <P>根据父节点Id模糊查询区域</P>
	 * @param parentIds 父节点Id
	 * @return 区域List 对象
	 */
	public List<Area> findByParentIdsLike(String parentIds){
		return find("from Area where parentIds like :p1", new Parameter(parentIds));
	}

	/**
	 * <P>查询所有正常区域</P>
	 * @return 区域List 对象
	 */
	public List<Area> findAllList(){
		return find("from Area where delFlag=:p1 order by code", new Parameter(Area.DEL_FLAG_NORMAL));
	}
	
	/**
	 * <P>根据父级编号或所有父级编号查询其子区域</P>
	 * @param parentId 父级编号
	 * @param likeParentIds 所有父级编号
	 * @return 区域List 对象
	 */
	public List<Area> findAllChild(String parentId, String likeParentIds){
		return find("from Area where delFlag=:p1 and (id=:p2 or parent.id=:p2 or parentIds like :p3) order by code", 
				new Parameter(Area.DEL_FLAG_NORMAL, parentId, likeParentIds));
	}
}
