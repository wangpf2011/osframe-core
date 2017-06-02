/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 王磊 2014-12-17 12:02:25
 */
package com.wf.ssm.common.persistence;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <P>DAO层（数据库操作)传入参数 基础类<br>
 * +定义传入参数的几种数据类型(包括一维数组，二维数组，HashMap类型)<br>
 * +适用于标准原生SQL和HQL<br>
 * </p> 
 * @version 1.0
 * @author 王磊 2015-03-11 12:02:25
 * @since JDK 1.6
 */
public class Parameter extends HashMap<String, Object> {
	
	private static final long serialVersionUID = 66053120140527L;
	
	/**
	 * <P>构造类,默认索引,一维数组参数<br>
	 * <b>例如：</b><br>
	 * 一个参数：new Parameter("name")<br>
	 * 多个参数：new Parameter("id","3",9,...)
	 * <br>
	 * SQL或HQL参数位对应为 <b>默认占位符</b>（:p1 :p2 :p3 ....)格式
	 * <P>
	 * @param values 参数值
	 */
	public Parameter(Object... values) {
		if (values != null){
			for (int i=0; i<values.length; i++){
				put("p"+(i+1), values[i]);
			}
		}
	}
	
	/**
	 * <P>构造类,自定义索引,二维数组参数<br>
	 * <b>例如：</b><br>
	 * 一个参数：new Parameter(new Object[][]{{"id", id}<br>
	 * 多个参数：new Parameter(new Object[][]{{"id", id}, {"name", name}，.....})
	 * <br>
	 * +SQL或HQL参数位对应为<b>自定义占位符</b>（:id :name .. ....)key格式
	 * <P>
	 * @param parameters 参数二维数组
	 */
	public Parameter(Object[][] parameters) {
		if (parameters != null){
			for (Object[] os : parameters){
				if (os.length == 2){
					put((String)os[0], os[1]);
				}
			}
		}
	}
	/**
	 * <P>构造类,自定义索引,HashMap参数（key-value格式）<br>
	 * <b>例如：</b><br>
	 *  Map m=new HashMap&lt;String,Object>();<br>
	 *  m.put("id",1);<br>
	 *  .....<br>
	 *  new Parameter(m);<br>
	 * +SQL或HQL参数位对应为<b>自定义占位符</b>（:id :name .. ....)key格式
	 * <P>
	 * @param parameters HashMap&lt;String,Object>
	 */
	public Parameter(Map<String,Object> parameters) {
		Iterator<Entry<String, Object>> iter = parameters.entrySet().iterator();  
        while (iter.hasNext()) {  
	        Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter.next();  
	        put(entry.getKey(), entry.getValue());
        }  
	}
	
}
