package com.wf.ssm.common.beanvalidator;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * <p>基于JSR303 Validator(Hibernate Validator)的Bean校验工具类.</br>
 * 
 * ConstraintViolation中包含propertyPath(属性名), message(错误信息) 和invalidValue(无效值)等信息.</br>
 * 提供了各种convert方法，适合不同的需求:</br>
 * 1. List&lt;String>, String内容为message</br>
 * 2. List&lt;String>, String内容为propertyPath + separator + message</br>
 * 3. Map&lt;propertyPath, message></br>
 * 
 * 详情见wiki: https://github.com/springside/springside4/wiki/HibernateValidator</p>
 * @version 1.0
 * @author wangpf  2015-03-11 10:40:00
 * @since JDK 1.6
 */
public class BeanValidators {

	/**
	 * <p>调用JSR303的validate方法, 验证失败时抛出ConstraintViolationException.</p>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void validateWithException(Validator validator, Object object, Class<?>... groups)
			throws ConstraintViolationException {
		Set constraintViolations = validator.validate(object, groups);
		if (!constraintViolations.isEmpty()) {
			throw new ConstraintViolationException(constraintViolations);
		}
	}

	/**
	 * <p>辅助方法, 转换ConstraintViolationException中的Set&lt;ConstraintViolations>中为List&lt;message>.</p>
	 */
	public static List<String> extractMessage(ConstraintViolationException e) {
		return extractMessage(e.getConstraintViolations());
	}

	/**
	 * <p>辅助方法, 转换Set&lt;ConstraintViolation>为List&lt;message></p>
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> extractMessage(Set<? extends ConstraintViolation> constraintViolations) {
		List<String> errorMessages = Lists.newArrayList();
		for (ConstraintViolation violation : constraintViolations) {
			errorMessages.add(violation.getMessage());
		}
		return errorMessages;
	}

	/**
	 * <p>辅助方法, 转换ConstraintViolationException中的Set&lt;ConstraintViolations>为Map&lt;property, message>.</p>
	 */
	public static Map<String, String> extractPropertyAndMessage(ConstraintViolationException e) {
		return extractPropertyAndMessage(e.getConstraintViolations());
	}

	/**
	 * <p>辅助方法, 转换Set&lt;ConstraintViolation>为Map&lt;property, message>.</p>
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, String> extractPropertyAndMessage(Set<? extends ConstraintViolation> constraintViolations) {
		Map<String, String> errorMessages = Maps.newHashMap();
		for (ConstraintViolation violation : constraintViolations) {
			errorMessages.put(violation.getPropertyPath().toString(), violation.getMessage());
		}
		return errorMessages;
	}

	/**
	 * <p>辅助方法, 转换ConstraintViolationException中的Set&lt;ConstraintViolations>为List&lt;propertyPath message>.</p>
	 */
	public static List<String> extractPropertyAndMessageAsList(ConstraintViolationException e) {
		return extractPropertyAndMessageAsList(e.getConstraintViolations(), " ");
	}

	/**
	 * <p>辅助方法, 转换Set&lt;ConstraintViolations>为List&lt;propertyPath message>.</p>
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> extractPropertyAndMessageAsList(Set<? extends ConstraintViolation> constraintViolations) {
		return extractPropertyAndMessageAsList(constraintViolations, " ");
	}

	/**
	 * <p>辅助方法, 转换ConstraintViolationException中的Set&lt;ConstraintViolations>为List&lt;propertyPath +separator+ message>.</p>
	 */
	public static List<String> extractPropertyAndMessageAsList(ConstraintViolationException e, String separator) {
		return extractPropertyAndMessageAsList(e.getConstraintViolations(), separator);
	}

	/**
	 * <p>辅助方法, 转换Set&lt;ConstraintViolation>为List&lt;propertyPath +separator+ message>.</p>
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> extractPropertyAndMessageAsList(Set<? extends ConstraintViolation> constraintViolations,
			String separator) {
		List<String> errorMessages = Lists.newArrayList();
		for (ConstraintViolation violation : constraintViolations) {
			errorMessages.add(violation.getPropertyPath() + separator + violation.getMessage());
		}
		return errorMessages;
	}
}