package com.wf.ssm.common.utils;

import java.io.IOException;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;

/**
 * <P>以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候取出ApplicaitonContext</P>
 * 
 * @version 1.0
 * @author wangpf 2015-3-12 9:06:26
 * @since JDK 1.6
 */
@Service
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

	private static ApplicationContext applicationContext = null;

	private static Logger logger = LoggerFactory.getLogger(SpringContextHolder.class);

	/**
	 * <P>取得存储在静态变量中的ApplicationContext</P>
	 * 
	 * @return applicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		assertContextInjected();
		return applicationContext;
	}
	
	/**
	 * <P>获取系统根目录</P>
	 * 
	 * @return 系统根目录
	 */
	public static String getRootRealPath(){
		String rootRealPath ="";
		try {
			rootRealPath=getApplicationContext().getResource("").getFile().getAbsolutePath();
		} catch (IOException e) {
			logger.warn("获取系统根目录失败");
		}
		return rootRealPath;
	}
	
	/**
	 * <P>获取资源根目录</P>
	 * 
	 * @return 资源根目录
	 */
	public static String getResourceRootRealPath(){
		String rootRealPath ="";
		try {
			rootRealPath=new DefaultResourceLoader().getResource("").getFile().getAbsolutePath();
		} catch (IOException e) {
			logger.warn("获取资源根目录失败");
		}
		return rootRealPath;
	}
	

	/**
	 * <P>从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型</P>
	 * 
	 * @param name beanId
	 * @return bean对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		assertContextInjected();
		return (T) applicationContext.getBean(name);
	}

	/**
	 * <P>从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型</P>
	 * 
	 * @param requiredType bean类型
	 * @return bean对象
	 */
	public static <T> T getBean(Class<T> requiredType) {
		assertContextInjected();
		return applicationContext.getBean(requiredType);
	}

	/**
	 * <P>清除SpringContextHolder中的ApplicationContext为Null</P>
	 * 
	 * @return void
	 */
	public static void clearHolder() {
		if (logger.isDebugEnabled()){
			logger.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
		}
		applicationContext = null;
	}

	/**
	 * <P>实现ApplicationContextAware接口, 注入Context到静态变量中</P>
	 * 
	 * @param applicationContext
	 * @return void
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
//		logger.debug("注入ApplicationContext到SpringContextHolder:{}", applicationContext);

		if (SpringContextHolder.applicationContext != null) {
			logger.info("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:" + SpringContextHolder.applicationContext);
		}

		SpringContextHolder.applicationContext = applicationContext; // NOSONAR
	}

	/**
	 * <P>实现DisposableBean接口, 在Context关闭时清理静态变量</P>
	 * 
	 * @return void
	 */
	@Override
	public void destroy() throws Exception {
		SpringContextHolder.clearHolder();
	}

	/**
	 * <P>检查ApplicationContext不为空</P>
	 * 
	 * @return void
	 */
	private static void assertContextInjected() {
		Validate.validState(applicationContext != null, "applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder.");
	}
}