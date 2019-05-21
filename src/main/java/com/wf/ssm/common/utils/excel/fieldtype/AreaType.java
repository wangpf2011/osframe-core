package com.wf.ssm.common.utils.excel.fieldtype;

import com.wf.ssm.core.sys.entity.Area;
import com.wf.ssm.core.sys.utils.UserUtils;

/**
 * <P>字段类型转换，区域Entity和区域名称间的转换</P>
 * 
 * @version 1.0
 * @author 王朋飞  2015-03-12 12:02:25
 * @since JDK 1.6
 */
public class AreaType {

	/**
	 * <P> 根据区域（Area）名称获取区域对象值，一般用于导入时</P>
	 * @param val Area名称
	 * @return Area对象
	 */
	public static Object getValue(String val) {
		for (Area e : UserUtils.getAreaList()){
			if (val.equals(e.getName())){
				return e;
			}
		}
		return null;
	}

	/**
	 * <P> 根据区域（Area）对象获取区域名称，一般用于导出时</P>
	 * @param val Area对象
	 * @return Area名称
	 */
	public static String setValue(Object val) {
		if (val != null && ((Area)val).getName() != null){
			return ((Area)val).getName();
		}
		return "";
	}
}
