/**
 * Copyright &copy; 2011-2020 lnint Inc. All rights reserved.
 * 
 * 修改信息：v1.0 doc做成
 * A: 新增类  葛松  2015-03-11 15:20:21
 */
package com.wf.ssm.core.sys.dao;

import java.util.List;

import com.wf.ssm.common.persistence.mybatis.CrudDao;
import com.wf.ssm.common.persistence.mybatis.annotation.MyBatisDao;
import com.wf.ssm.core.sys.entity.Dict;
/**
 * <P>字典DAO接口</P>
 * 
 * @version 1.0
 * @since JDK 1.6
 */
@MyBatisDao
public interface DictDao extends  CrudDao<Dict> {

//	/**
//	 * <p>查询所有正常字典<p>
//	 * @return 字典list对象
//	 */
//	public List<Dict> findAllList(){
//		return find("from Dict where delFlag=:p1 order by sort", new Parameter(Dict.DEL_FLAG_NORMAL));
//	}
//
//	/**
//	 * <p>查询所有正常字典的类型<p>
//	 * @return 字典类型list对象
//	 */
//	public List<String> findTypeList(){
//		return find("select type from Dict where delFlag=:p1 group by type", new Parameter(Dict.DEL_FLAG_NORMAL));
//	}
	
	public List<String> findTypeList(Dict dict);
}
