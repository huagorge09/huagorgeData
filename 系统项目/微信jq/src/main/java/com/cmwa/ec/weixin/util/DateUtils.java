package com.cmwa.ec.weixin.util;

import java.util.Date;
import java.util.Calendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;


/**
 * <p>Title: DateUtils </p>
 * <p>Description: 日期通用工具. </p>
 * <p>Copyright: Copyright (c) 2004-2008</p>
 * <p>Company: Legion Technology</p>
 * @author 李建海
 * @version 1.0
 * @Create date: 2008-05-30
 *
 */

public class DateUtils {
	
	private static Logger logger = Logger.getLogger(DateUtils.class);

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
     * 计算指定日期加减天数后的日期
     * @param inputDate  '20080301'
     * @param dateFormat 日期格式，'yyyyMMdd' 参考SimpleDateFormat
     * @param amount 数量
     */
    public String addDay(String inputDate, String dateFormat, int amount) {

        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date date = sdf.parse(inputDate);
            calendar.setTime(date);
            //calendar.add(field, amount);
            calendar.add(Calendar.DAY_OF_MONTH, amount);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            System.out.println("DateUtils.addDay异常：" + e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 计算指定日期加减月数后的日期
     * @param inputDate  '20080301'
     * @param dateFormat 日期格式，'yyyyMMdd' 参考SimpleDateFormat
     * @param amount 数量
     */
    public String addMonth(String inputDate, String dateFormat, int amount) {

        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date date = sdf.parse(inputDate);
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, amount);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            System.out.println("DateUtils.addMonth异常：" + e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 计算指定日期加减年数后的日期
     * @param inputDate  '20080301'
     * @param dateFormat 日期格式，'yyyyMMdd' 参考SimpleDateFormat
     * @param amount 数量
     */
    public String addYear(String inputDate, String dateFormat, int amount) {

        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date date = sdf.parse(inputDate);
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, amount);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            System.out.println("DateUtils.addMonth异常：" + e);
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean isExpired(String timeStamp, long diffSeconds){  
    	long curTime = System.currentTimeMillis();
    	long lastTime = 0L;
    	try{
    		lastTime = Long.parseLong(timeStamp);
    	}catch(Exception e){
    	}    	
    	long diff = (curTime - lastTime) / 1000; //转换为秒    	
    	return diff <= diffSeconds;
    }
    
	/**
	 * 把Date转换为缺省的日期格式字串，缺省的字转换格式为yyyy-MM-dd HH:mm:ss 如：2004-10-10 20:12:10
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
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
	/**
	 * 以缺省的yyyy-MM-dd HH:mm:ss格式转换字符串为Date，若转换成功，则返回相应的Date对象 若转换失败，则返回null。
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date parseString(String dateStr) {
		return parseString(dateStr, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 以特定的格式转换字符串为Date，若转换成功，则返回相应的Date对象 若转换失败，则返回null。
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date parseString(String dateStr, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		try {
			if (!StringHelper.isEmpty(dateStr)) {
				return dateFormat.parse(dateStr);
			}
		} catch (ParseException ex) {
			logger.error("", ex);
		}
		return null;
	}
    public static String getBeforeDay(int day,String pattern){
    	Calendar  calendar=Calendar.getInstance();
    	calendar.add(Calendar.DAY_OF_MONTH, day);
    	Date date=calendar.getTime();
    	return formatDate(date,pattern);
    }
    /**
     * 判断当前是否是工作时间，周六，周天，工作日9:00-11:30 13:00-17:30 以外为工作时间
     * @return flag
     */
    public static boolean isWorkTime(){
    	boolean flag=true;
    	Calendar calendar=Calendar.getInstance();
    	long nowTime=calendar.getTimeInMillis();
    	int dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);
    	if(dayOfWeek==7||dayOfWeek==1){
    		flag=false;
    		return flag;
    	}
    	calendar.set(Calendar.HOUR_OF_DAY, 9);
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);
    	long monitorBeginTime=calendar.getTimeInMillis();
    	calendar.set(Calendar.HOUR_OF_DAY, 11);
    	calendar.set(Calendar.MINUTE, 30);
    	calendar.set(Calendar.SECOND, 0);
    	long monitorEndTime=calendar.getTimeInMillis();
    	calendar.set(Calendar.HOUR_OF_DAY, 13);
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);
    	long afertnoonBeginTime=calendar.getTimeInMillis();
    	calendar.set(Calendar.HOUR_OF_DAY, 17);
    	calendar.set(Calendar.MINUTE, 30);
    	calendar.set(Calendar.SECOND, 0);
    	long afertnoonEndTime=calendar.getTimeInMillis();
    	if((nowTime>monitorBeginTime&&nowTime<monitorEndTime)||(nowTime>afertnoonBeginTime&&nowTime<afertnoonEndTime)){
    		flag=true;
    	}else{
    		flag=false;
    	}

    	return flag;
    }
    
     /**
      * 比较两个时间的差距
      * @param date1
      * @param date2
      * @return
      */
	 public static int differentDays(Date date1,Date date2)
	    {
	        Calendar cal1 = Calendar.getInstance();
	        cal1.setTime(date1);
	        
	        Calendar cal2 = Calendar.getInstance();
	        cal2.setTime(date2);
	       int day1= cal1.get(Calendar.DAY_OF_YEAR);
	        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
	        
	        int year1 = cal1.get(Calendar.YEAR);
	        int year2 = cal2.get(Calendar.YEAR);
	        if(year1 != year2)   //同一年
	        {
	            int timeDistance = 0 ;
	            for(int i = year1 ; i < year2 ; i ++)
	            {
	                if(i%4==0 && i%100!=0 || i%400==0)    //闰年            
	                {
	                    timeDistance += 366;
	                }
	                else    //不是闰年
	                {
	                    timeDistance += 365;
	                }
	            }
	            
	            return timeDistance + (day2-day1) ;
	        }
	        else    //不同年
	        {
	            return day2-day1;
	        }
	    }
    public static void main(String[] args){
    	System.out.println(isWorkTime());
    }
}
