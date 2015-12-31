package org.haitao.common.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class TimeUtil {
	
	public final static String FORMAT_YEAR = "yyyy";
	public final static String FORMAT_MONTH_DAY = "MM月dd日";
	
	public final static String FORMAT_DATE = "yyyy-MM-dd";
	public final static String FORMAT_TIME = "HH:mm";
	public final static String FORMAT_MONTH_DAY_TIME = "MM月dd日  hh:mm";
	public final static String FORMAT_MONTH_DAY_TIME_EN = "MM-dd hh:mm";
	
	public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
	public final static String FORMAT_DATE1_TIME = "yyyy/MM/dd HH:mm";
	public final static String FORMAT_DATE_TIME_SECOND = "yyyy_MM_dd_HH_mm_ss";
	
	private static SimpleDateFormat sdf = new SimpleDateFormat();
	private static final int YEAR = 365 * 24 * 60 * 60;// 年
	private static final int MONTH = 30 * 24 * 60 * 60;// 月
	private static final int DAY = 24 * 60 * 60;// 天
	private static final int HOUR = 60 * 60;// 小时
	private static final int MINUTE = 60;// 分钟

	/**
	 * 根据时间戳获取描述性时间，如3分钟前，1天前
	 * 
	 * @param timestamp
	 *            时间戳 单位为毫秒
	 * @return 时间字符串
	 */
	public static String getDescriptionTimeFromTimestamp(long timestamp) {
		long currentTime = System.currentTimeMillis();
		long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
		System.out.println("timeGap: " + timeGap);
		String timeStr = null;
		if (timeGap > YEAR) {
			timeStr = timeGap / YEAR + "年前";
		} else if (timeGap > MONTH) {
			timeStr = timeGap / MONTH + "个月前";
		} else if (timeGap > DAY) {// 1天以上
			timeStr = timeGap / DAY + "天前";
		} else if (timeGap > HOUR) {// 1小时-24小时
			timeStr = timeGap / HOUR + "小时前";
		} else if (timeGap > MINUTE) {// 1分钟-59分钟
			timeStr = timeGap / MINUTE + "分钟前";
		} else {// 1秒钟-59秒钟
			timeStr = "刚刚";
		}
		return timeStr;
	}

 	
 	/**
 	 * long to date 
 	 * 转成自己的格式
 	 * @param currentTime
 	 * @param formatType
 	 * @return
 	 */
 	public static Date longToDate(long currentTime, String formatType){
 		Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
 		String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
 		Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
 		return date;
 	}
 	/**
 	 * long to string 的date
 	 * @param currentTime
 	 * @param formatType
 	 * @return
 	 */
 	public static String longToString(long currentTime, String formatType){
 		String strTime="";
		Date date = longToDate(currentTime, formatType);// long类型转成Date类型
		strTime = dateToString(date, formatType); // date类型转成String 
 		return strTime;
 	}
 	
 	/**
 	 * string 转long
 	 * warn strTime的时间格式和formatType的时间格式必须相同
 	 * @param strTime
 	 * @param formatType
 	 * @return
 	 */
 	public static long stringToLong(String strTime, String formatType){
 		Date date = stringToDate(strTime, formatType); // String类型转成date类型
 		if (date == null) {
 			return 0;
 		} else {
 			long currentTime = dateToLong(date); // date类型转成long类型
 			return currentTime;
 		}
 	}
 	/**
 	 * string类型转换为date类型
 	 * @param strTime
 	 * @param formatType
 	 * @return
 	 */
 	public static Date stringToDate(String strTime, String formatType){
 		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
 		Date date = null;
 		try {
			date = formatter.parse(strTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		return date;
 	}
 

 	/**
 	 * date 转long
 	 * @param date
 	 * @return
 	 */
 	public static long dateToLong(Date date) {
 		return date.getTime();
 	}
	 	

 	/**
 	 * date to string
 	 * @param data
 	 * @param formatType
 	 * @return
 	 */
 	public static String dateToString(Date data, String formatType) {
 		return new SimpleDateFormat(formatType).format(data);
 	}
 	
	/**
	 * 获取 time 返回 yy-MM-dd HH:mm
	 * @param time
	 * @return
	 */
	public static String getTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
		return format.format(new Date(time));
	}
	/**
	 * 获取 小时和分钟
	 * @param time
	 * @return
	 */
	public static String getHourAndMin(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(new Date(time));
	}

	/**
	 * 和现在比较是 返回今天 昨天 前天
	 * @param timesamp
	 * @return
	 */
	public static String getZoneTime(long timesamp) {
		long clearTime = timesamp;
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(clearTime);
		int temp = Integer.parseInt(sdf.format(today))
				- Integer.parseInt(sdf.format(otherDay));
		
		switch (temp) {
		case 0:
			result = "今天 " ;
			break;
		case 1:
			result = "昨天 " ;
			break;
		case 2:
			result = "前天 ";
			break;
			
		default:
			SimpleDateFormat format = new SimpleDateFormat("MM-dd");
			result = format.format(new Date(timesamp));;
			break;
		}
		
		return result;
	}
	
	/**
	 * 获取当前日期的指定格式的字符串
	 * 
	 * @param format
	 *            指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
	 * @return
	 */
	public static String getCurrentTime(String format) {
		if (format == null || format.trim().equals("")) {
			sdf.applyPattern(FORMAT_DATE_TIME);
		} else {
			sdf.applyPattern(format);
		}
		return sdf.format(new Date());
	}
	
	/**
	 * 是否是现在的多时候小时以后
	 * @param newDate
	 * @param hours
	 * @return
	 */
	public static boolean isAfter(Date olddate,Date newDate,int hours) {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(olddate);  
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + hours);  
        Date da= cal.getTime();   
		return newDate.after(da);  
	} 
}