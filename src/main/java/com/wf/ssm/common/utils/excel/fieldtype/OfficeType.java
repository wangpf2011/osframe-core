/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 王朋飞 2015-03-12 12:02:25
 */
package com.wf.ssm.common.utils.excel.fieldtype;

import com.wf.ssm.core.sys.entity.Office;
import com.wf.ssm.core.sys.utils.UserUtils;

/**
 * <P>字段类型转换，机构Entity和机构名称间的转换</P>
 * 
 * @version 1.0
 * @author 王朋飞  2015-03-12 12:02:25
 * @since JDK 1.6
 */
public class OfficeType {

	/**
	 * <P> 根据机构（Office）名称获取机构对象值，一般用于导入时</P>
	 * @param val Office名称
	 * @return Office对象
	 */
	public static Object getValue(String val) {
		for (Office e : UserUtils.getOfficeList()){
			if (val.equals(e.getName())){
				return e;
			}
		}
		return null;
	}

	/**
	 * <P> 根据机构（Office）对象获取机构名称，一般用于导出时</P>
	 * @param val Office对象
	 * @return Office名称
	 */
	public static String setValue(Object val) {
		if (val != null && ((Office)val).getName() != null){
			return ((Office)val).getName();
		}
		return "";
	}
}
