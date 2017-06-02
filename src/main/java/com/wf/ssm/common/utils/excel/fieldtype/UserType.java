/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 王朋飞 2015-03-12 12:02:25
 */
package com.wf.ssm.common.utils.excel.fieldtype;

import com.wf.ssm.core.sys.entity.User;
import com.wf.ssm.core.sys.utils.UserUtils;

/**
 * <P>字段类型转换，用户Entity和用户名称间的转换</P>
 * 
 * @version 1.0
 * @author 王朋飞  2015-03-12 12:02:25
 * @since JDK 1.6
 */
public class UserType {
	/**
	 * <P> 根据用户（User）名称获取用户对象值，一般用于导入时</P>
	 * @param val User名称
	 * @return User对象
	 */
	public static Object getValue(String val) {
		for (User e : UserUtils.getUserList()){
			if (val.equals(e.getName())){
				return e;
			}
		}
		return null;
	}

	/**
	 * <P> 根据用户（User）对象获取用户名称，一般用于导出时</P>
	 * @param val User对象
	 * @return User名称
	 */
	public static String setValue(Object val) {
		if (val != null && ((User)val).getName() != null){
			return ((User)val).getName();
		}
		return "";
	}
}
