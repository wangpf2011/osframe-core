/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 赵庆辉 2015-3-12 9:06:26
 */
package com.wf.ssm.common.utils;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.wf.ssm.common.utils.Encodes;
import com.wf.ssm.core.sys.utils.UserUtils;

/**
 * <P>封装各种生成唯一性ID算法的工具类.</P>
 * 
 * @version 1.0
 * @author wangpf
 * @since JDK 1.6
 */
@Service
@Lazy(false)
public class IdGen implements SessionIdGenerator {
	
	private static SecureRandom random = new SecureRandom();
	
	/**
	 * <P>封装JDK自带的UUID, 通过Random数字生成, 中间有-分割</P>
	 * @return String 生成的一个随机数
	 */
	public static String uuid() {
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
	
	/**
	 * <P>Activiti ID 生成 </P>
	 * @return String 生成的一个随机数
	 */
	public String getNextId() {
		return IdGen.uuid();
	}

	/**
	 * <P>Activiti ID 生成 </P>
	 * @param session shiro的session对象
	 * @return Serializable 生成的一个序列化对象
	 */
	@Override
	public Serializable generateId(Session session) {
		return IdGen.uuid();
	}

	/**
	 * <P>生成随机数</P>
	 * @param length 数据长度
	 * @return String 生成的一个随机数
	 */
	public static String getRandomWithZero(int length){
		String randomStr = "";
		 int a[] = new int[length];
		 for(int i=0;i<a.length;i++) {
			 a[i] = (int)(10*(Math.random()));
			 randomStr = randomStr + a[i];
		 }
		return randomStr;
	}
	//
	/**
	 * <P>根据序列名称获得序列值</P>
	 * @param seqName 自定义序列名称
	 * @return String 生成的下一个序列值
	 */
	public static String findNextSeqByName(String seqName){
		return UserUtils.findNextSeqByName(seqName);
	}

	/**
	 * <P>生成业务流水号（时间+length长度随机数）</P>
	 * @return String 序列号
	 */
	public static String getBusiFlowNo(int length) {
		String flowno = DateFormatUtils.format(new Date(), "yyMMddHHmmss");
		flowno += getRandomWithZero(length);
		return flowno;
	}
}
