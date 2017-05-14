package org.haitao.common.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.util.Log;

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
	public final static String FORMAT_DATE_SECOND = "yyyy-MM-dd HH:mm:ss";
	
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
 	public static Date longToDate(long currentTime){
 		//Date dateOld = new Date(currentTime * 1000); // 根据long类型的毫秒数生命一个date类型的时间
 		Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
 		return dateOld;
 	}
 	/**
 	 * long to string 的date
 	 * @param currentTime
 	 * @param formatType
 	 * @return
 	 */
 	public static String longToString(long currentTime, String formatType){
 		String strTime="";
		Date date = longToDate(currentTime);// long类型转成Date类型
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
			// TODO Auto-generated catch block;
			Log.e("date error", e.toString());
		}
 		return date;
 	}
 

 	/**
 	 * date 转long
 	 * @param date
 	 * @return
 	 */
 	public static long dateToLong(Date date) {
 		//return date.getTime()/1000;
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
	/**
	 * 获取星期几 1 星期日
	 * @param d
	 * @param day
	 * @return
	 */
	public static int  getWeek(Date d) {  
		Calendar now = Calendar.getInstance();  
		now.setTime(d);  
		int weekZh=0;
		int week =now.get(Calendar.DAY_OF_WEEK);
		if(week==1){
			weekZh=7;
		}else{
			week=week-1;
		}
		return weekZh;  
    }  
	/**
	 * 获取星期几
	 * @param d
	 * @param day
	 * @return
	 */
	public static String getWeekZh(Date d) {  
		Calendar now = Calendar.getInstance();  
		now.setTime(d);  
		String weekZh="";
		int week =now.get(Calendar.DAY_OF_WEEK);
		
		switch (week) {
		case 1:
			weekZh ="星期日";
			break;
		case 2:
			weekZh ="星期一";
			break;
		case 3:
			weekZh ="星期二";
			break;
		case 4:
			weekZh ="星期三";
			break;
		case 5:
			weekZh ="星期四";
			break;
		case 6:
			weekZh ="星期五";
			break;
		case 7:
			weekZh ="星期六";
			break;
		default:
			break;
		}
		return weekZh;  
	}  
	/**
	 * 获取几天后是星期几
	 * @param d
	 * @param day
	 * @param formatType
	 * @return
	 */
	public static String[] getDateAfter(Date d,int day,String formatType) {
		
		if(day<0){
			return null;
		}
		String[] str = new String[day];
		for(int i=0;i<day;i++){
			str[i]=dateToString(getDateAfter(d,i), formatType);
		}
		
		return str;
		
	}
	public static Date getDateAfter(Date d, int day) {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(d);  
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + day);  
        return cal.getTime();  
    }
	public static Date getDateBefore(Date d, int day) {  
		Calendar cal = Calendar.getInstance();  
		cal.setTime(d);  
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) - day);  
		return cal.getTime();  
	}
	
	/**  
    * 计算两个日期之间相差的天数  
    * @param smdate 较小的时间 
    * @param bdate  较大的时间 
    * @return 相差天数 
    * @throws ParseException  
    */    
   public static int daysBetween(Date smdate,Date bdate)   
   {    
       SimpleDateFormat sdf=new SimpleDateFormat(FORMAT_DATE_TIME);  
       try {
		smdate=sdf.parse(sdf.format(smdate));
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
       try {
		bdate=sdf.parse(sdf.format(bdate));
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
       Calendar cal = Calendar.getInstance();    
       cal.setTime(smdate);    
       long time1 = cal.getTimeInMillis();                 
       cal.setTime(bdate);    
       long time2 = cal.getTimeInMillis();         
       long between_days=(time2-time1)/(1000*3600*24);  
           
      return Integer.parseInt(String.valueOf(between_days));           
   }  
   
	/**  
    * 计算两个日期之间相差的天数  
    * @param smdate 较小的时间 
    * @param bdate  较大的时间 
    * @return 相差天数 
    * @throws ParseException  
    */    
   public static int daysBetween(long smdate,long bdate)  
   {    
      return daysBetween(longToDate(smdate), longToDate(bdate));           
   }  
   
   //星座相关
   public static final String[] zodiacArr = { "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊" };

   public static final String[] constellationArr = { "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座" };

   public static final int[] constellationEdgeDay = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };

   /**
    * 根据日期获取生肖
    * @return
    */
   public static String getZodica(Date date) {
       Calendar cal = Calendar.getInstance();
       cal.setTime(date);
       return zodiacArr[cal.get(Calendar.YEAR) % 12];
   }

   /**
    * 根据日期获取星座
    * @return
    */
   public static String getConstellation(Date date) {
       if (date == null) {
           return "";
       }
       Calendar cal = Calendar.getInstance();
       cal.setTime(date);
       int month = cal.get(Calendar.MONTH);
       int day = cal.get(Calendar.DAY_OF_MONTH);
       if (day < constellationEdgeDay[month]) {
           month = month - 1;
       }
       if (month >= 0) {
           return constellationArr[month];
       }
       // default to return 魔羯
       return constellationArr[11];
   }
}