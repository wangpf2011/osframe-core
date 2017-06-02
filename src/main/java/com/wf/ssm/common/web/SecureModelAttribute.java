/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 王朋飞 2015-03-12 12:02:25
 */
package com.wf.ssm.common.web;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <P>方法、属性注解定义</P>
 * 
 * @version 1.0
 * @author 王朋飞  2015-03-12 12:02:25
 * @since JDK 1.6
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SecureModelAttribute {

	String value() default "";
	
	String[] allowedField() default "*";
	
	String[] deniedField() default "";
	
	String[] clearFiled() default "";

}
