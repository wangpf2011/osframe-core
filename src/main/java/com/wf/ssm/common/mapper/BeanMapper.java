package com.wf.ssm.common.mapper;

import java.util.Collection;
import java.util.List;

import org.dozer.DozerBeanMapper;

import com.google.common.collect.Lists;

/**
 * <P>简单封装Dozer(dozer是一种JavaBean的映射工具), 实现Bean<->Bean的深度转换.实现:<br>
 * 1. 持有Mapper的单例. </br>
 * 2. 返回值类型转换.</br>
 * 3. 批量转换Collection中的所有对象.</br>
 * 4. 区分创建新的B对象与将对象A值复制到已存在的B对象两种函数.</p>
 * 
 * @version 1.0 
 * @author wangpf  2015-03-11 14:40:00
 * @since JDK 1.6
 */
public class BeanMapper {

	/**
	 * <p>持有Dozer单例, 避免重复创建DozerMapper消耗资源.</p>
	 */
	private static DozerBeanMapper dozer = new DozerBeanMapper();

	/**
	 * <p>基于Dozer转换对象的类型.</br>
     * 通过source对象中的字段内容映射到destinationClass实例对象中，并返回新的destinationClass实例对象。 </p>
     *  
     * @param source 源数据对象 
     * @param destinationClass 要构造新的实例对象Class 
     * @return &lt;T> destinationClass实例对象
     */ 
	public static <T> T map(Object source, Class<T> destinationClass) {
		return dozer.map(source, destinationClass);
	}

	/**
	 * <p>基于Dozer转换Collection中对象的类型.</br>
	 * 把sourceList对象集合中的每个对象的字段内容映射到destinationClass实例对象中，并返回一个destinationClass实例对象列表。 </p>
	 * @param sourceList 需要转换的对象集合
	 * @param destinationClass 要构造新的实例对象Class
	 * @return List &lt;T> 转换后的对象列表
	 */
	@SuppressWarnings("rawtypes")
	public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
		List<T> destinationList = Lists.newArrayList();
		for (Object sourceObject : sourceList) {
			T destinationObject = dozer.map(sourceObject, destinationClass);
			destinationList.add(destinationObject);
		}
		return destinationList;
	}

	/**
	 * <p>基于Dozer将对象source的所有属性值拷贝到对象destination中.</p> 
     * @param source 对象source 
     * @param destination 对象destination 
     */  
	public static void copy(Object source, Object destinationObject) {
		dozer.map(source, destinationObject);
	}
}