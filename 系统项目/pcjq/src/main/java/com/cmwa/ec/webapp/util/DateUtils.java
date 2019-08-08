package com.cmwa.ec.webapp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * Title: DateUtils
 * </p>
 * <p>
 * Description: 日期通用工具.
 * </p>
 * @author niedc
 * @version 1.0
 * @Create date: 2014-12-23
 * 
 */

public class DateUtils {

	/**
	 * yyyyMMdd日期格式化样式
	 */
    public static final String yyyyMMdd = "yyyyMMdd"; 
    
    /**
     * yyyy-MM-dd日期格式化样式
     */
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    
    /**
     * yyyy-MM-dd日期格式化样式
     */
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyyMMdd日期格式化工具
     */
    public static final SimpleDateFormat DF_yyyyMMdd = new SimpleDateFormat(yyyyMMdd);
    
    public static final SimpleDateFormat DF_yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
	 * 构造函数
	 */
	public DateUtils() {

	}
	
	/**
	 * 把Date转换为缺省的日期格式字串，缺省的字转换格式为yyyy-MM-dd HH:mm 如：2004-10-10 20:12
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm");
	}
	
	/**
	 * 根据特定的Pattern,把Date转换为相应的日期格式字符串
	 * 如果日期参数为空，返回无日期
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}

	/***
	 * 根据当前时间生成问候语   0:00-6:00清晨好   6：00-11:00 早上好  11:01-14:30 中午好 14:31-18:30 下午好 18:31-23:59 晚上好
	 * niedc
	 */
	public static String getWelcome() {
    	Calendar calendar=Calendar.getInstance();
    	long nowTime=calendar.getTimeInMillis();
     
    	calendar.set(Calendar.HOUR_OF_DAY, 6);
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);
    	long time1=calendar.getTimeInMillis();
    	
    	calendar.set(Calendar.HOUR_OF_DAY, 11);
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);
    	long time2=calendar.getTimeInMillis();
    	
    	calendar.set(Calendar.HOUR_OF_DAY, 14);
    	calendar.set(Calendar.MINUTE, 30);
    	calendar.set(Calendar.SECOND, 0);
    	long time3=calendar.getTimeInMillis();
    	
    	calendar.set(Calendar.HOUR_OF_DAY, 18);
    	calendar.set(Calendar.MINUTE, 30);
    	calendar.set(Calendar.SECOND, 0);
    	long time4=calendar.getTimeInMillis();
    	
    	calendar.set(Calendar.HOUR_OF_DAY, 24);
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);
    	long time5=calendar.getTimeInMillis();
		if(nowTime<=time1){
			return "清晨好";
		}else if(nowTime>time1&&nowTime<=time2){
			return "上午好";
		}else if(nowTime>time2&&nowTime<=time3){
			return "中午好";
		}else if(nowTime>time3&&nowTime<=time4){
			return "下午好";
		}else if(nowTime>time4&&nowTime<=time5){
			return "晚上好";
		}else{
			return "好";
		}
		 
	}

	public static void main(String[] args) {
	 
		System.out.println(getWelcome()+"");
	}
}
