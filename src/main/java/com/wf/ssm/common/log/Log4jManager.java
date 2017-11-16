package com.wf.ssm.common.log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * <p>
 * 基于JMX动态配置Log4J日志级别，并控制Trace开关的MBean.</br>
 * 通常使用JMX来监控系统的运行状态或管理系统的某些方面，比如清空缓存、重新加载配置文件等</p>
 * 
 * @version 1.0 
 * @author wangpf  2015-03-11 11:22:00
 * @since JDK 1.6
 */

 
//通过annotation注册MBean到JMX
@ManagedResource(objectName = Log4jManager.MBEAN_NAME, description = "Log4j Management Bean")
public class Log4jManager {

	/**
	 * <p>Log4jManager的Mbean的注册名称(唯一标识符).</p>
	 */
	public static final String MBEAN_NAME = "log4j:name=Log4j";

	private static org.slf4j.Logger managerLogger = LoggerFactory.getLogger(Log4jManager.class);

	private String projectLoggerName;
	/**
	 * <p>得到根Logger的日志级别.</p>
	 * @return String 日志级别
	 */
	@ManagedAttribute(description = "Level of the root logger")
	public String getRootLoggerLevel() {
		Logger logger = Logger.getRootLogger();
		return logger.getEffectiveLevel().toString();
	}
	/**
	 * <p>设置根Logger的日志级别.</p>
	 * @param newLevel 日志级别(等级可分为OFF、FATAL、ERROR、WARN、INFO、DEBUG、ALL)
	 */
	@ManagedAttribute
	public void setRootLoggerLevel(String newLevel) {
		Logger logger = Logger.getRootLogger();
		Level level = Level.toLevel(newLevel);
		logger.setLevel(level);
		managerLogger.info("设置Root Logger 级别为{}", newLevel);
	}

	/**
	 * <p>获得项目默认logger的级别.</br>
	 * 项目默认logger名称通过#setProjectLoggerName(String)配置.</p>
	 * @return String 日志级别
	 */
	@ManagedAttribute(description = "Level of the project default package logger")
	public String getProjectLoggerLevel() {
		if (projectLoggerName != null) {
			return getLoggerLevel(projectLoggerName);
		}

		return null;
	}

	/**
	 * <p>设置项目默认logger的级别.</br>
	 * 项目默认logger名称通过#setProjectLoggerName(String)配置.</p>
	 * @param newLevel 日志级别
	 */
	@ManagedAttribute
	public void setProjectLoggerLevel(String newLevel) {
		if (projectLoggerName != null) {
			setLoggerLevel(projectLoggerName, newLevel);
		}
	}

	/**
	 * <p>根据logger名称获取Logger的日志级别.</p>
	 * @param loggerName logger名称
	 * @return String 日志级别
	 */
	@ManagedOperation(description = "Get logging level of the logger")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "loggerName", description = "Logger name") })
	public String getLoggerLevel(String loggerName) {
		Logger logger = Logger.getLogger(loggerName);
		return logger.getEffectiveLevel().toString();
	}

	/**
	 * <p>根据logger名称设置Logger的日志级别.</br>
	 * 如果日志级别名称错误, 设为DEBUG.</p>
	 * @param loggerName logger名称
	 * @param newLevel 日志级别
	 */
	@ManagedOperation(description = "Set new logging level to the logger")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "loggerName", description = "Logger name"),
			@ManagedOperationParameter(name = "newlevel", description = "New level") })
	public void setLoggerLevel(String loggerName, String newLevel) {
		Logger logger = Logger.getLogger(loggerName);
		Level level = Level.toLevel(newLevel);
		logger.setLevel(level);
		managerLogger.info("设置{}级别为{}", loggerName, newLevel);
	}

	/**
	 * <p>根据log4j.properties中的定义, 设置项目默认的logger名称, 如com.lnint.jess.</p>
	 * @param projectLoggerName 项目默认logger名称
	 */
	public void setProjectLoggerName(String projectLoggerName) {
		this.projectLoggerName = projectLoggerName;
	}

}