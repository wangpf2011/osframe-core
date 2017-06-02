/*
 * Copyright &copy; 2011-2020  Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 王磊 2015-03-09 11:55:39
 */
package com.wf.ssm.common.persistence.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;


/**
 * <P>自定义一个MyBatis注解 @Annotation</P>
 * <P>标识MyBatis的DAO层<br>
 * 提供给：spring-context-mybatis.xml方便{org.mybatis.spring.mapper.MapperScannerConfigurer}的扫描
 * </P>
 * <P>注解用法：@MyBatisDao </P>
 * @version 1.0
 * @author 王磊 2015-02-09 11:55:39
 * @since JDK 1.6
 */
@Retention(RetentionPolicy.RUNTIME)//修饰注解的元注解
@Target(ElementType.TYPE)//修饰注解的元注解
@Documented
@Component
public @interface MyBatisDao {
	
}