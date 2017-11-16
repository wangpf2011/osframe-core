/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 赵庆辉 2015-3-12 9:06:26
 */
package com.wf.ssm.common.utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * <P>处理缓存的工具类</P>
 * 
 * @version 1.0
 * @author wangpf2015-3-12 9:06:26
 * @since JDK 1.6
 */
public class CacheUtils {
	
	// 从spring的配置bean中获取到CacheManager对象的实例.由spring进行管理.
	private static CacheManager cacheManager = ((CacheManager)SpringContextHolder.getBean("cacheManager"));

	private static final String SYS_CACHE = "sysCache";

	/**
	 * <P>往Cache对象中获取一个Element对象的值</P>
	 * @param key  要获取的Element元素的key;
	 * @return void.
	 */
	public static Object get(String key) {
		return get(SYS_CACHE, key);
	}

	/**
	 * <P>往Cache对象中存入一个Element对象</P>
	 * @param key  要存放在SYS_CACHE缓存对象中的Element元素的key;
	 * @param value  要存放在SYS_CACHE缓存对象中的Element元素的value;
	 * @return void.
	 */
	public static void put(String key, Object value) {
		put(SYS_CACHE, key, value);
	}

	/**
	 * <P>从cache中删除元素</P>
	 * @param key  存放在SYS_CACHE对象中的Element元素的key;
	 * @return void.
	 */
	public static void remove(String key) {
		remove(SYS_CACHE, key);
	}
	
	/**
	 * <P>从cache中获取某个ELement元素的值</P>
	 * @param cacheName  cache对象的名称;
	 * @param key  存在在cache对象中的element对象的key;
	 * @return Object 返回Element对象的值,用Object形式.
	 */
	public static Object get(String cacheName, String key) {
		Element element = getCache(cacheName).get(key);
		return element==null?null:element.getObjectValue();
	}

	/**
	 * <P>往Cache对象中存入一个Element对象</P>
	 * @param cacheName  寄存ELement元素的cache对象名称;
	 * @param key  要存放在SYS_CACHE缓存对象中的Element元素的key;
	 * @param value  要存放在SYS_CACHE缓存对象中的Element元素的value;
	 * @return void.
	 */
	public static void put(String cacheName, String key, Object value) {
		Element element = new Element(key, value);
		getCache(cacheName).put(element);
	}
	
	/**
	 * <P>从cache中删除元素</P>
	 * @param cacheName  存放对象的cache;
	 * @param key  存放在cache对象中的Element元素的key;
	 * @return void.
	 */
	public static void remove(String cacheName, String key) {
		getCache(cacheName).remove(key);
	}
	
	/**
	 * <P>获得一个Cache，没有则创建一个。</P>
	 * @param cacheName  想要获取到的cache的名字;
	 * @return 返回一个Cache对象.
	 */
	private static Cache getCache(String cacheName){
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null){
			cacheManager.addCache(cacheName);
			cache = cacheManager.getCache(cacheName);
			cache.getCacheConfiguration().setEternal(true);
		}
		return cache;
	}
	
	/**
	 * <P>对外暴露的获取cache管理类对象的接口</P>
	 * @return 返回CacheManager实例
	 */
	public static CacheManager getCacheManager() {
		return cacheManager;
	}
	
}
