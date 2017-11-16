/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 赵庆辉 2015-3-12 9:06:26
 */
package com.wf.ssm.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * <P>Properties文件载入工具类. 可载入多个properties文件, 相同的属性在最后载入的文件中的值将会覆盖之前的值，但以System的Property优先.</P>
 * 
 * @version 1.0
 * @author wangpf 2015-3-12 9:06:26
 * @since JDK 1.6
 */
public class PropertiesLoader {

	private static Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);

	private static ResourceLoader resourceLoader = new DefaultResourceLoader();

	private final Properties properties;

	/**
	 * <P>初始化properties对象,加载属性的配置文件</P>
	 * @param resourcesPaths 属性文件的路径数组
	 */
	public PropertiesLoader(String... resourcesPaths) {
		properties = loadProperties(resourcesPaths);
	}

	/**
	 * <P>获取加载完毕配置文件的properties对象</P>
	 * @return Properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * <P>取出Property，但以System的Property优先,取不到返回空字符串.</P>
	 * @param key 属性的key
	 * @return String
	 */
	private String getValue(String key) {
		String systemProperty = System.getProperty(key);
		if (systemProperty != null) {
			return systemProperty;
		}
		if (properties.containsKey(key)) {
	        return properties.getProperty(key);
	    }
	    return "";
	}

	/**
	 * <P>取出String类型的Property，但以System的Property优先,如果都为Null则抛出异常.</P>
	 * @param key 属性的key
	 * @return String
	 */
	public String getProperty(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return value;
	}

	/**
	 * <P>取出String类型的Property，但以System的Property优先.如果都为Null则返回Default值.</P>
	 * @param key 属性的key
	 * @param defaultValue 默认值
	 * @return String
	 */
	public String getProperty(String key, String defaultValue) {
		String value = getValue(key);
		return value != null ? value : defaultValue;
	}

	/**
	 * <P>取出Integer类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.</P>
	 * @param key 属性的key
	 * @return Integer
	 */
	public Integer getInteger(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		// 类型转换
		return Integer.valueOf(value);
	}

	/**
	 * <P>取出Integer类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常</P>
	 * @param key 属性的key
	 * @param defaultValue 默认值
	 * @return Integer
	 */
	public Integer getInteger(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Integer.valueOf(value) : defaultValue;
	}

	/**
	 * <P>取出Double类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.</P>
	 * @param key 属性的key
	 * @return Double
	 */
	public Double getDouble(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Double.valueOf(value);
	}

	/**
	 * <P>取出Double类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常</P>
	 * @param key 属性的key
	 * @param defaultValue 属性的默认值
	 * @return Double
	 */
	public Double getDouble(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Double.valueOf(value) : defaultValue;
	}

	/**
	 * <P>取出Boolean类型的Property，但以System的Property优先.如果都为Null抛出异常,如果内容不是true/false则返回false.</P>
	 * @param key 属性的key
	 * @return Boolean
	 */
	public Boolean getBoolean(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Boolean.valueOf(value);
	}

	/**
	 * <P>取出Boolean类型的Property，但以System的Property优先.如果都为Null则返回Default值,如果内容不为true/false则返回false.</P>
	 * @param key 属性的key
	 * @param defaultValue 属性的默认值
	 * @return Boolean
	 */
	public Boolean getBoolean(String key, boolean defaultValue) {
		String value = getValue(key);
		return value != null ? Boolean.valueOf(value) : defaultValue;
	}

	/**
	 * <P>载入多个文件, 文件路径使用Spring Resource格式.</P>
	 * @param resourcesPaths 属性文件的路径数组
	 * @return Properties 
	 */
	private Properties loadProperties(String... resourcesPaths) {
		Properties props = new Properties();

		for (String location : resourcesPaths) {
//			logger.debug("Loading properties file from:" + location);
			InputStream is = null;
			try {
				// 获取resource对象
				Resource resource = resourceLoader.getResource(location);
				// 根据resource对象来获取输入流
				is = resource.getInputStream();
				// UFT-8转码，解决配置文件中文乱码问题
				BufferedReader bf=new BufferedReader(new InputStreamReader(is,"utf-8"));
//				props.load(is);
				props.load(bf);
			} catch (IOException ex) {
				logger.info("Could not load properties from path:" + location + ", " + ex.getMessage());
			} finally {
				IOUtils.closeQuietly(is);
			}
		}
		return props;
	}
}
