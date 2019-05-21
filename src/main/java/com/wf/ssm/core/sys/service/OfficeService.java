package com.wf.ssm.core.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wf.ssm.common.service.BaseService;
import com.wf.ssm.core.sys.dao.OfficeDao;
import com.wf.ssm.core.sys.entity.Office;
import com.wf.ssm.core.sys.utils.UserUtils;

/**
 * 机构Service
 * @version 1.0
 * @author wangpf 2015-3-10
 * @since JDK 1.6
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends BaseService {

	@Autowired
	private OfficeDao officeDao;

	public Office get(String id) {
		return officeDao.get(id);
	}
	public Office getPersistent(String id) {
		return officeDao.getPersistent(id);
	}
	
	/**
	 * 查询机构信息，根据当前用户过滤
	 */
	public List<Office> findAll() {
		return UserUtils.getOfficeList();
	}

	/**
	 * 查询机构信息，根据当前用户过滤
	 */
	public List<Office> findAllByCorp() {
		return UserUtils.getOfficeListByCorp();
	}
	/**
	 * 查询机构信息，根据当前用户过滤出用户所在运营商的组织机构
	 */
	public List<Office> findOfficeListByOnwerCorp() {
		return UserUtils.getOfficeListByOnwerCorp();
	}
	/**
	 * 查询机构信息，根据当前用户过滤出用户本级别及其下级别所有的组织机构数据
	 */
	public List<Office> findChildOfficeListByCorp() {
		return UserUtils.findChildOfficeListByCorp();
	}
	
	/**
	 * 获得所有的组织机构
	 */
	public List<Office> findAllOffice() {
		return UserUtils.getOfficeListByOnwerCorp();
	}

	@Transactional(readOnly = false)
	public void save(Office office) {
		office.setParent(this.get(office.getParent().getId()));
		String oldParentIds = office.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		office.setParentIds(office.getParent().getParentIds() + office.getParent().getId() + ",");
		officeDao.clear();
		officeDao.save(office);
		// 更新子节点 parentIds
		List<Office> list = officeDao.findByParentIdsLike(office.getId());
		for (Office e : list) {
			if(!e.getId().equals(office.getId()) && oldParentIds != null) {
				e.setParentIds(e.getParentIds().replace(oldParentIds, office.getParentIds()));
			}
		}
		officeDao.save(list);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		officeDao.deleteById(id, "%," + id + ",%");
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}

}
