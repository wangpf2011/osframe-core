/**
 * Copyright &copy; 2011-2020 lnint Inc. All rights reserved.
 * 
 * 修改信息：v1.0 doc做成
 * A: 新增类  葛松  2015-03-11 15:20:21
 */
package com.wf.ssm.core.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wf.ssm.common.persistence.BaseDao;
import com.wf.ssm.common.persistence.Parameter;
import com.wf.ssm.core.sys.entity.Dict;
import com.wf.ssm.core.sys.entity.Menu;

/**
 * <P>菜单DAO接口</P>
 * 
 * @version 1.0
 * @author wangpf 2015-03-12 00:00:00
 * @since JDK 1.6
 */
@Repository
public class MenuDao extends BaseDao<Menu> {
	
	/**
	 * <p>查询所有正常活性菜单列表<p>
	 * @return 菜单list 对象
	 */
	public List<Menu> findAllActivitiList() {
		return find("from Menu where delFlag=:p1 and isActiviti = :p2 order by sort",new Parameter(Dict.DEL_FLAG_NORMAL,Menu.YES));
	}
	
	/**
	 * <p>根据所有父级编号模糊查询菜单列表<p>
	 * @param parentIds
	 * @return 菜单list对象
	 */
	public List<Menu> findByParentIdsLike(String parentIds){
		return find("from Menu where parentIds like :p1", new Parameter(parentIds));
	}

	/**
	 * <p>查询所有正常菜单列表<p>
	 * @return 菜单list 对象
	 */
	public List<Menu> findAllList(){
		return find("from Menu where delFlag=:p1 order by sort", new Parameter(Dict.DEL_FLAG_NORMAL));
	}
	
	/**
	 * <p>根据userId查询正常菜单列表<p>
	 * @param userId 用户id
	 * @return 菜单list对象
	 */
	public List<Menu> findByUserId(String userId){
		return find("select distinct m from Menu m, Role r, User u where m in elements (r.menuList) and r in elements (u.roleList)" +
				" and m.delFlag=:p1 and r.delFlag=:p1 and u.delFlag=:p1 and u.id=:p2" + // or (m.user.id=:p2  and m.delFlag=:p1)" + 
				" order by m.sort", new Parameter(Menu.DEL_FLAG_NORMAL, userId));
	}
	
	/**
	 * <p>根据userId查询正常活性菜单列表<p>
	 * @param userId
	 * @return 菜单list对象
	 */
	public List<Menu> findAllActivitiList(String userId) {
		return find("select distinct m from Menu m, Role r, User u where m in elements (r.menuList) and r in elements (u.roleList)" +
				" and m.delFlag=:p1 and r.delFlag=:p1 and u.delFlag=:p1 and m.isActiviti=:p2 and u.id=:p3 order by m.sort", 
				new Parameter(Menu.DEL_FLAG_NORMAL, Menu.YES,userId));
	}
	
	/**
     * <p>根据请求uri获取菜单<p>
     * @return 菜单list 对象
     */
    public Menu findByRequestURI(String requestURI) {
        List<Menu> menus = find("from Menu where delFlag=:p1 and href = :p2 order by sort",new Parameter(Dict.DEL_FLAG_NORMAL,requestURI));
        if(menus != null && menus.size() >0) {
            return menus.get(0);
        }else {
            return new Menu();
        }
    }
}
