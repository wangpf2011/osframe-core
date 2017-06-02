/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 赵庆辉 2015-3-12 9:06:26
 */
package com.wf.ssm.common.utils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * <P>封装各种生成唯一性ID算法的工具类.</P>
 * 
 * @version 1.0
 * @author 赵庆辉 2015-3-12 9:06:26
 * @since JDK 1.6
 */
public class Identities {

	private static SecureRandom random = new SecureRandom();

	/**
	 * <P>封装JDK自带的UUID, 通过Random数字生成, 中间有-分割</P>
	 * @return String 生成的一个随机数
	 */
	public static String uuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * <P>封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.</P>
	 * @return String 生成的一个随机数
	 */
	public static String uuid2() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * <P>使用SecureRandom随机生成Long. </P>
	 * @return long 生成的一个随机数
	 */
	public static long randomLong() {
		return Math.abs(random.nextLong());
	}

	/**
	 * <P>基于Base62编码的SecureRandom随机生成bytes. </P>
	 * @param length 数据长度
	 * @return String 生成的一个随机数
	 */
	public static String randomBase62(int length) {
		byte[] randomBytes = new byte[length];
		random.nextBytes(randomBytes);
		return Encodes.encodeBase62(randomBytes);
	}
	
}


