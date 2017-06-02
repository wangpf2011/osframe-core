/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 赵庆辉 2015-3-12 9:06:26
 */
package com.wf.ssm.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * <P>Collections工具集.</P>
 * 
 * @version 1.0
 * @author 赵庆辉 2015-3-12 9:06:26
 * @since JDK 1.6
 */
@SuppressWarnings("rawtypes")
public class Collections3 {

	/**
	 * <P>提取集合中的对象的两个属性(通过Getter函数), 组合成Map.</P>
	 * @param collection 来源集合.
	 * @param keyPropertyName 要提取为Map中的Key值的属性名.
	 * @param valuePropertyName 要提取为Map中的Value值的属性名.
	 * @return map 组装后的map集合对象.
	 */
	@SuppressWarnings("unchecked")
	public static Map extractToMap(final Collection collection, final String keyPropertyName,
			final String valuePropertyName) {
		Map map = new HashMap(collection.size());

		// 遍历collection集合,将集合中每个元素的key和value取出然后放入map集合中
		try {
			for (Object obj : collection) {
				map.put(PropertyUtils.getProperty(obj, keyPropertyName),
						PropertyUtils.getProperty(obj, valuePropertyName));
			}
		} catch (Exception e) {
			throw Reflections.convertReflectionExceptionToUnchecked(e);
		}

		return map;
	}

	/**
	 * <P>提取集合中的对象的一个属性(通过Getter函数), 组合成List.</P>
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 * @return List 组装后的List集合对象.
	 */
	@SuppressWarnings("unchecked")
	public static List extractToList(final Collection collection, final String propertyName) {
		List list = new ArrayList(collection.size());

		try {
			for (Object obj : collection) {
				// 获取属性值
				list.add(PropertyUtils.getProperty(obj, propertyName));
			}
		} catch (Exception e) {
			throw Reflections.convertReflectionExceptionToUnchecked(e);
		}

		return list;
	}

	/**
	 * <P>提取集合中的对象的一个属性(通过Getter函数), 组合成由分割符分隔的字符串.</P>
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 * @param separator 分隔符.
	 * @return String 组装后的String对象
	 */
	public static String extractToString(final Collection collection, final String propertyName, final String separator) {
		List list = extractToList(collection, propertyName);
		// Iterable --> Collection --> List
		return StringUtils.join(list, separator);
	}

	/**
	 * <P>转换Collection所有元素(通过toString())为String, 中间以 separator分隔</P>
	 * @param collection 来源集合.
	 * @param separator 分隔符.
	 * @return String 组装后的String对象
	 */
	public static String convertToString(final Collection collection, final String separator) {
		return StringUtils.join(collection, separator);
	}

	/**
	 * <P>转换Collection所有元素(通过toString())为String, 每个元素的前面加入prefix，后面加入postfix，如<div>mymessage</div></P>
	 * @param collection 来源集合.
	 * @param prefix 前缀.
	 * @param postfix 后缀.
	 * @return String 组装后的String对象
	 */
	public static String convertToString(final Collection collection, final String prefix, final String postfix) {
		StringBuilder builder = new StringBuilder();
		for (Object o : collection) {
			builder.append(prefix).append(o).append(postfix);
		}
		return builder.toString();
	}

	/**
	 * <P>判断集合元素是否为空</P>
	 * @param collection 需要进行判断的集合对象.
	 * @return Boolean 是否为空,true or false
	 */
	public static boolean isEmpty(Collection collection) {
		return (collection == null || collection.isEmpty());
	}

	/**
	 * <P>取得Collection的第一个元素，如果collection为空返回null.</P>
	 * @param collection<T> 需要进行判断的集合对象.
	 * @return <T> T 这里用到了泛型的知识,如果不清楚,请自行查找JavaAPI.使用泛型声明方法的话,需要在返回值类型之前声明该类型,例如<T> 这样的话返回值类型可以写为T,否则报错.
	 */
	public static <T> T getFirst(Collection<T> collection) {
		if (isEmpty(collection)) {
			return null;
		}

		// 使用iterator以后的next即为第一个元素
		return collection.iterator().next();
	}

	/**
	 * <P>获取Collection的最后一个元素 ，如果collection为空返回null.</P>
	 * @param collection<T> 需要进行判断的集合对象.
	 * @return <T> T 这里用到了泛型的知识,如果不清楚,请自行查找JavaAPI.使用泛型声明方法的话,需要在返回值类型之前声明该类型,例如<T> 这样的话返回值类型可以写为T,否则报错.
	 */
	public static <T> T getLast(Collection<T> collection) {
		if (isEmpty(collection)) {
			return null;
		}

		//当类型为List时，直接取得最后一个元素 。
		if (collection instanceof List) {
			List<T> list = (List<T>) collection;
			return list.get(list.size() - 1);
		}

		//其他类型通过iterator滚动到最后一个元素.
		Iterator<T> iterator = collection.iterator();
		while (true) {
			T current = iterator.next();
			if (!iterator.hasNext()) {
				return current;
			}
		}
	}

	/**
	 * <P>返回a+b的新List.</P>
	 * @param a 加集合
	 * @param b 被加集合
	 * @return <T> List<T> 这里用到了泛型的知识,如果不清楚,请自行查找JavaAPI.使用泛型声明方法的话,需要在返回值类型之前声明该类型,例如<T> 这样的话返回值类型可以写为T,否则报错.
	 */
	public static <T> List<T> union(final Collection<T> a, final Collection<T> b) {
		List<T> result = new ArrayList<T>(a);
		// 强转为List以后,调用addAll直接进行两个集合的并集
		result.addAll(b);
		return result;
	}

	/**
	 * <P>返回a-b的新List.</P>
	 * @param a 减集合
	 * @param b 被减集合
	 * @return <T> List<T> 这里用到了泛型的知识,如果不清楚,请自行查找JavaAPI.使用泛型声明方法的话,需要在返回值类型之前声明该类型,例如<T> 这样的话返回值类型可以写为T,否则报错.
	 */
	public static <T> List<T> subtract(final Collection<T> a, final Collection<T> b) {
		List<T> list = new ArrayList<T>(a);
		for (T element : b) {
			list.remove(element);
		}

		return list;
	}

	/**
	 * <P>返回a与b的交集的新List.</P>
	 * @param a 需要判断的集合
	 * @param b 被判断集合
	 * @return <T> List<T> 这里用到了泛型的知识,如果不清楚,请自行查找JavaAPI.使用泛型声明方法的话,需要在返回值类型之前声明该类型,例如<T> 这样的话返回值类型可以写为T,否则报错.
	 */
	public static <T> List<T> intersection(Collection<T> a, Collection<T> b) {
		List<T> list = new ArrayList<T>();

		for (T element : a) {
			if (b.contains(element)) {
				list.add(element);
			}
		}
		return list;
	}
}
