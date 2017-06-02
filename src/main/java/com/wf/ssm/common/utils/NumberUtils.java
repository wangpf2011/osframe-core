/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 葛松 2015-06-17 9:06:26
 */
package com.wf.ssm.common.utils;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * <P>线程相关工具类</P>
 * 
 * @version 1.0
 * @author 葛松 2015-06-17 9:06:26
 * @since JDK 1.6
 */
public class NumberUtils {
	
	/**
	 * <p>返回2位小数</p>
	 * @param val
	 * @return 包含两位小数的字符串
	 */
	public static String getFloatStr(String val) {
		String s ="";
		float tempfloat = Float.parseFloat(val);
		DecimalFormat decimalFormat=new DecimalFormat("##0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		s = decimalFormat.format(tempfloat);//format 返回的是字符串
		return s;
	}

	/**
	 * @param val值字符串，dot保留几位小数(0至3)
	 * @return 包含几位小数的字符串，四舍五入
	 */
	public static String getFloatStr(String val,int dot) {
		String s ="";
		float tempfloat = Float.parseFloat(val);
		DecimalFormat decimalFormat=new DecimalFormat("##0.00");//构造方法的字符格式这里如果小数不足,会以0补足.
		if(dot==1)
			decimalFormat=new DecimalFormat("##0.0");
		else if(dot==2)
			decimalFormat=new DecimalFormat("##0.00");
		else if(dot==3)
			decimalFormat=new DecimalFormat("##0.000");
		else if(dot==0)
			decimalFormat=new DecimalFormat("##");

		s = decimalFormat.format(tempfloat);//format 返回的是字符串
		return s;
	}
	
	/**
	 * 长数字精简显示，长度6位，小数两位
	 * add by weisu 16.6.24
	 */
	public static String getSimpleDouble(double val){
		String res;
		String unit = "";
		res = new DecimalFormat("#0.00").format(val);
		if(res.length()<8)
			return res;
		
		val = Double.parseDouble(res)/10000;
		res = new DecimalFormat("#0.00").format(val);
		unit = "万";
		if(res.length()<8)
			return res + unit;
		
		val = Double.parseDouble(res)/10000;
		res = new DecimalFormat("#0.00").format(val);
		unit = "亿";
		
		return res + unit;
	}
	
	/**
	 * 长数字精简显示，长度4位
	 * add by weisu 16.6.24
	 */
	public static String getSimpleLong(long val){
		String res = val+"";
		String unit = "";
		if(res.length() < 6)
			return res;
		
		double d = Double.parseDouble(res)/10000;
		res = new DecimalFormat("#0.00").format(d);
		unit = "万";
		if(res.length()<8)
			return res + unit;
		
		d = Double.parseDouble(res)/10000;
		res = new DecimalFormat("#0.00").format(d);
		unit = "亿";
		
		return res + unit;
	}
	
	/** 
     * 判断是否为整数  
     * @param str 传入的字符串  
     * @return 是整数返回true,否则返回false  
	*/  
	  public static boolean isInteger(String str) {    
	    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");    
	    return pattern.matcher(str).matches();    
	  } 
}
