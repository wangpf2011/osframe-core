package com.wf.ssm.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * <P>日期工具类, 继承org.apache.commons.lang.time.DateUtils类</P>
 * 
 * @version 1.0
 * @author wangpf 2015-3-12 9:06:26
 * @since JDK 1.6
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {
	
	private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" };

	/**
	 * <P>得到当前日期字符串 格式(yyyy-MM-dd)</P>
	 * @return String 当前日期的字符串
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	/**
	 * <P>得到某一日期 之前N天的日期  格式(yyyy-MM-dd)</P>
	 * @return String 当前日期的字符串
	 */
	public static String getDateBefore(Date d, int day) {  
        Calendar now = Calendar.getInstance();  
        now.setTime(d);  
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);  
        return formatDate(now.getTime()); 
	}
	/**
	 * <P>得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"</P>
	 * @return String 当前日期的字符串
	 */
	public static String getDate(String pattern) {
		// 实际使用的还是DateFormatUtils的方法
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * <P>得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"</P>
	 * @return String 当前日期的字符串
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		// 利用了java的可变参数
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 * <P>得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）</P>
	 * @return String 当前日期的字符串
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * <P>得到日期时间字符串，转换格式（HH:mm:ss）</P>
	 * @return String 当前日期的字符串
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * <P>得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）</P>
	 * @return String 当前日期的字符串
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * <P>得到当前年份的字符串</P>
	 * @return String 当前年份的字符串
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * <P>得到当前月份的字符串</P>
	 * @return String 当前月份的字符串
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * <P>得到当前天的字符串</P>
	 * @return String 当前天的字符串
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * <P>得到当前星期的字符串</P>
	 * @return String 当前星期的字符串
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * <P>日期型字符串转化为日期 格式</P>
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" }
	 * @return Date 转换后的日期
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * <P>获取过去的天数</P>
	 * @param date 表示某个具体日期的date对象
	 * @return long 过去的天数,表示当前时间和date表示的时间的天数差
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}
	
	/**
	 * <P>设置开始时间,格式如下:1988-03-26 00:00:00</P>
	 * @param date 表示某个具体日期的date对象
	 * @return Date 设置好了的开始时间对象
	 */
	public static Date getDateStart(Date date) {
		if(date==null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date= sdf.parse(formatDate(date, "yyyy-MM-dd")+" 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * <P>设置结束时间,格式如下:1988-03-26 23:59:59</P>
	 * @param date 表示某个具体日期的date对象
	 * @return Date 设置好了的结束时间对象
	 */
	public static Date getDateEnd(Date date) {
		if(date==null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date= sdf.parse(formatDate(date, "yyyy-MM-dd") +" 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * <P>获得输入日期所在的 年份和周数</P>
	 * @param date 表示某个具体日期的date对象
	 * @return String 年份和周数的字符串,例如:201501
	 */
	public static String getYearWeeks(Date date) {
		if(date==null) {
			return null;
		}
		  Calendar calendar = Calendar.getInstance();
		  calendar.setFirstDayOfWeek(Calendar.MONDAY);
		  // 设置时间
		  calendar.setTime(date);
		  // 获取年份
		  int year= calendar.get(Calendar.YEAR);
		  // WEEK_OF_YEAR表示这一年的第几周,如果小于10,前面加上'0'
		  String week= calendar.get(Calendar.WEEK_OF_YEAR)<10?('0'+String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR))):String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));
		  
		 return String.valueOf(year+week);
	}
	
	/**
	 * <P>根据输入的周数获得本周开始时间  201404,获得当前周- 周一的日期 和周日的日期</P>
	 * @param date 表示某个具体日期的date对象
	 * @return String 年份和周数的字符串,例如:201501
	 */
	public static String getWeekDates(String weeksn) {
		if (weeksn == null) {
			return null;
		}
		//
		String begindate = "";
		String endindate = "";
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		// 截取前四位表示年份
		calendar.set(Calendar.YEAR, Integer.parseInt(weeksn.substring(0, 4)));
		// 截取后两位表示今年的周数
		calendar.set(Calendar.WEEK_OF_YEAR,Integer.parseInt(weeksn.substring(4, 6)));
		// SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, and SATURDAY.
		// 值从1开始计数.所以2表示星期一
		calendar.set(Calendar.DAY_OF_WEEK, 2);

		begindate = formatDate(calendar.getTime());
		// 6表示星期五
		calendar.add(Calendar.DAY_OF_WEEK, 6);
		endindate = formatDate(calendar.getTime());
		return begindate + "," + endindate;
	}
	    
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * <P>获得当前月--开始日期</P>
	 * @param date 某个日期的字符串表现形式(年月日格式)
	 * @return Date 当前月份的第一天
	 */
	public static Date getMinMonthDate(String date) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(dateFormat.parse(date));
			// calendar.getActualMinimum(Calendar.DAY_OF_MONTH)这个方法返回的就是当前月份的开始值
			calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return calendar.getTime();
	}
	
	/**
	 * <P>获得当前月--结束日期</P>
	 * @param date 某个日期的字符串表现形式(年月日格式)
	 * @return Date 当前月份的结束日期
	 */
	public static Date getMaxMonthDate(String date) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(dateFormat.parse(date));
			// calendar.getActualMaximum(Calendar.DAY_OF_MONTH) 这个方法实际返回最后一天的int值
			calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return calendar.getTime();
	}
	/**
	 * <P>获得一个月有多少天</P>
	 * @param year 年
	 * @param month 月
	 * @return int
	 */
	public static int getMonthDays(int year,int month){
		Calendar time=Calendar.getInstance(); 
		time.clear(); 
		time.set(Calendar.YEAR,year); 
		//year年
		time.set(Calendar.MONTH,month-1);
		//Calendar对象默认一月为0,month月 
		return time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
	}
	/**
	 * <P>计算时差（分钟）</P>
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public static long getDistanceMin(Date starttime, Date endtime) {
        long min = 0;
        if(starttime == null || endtime == null) return min;
    	
        try {
            long start = starttime.getTime();  
            long end = endtime.getTime();  
            long diff = end - start;  
            min = diff / (1000 * 60);
        }catch (Exception e) {
            e.printStackTrace();  
            return min;
        }  
        return min;  
    } 
	/**
	 * <P>计算时差（秒）</P>
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public static long getDistanceSecond(Date starttime, Date endtime) {
        long second = 0;
        if(starttime == null || endtime == null) return second;
    	
        try {
            long start = starttime.getTime();  
            long end = endtime.getTime();  
            long diff = end - start;  
            second = diff / 1000;
        }catch (Exception e) {
            e.printStackTrace();  
            return second;
        }  
        return second;  
    } 
	/**
	 * <P>月份加减</P>
	 * @param month 加减的数量（YYYY-MM)格式
	 * @return
	 */
	public static String addMonth(int month) {
		
		Calendar calendar=Calendar.getInstance();   
	    calendar.setTime(new Date()); 
	    calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)+month); 
	    
	    String date = calendar.get(Calendar.YEAR)+"-";
	    if(calendar.get(Calendar.MONTH) <9)
	    	date += "0"+(calendar.get(Calendar.MONTH)+1)+"-";
    	else 
    		date += (calendar.get(Calendar.MONTH)+1)+"-";
	    if(calendar.get(Calendar.DAY_OF_MONTH) <10)
	    	date += "0"+calendar.get(Calendar.DAY_OF_MONTH);
    	else 
    		date += calendar.get(Calendar.DAY_OF_MONTH);
	    
	    return date;
	}
	/**
	 * <P>获得日期 前几个月后几个月的日期</P>
	 * @param month 加减的数量（-N 或者N )格式
	   @param cdate 日期参数
	 * @return (yyyy-MM-dd HH:mm:ss)
	 */
	public static String getMonthDay(Date cdate,int month) {
		
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(cdate);//把当前时间赋给日历
		calendar.add(Calendar.MONTH, month); //设置为月
		dBefore = calendar.getTime(); //

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
		String defaultStartDate = sdf.format(dBefore); //格式化前3月的时间

	    return defaultStartDate;
	}
	/**
     * 获取某年某周的起始时间
     * 
     * @param year
     * @param weekindex
     * @return
     */
	public static String getStartDayOfWeekNo(int year,int weekNo){
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +
               cal.get(Calendar.DAY_OF_MONTH);    
        
    }
	/**
     * 获取某年某周的结束时间
     * 
     * @param year
     * @param weekindex
     * @return
     */
	public static String getEndDayOfWeekNo(int year,int weekNo){
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        cal.add(Calendar.DAY_OF_WEEK, 6);
        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +
               cal.get(Calendar.DAY_OF_MONTH);    
    }
	 private static Calendar getCalendarFormYear(int year){
	        Calendar cal = Calendar.getInstance();
	        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);      
	        cal.set(Calendar.YEAR, year);
	        return cal;
	 }
	 // 获取当前时间所在年的最大周数
    public static int getMaxWeekNumOfYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);

        return getWeekOfYear(c.getTime());
    }
 // 获取当前时间所在年的周数
    public static int getWeekOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);

        return c.get(Calendar.WEEK_OF_YEAR);
    }
    //获取两个日期相差的天数
    public static int daysOfTwo(Date fDate, Date oDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(oDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1;
     }
    /**
     * 得到两日期相差几个月
     * 
     * @param String
     * @return
     */
    public static int getMonth(String startDate, String endDate) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        int monthday=0;
		try {
			Date startDate1 = f.parse(startDate);
            //开始时间与今天相比较
            Date endDate1 = new Date();
            Calendar starCal = Calendar.getInstance();
            starCal.setTime(startDate1);

            int sYear = starCal.get(Calendar.YEAR);
            int sMonth = starCal.get(Calendar.MONTH);
            int sDay = starCal.get(Calendar.DATE);

            Calendar endCal = Calendar.getInstance();
            endCal.setTime(endDate1);
            int eYear = endCal.get(Calendar.YEAR);
            int eMonth = endCal.get(Calendar.MONTH);
            int eDay = endCal.get(Calendar.DATE);

            monthday = ((eYear - sYear) * 12 + (eMonth - sMonth));
            if (sDay < eDay) {
                monthday = monthday + 1;
            }
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return monthday;
    }
}
