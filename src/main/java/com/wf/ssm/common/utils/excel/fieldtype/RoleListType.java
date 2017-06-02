/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 王朋飞 2015-03-12 12:02:25
 */
package com.wf.ssm.common.utils.excel.fieldtype;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;
import com.wf.ssm.common.utils.Collections3;
import com.wf.ssm.common.utils.SpringContextHolder;
import com.wf.ssm.core.sys.entity.Role;
import com.wf.ssm.core.sys.service.SystemService;

/**
 * <P>字段类型转换，角色名称和角色对象列表间的转换</P>
 * 
 * @version 1.0
 * @author 王朋飞  2015-03-12 12:02:25
 * @since JDK 1.6
 */
public class RoleListType {

	private static SystemService systemService = SpringContextHolder.getBean(SystemService.class);
	
	/**
	 * <P> 根据角色（Role）名称获取角色对象列表，一般用于导入时</P>
	 * @param val Role名称:角色1名称,角色2名称,角色3名称
	 * @return List<Role>
	 */
	public static Object getValue(String val) {
		List<Role> roleList = Lists.newArrayList();
		List<Role> allRoleList = systemService.findAllRole();
		for (String s : StringUtils.split(val, ",")){
			for (Role e : allRoleList){
				if (e.getName().equals(s)){
					roleList.add(e);
				}
			}
		}
		return roleList.size()>0?roleList:null;
	}

	/**
	 * <P> 根据角色对象列表（List<Role>）获取角色名称字符串，一般用于导出时</P>
	 * @param val List<Role>对象
	 * @return Area名称 :角色1名称,角色2名称,角色3名称
	 */
	public static String setValue(Object val) {
		if (val != null){
			@SuppressWarnings("unchecked")
			List<Role> roleList = (List<Role>)val;
			return Collections3.extractToString(roleList, "name", ", ");
		}
		return "";
	}
	
}
