package com.fxwx.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

public class DateUtil {

	/**
	 * 自动匹配字符串格式
	 */
	public  static String[] parsePatterns = { "yyyyMMdd","yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" };
	/**
	 * 默认 日期时间 格式  yyyy-MM-dd HH:mm:ss
	 */
	public static final String PATTERN_STANDARD = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 线程共享一个simpledateformat对象,多线程下处理对象创建问题
	 */
	private static ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	/**
	 * 当前时间转化为yyyy-MM-dd
	 * @return
	 */
	public static String getDate(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}
	/**
	 * @Description 字符串转化为时间
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static Date parse(String dateStr) throws ParseException {
		return threadLocal.get().parse(dateStr);
	}

	/**
	 * @Description  时间转化为字符串
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return threadLocal.get().format(date);
	}

	/**
	 * 获取小时数和分钟数拼接后转换为int 列子：2014-09-23 08:32:54-->832
	 * @return
	 */
	public static int getHHMMInt(){
		SimpleDateFormat sdf=new SimpleDateFormat("HHmm");
		String str=sdf.format(new Date());
		return Integer.parseInt(str);
	}

	/**
	 * 当前时间转化为yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getStringDate(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	
	/**
	 * 当前时间转化为yyyyMMdd
	 * @return
	 */
	public static String getDateyyyyMMdd(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}

	/**
	 * 给定date时间转化为yyyy-MM-dd HH:mm:ss,可能为null
	 * @return 
	 */
	public static String getStringDate(Date date){
		if(date==null){return null;}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 字符串时间转化为Date
	 * @param str
	 * @return
	 */
	public static Date getDateFromString(String str){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 字符串时间转化为 String 转String
	 * @param str
	 * @return
	 */
	public static String getStringFromString(String str){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			 return sdf.format(sdf.parse(str));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 日期加减
	 * 0按小时收费；1按天;2按月
	 */
	public static String datePlus(int type,int num){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar now=Calendar.getInstance();
		if(type==0){//0按小时
			now.add(Calendar.HOUR, num);

		}else if(type==1){//1按天
			now.add(Calendar.DAY_OF_YEAR, num);
		}else if(type==2){//2按月
			now.add(Calendar.MONTH, num);
		}else if(type==3){//按一年
			now.add(Calendar.YEAR, num);

		}else if(type==4){//按两年
			now.add(Calendar.YEAR, num*2);
		}
		return DateUtil.format(now.getTime());
	}
	/**
	 * 日期加减
	 * 0按小时收费；1按天;2按月
	 */
	public static String datePlus(int type,int num,String priceNum){


		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar now=Calendar.getInstance();
		//log.error(sdf.format(now.getTime()));
		if(type==0){//0按小时

			now.add(Calendar.HOUR, num*Integer.parseInt(priceNum));
		}else if(type==1){//1按天

			now.add(Calendar.DAY_OF_YEAR, num*Integer.parseInt(priceNum));
		}else if(type==2){

			now.add(Calendar.MONTH, num*Integer.parseInt(priceNum));
		}
		return DateUtil.format(now.getTime());
	}
	/** 
	 * 
	 * 根据到期时间和当前时间比较判断日期加减
	 * 0按小时收费；1按天;2按月
	 */
	public static String datePluss(int type,int num,String date){
		//		log.error(type);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar now=Calendar.getInstance();
		try {
			Date test = DateUtil.parse(date);
			now.setTime(test);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(type==0){//0按小时
			now.add(Calendar.HOUR, num);

		}else if(type==1){//1按天
			now.add(Calendar.DAY_OF_YEAR, num);
		}else if(type==2){//2按月
			now.add(Calendar.MONTH, num);
		}else if(type==3){//按一年
			now.add(Calendar.YEAR, num);

		}else if(type==4){//按两年
			now.add(Calendar.YEAR, num*2);
		}
		return DateUtil.format(now.getTime());
	}

	/**
	 * 根据到期时间和当前时间比较判断日期加减
	 * 0按小时收费；1按天;2按月
	 */
	public static String newDatePluss(int type,int num,String data,String priceNum){
		int nums=Integer.parseInt(priceNum);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar now=Calendar.getInstance();
		//log.error(sdf.format(now.getTime()));
		try {
			Date test = DateUtil.parse(data);
			now.setTime(test);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(type==0){//0按小时
			now.add(Calendar.HOUR, num*nums);

		}else if(type==1){//1按天
			now.add(Calendar.DAY_OF_YEAR, num*nums);
		}else if(type==2){//2按月
			now.add(Calendar.MONTH, num*nums);
		}

		return DateUtil.format(now.getTime());
	}

	public static String getDateNoP(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}
	public static String getDateNoPs(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}
	/**
	 * 此方法是对当前时间和过期时间的比较
	 * 
	 * @param DATE1 当前时间
	 * @param DATE2  用户的缴费记录时间
	 * @return 1--当前时间>用户的缴费记录的时间(过期)
	 *        -1/0 --当前时间<用户缴费记录的时间(没有过期)
	 */
	public static int compareDate(Date DATE1, Date DATE2) {
		try {
			if (DATE1.getTime() > DATE2.getTime()) {
				return 1;
			} else if (DATE1.getTime() <= DATE2.getTime()) {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 此方法是对当前时间和过期时间的比较
	 * 
	 * @param DATE1 当前时间
	 * @param DATE2  用户的缴费记录时间
	 * @return 1--当前时间>用户的缴费记录的时间(过期)
	 *        -1/0 --当前时间<用户缴费记录的时间(没有过期)
	 */
	public static int compareDate(String DATE1, String DATE2) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt1 = DateUtil.parse(DATE1);
			Date dt2 = DateUtil.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() <= dt2.getTime()) {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * @Description 计算两个时间相差几年几小时几分钟
	 * @param diff
	 *            获得两个时间的毫秒时间差
	 * @return
	 * @throws ParseException
	 */
	public static String dateDiff(long diff) throws ParseException {
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long nh = 1000 * 60 * 60;// 一小时的毫秒数
		long nm = 1000 * 60;// 一分钟的毫秒数
		long ns = 1000;// 一秒钟的毫秒数
		long day = diff / nd;// 计算差多少天
		long hour = diff % nd / nh;// 计算差多少小时
		long min = diff % nd % nh / nm;// 计算差多少分钟
		long sec = diff % nd % nh % nm / ns;// 计算差多少秒//输出结果
		if (day <= 0) {
			return hour + "小时" + min + "分钟";
		}
		if (hour <= 0 && day <= 0) {
			return "0小时" + min + "分钟";
		}
		return day + "天" + hour + "小时";
	}
	/**
	 * @Description 计算两个时间相差几年几小时几分钟
	 * @param diff
	 *            获得两个时间的毫秒时间差
	 * @return
	 * @throws ParseException
	 */
	public static String dateDiff1(long diff) throws ParseException {
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long nh = 1000 * 60 * 60;// 一小时的毫秒数
		long nm = 1000 * 60;// 一分钟的毫秒数
		long ns = 1000;// 一秒钟的毫秒数
		long day = diff / nd;// 计算差多少天
		long hour = diff % nd / nh;// 计算差多少小时
		long min = diff % nd % nh / nm;// 计算差多少分钟
//		long sec = diff % nd % nh % nm / ns;// 计算差多少秒//输出结果
		if (day <= 0) {
			day=0;
			//return hour + "小时" + min + "分钟";
		}
		if (hour <= 0 && day <= 0) {
			day=0;
			hour=0;
			//return "0小时" + min + "分钟";
		}
		  if(hour>=24){
		      day=day+1;
		      hour=hour-24;
		    }
		return day + "," + hour + ","+min;
	}
	/**
	 * 计算天时分相加
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static String dateAdd(String str1,String str2){
		String[] strArry1=str1.replaceAll("[\u4e00-\u9fa5]+", "").split(",");
		String[] strArry2=str2.replaceAll("[\u4e00-\u9fa5]+", "").split(",");
		int day=Integer.valueOf(strArry1[0])+Integer.valueOf(strArry2[0]);
		int hour=(strArry1.length==1?0:Integer.valueOf(strArry1[1]))+(strArry2.length==1?0:Integer.valueOf(strArry2[1]));
		int min=(strArry1.length==2?0:Integer.valueOf(strArry1[2]))+(strArry2.length==2?0:Integer.valueOf(strArry2[2]));
		int newDay=0;
		int newHour=0;
		int newMin=0;
		if(min>60){
			newMin=min%60;
			newHour=newHour+1;
		}else{
			newMin=min;
		}
		if(hour+newHour>24){
			newHour=(hour+newHour)%24;
			newDay=newDay+1;
		}else{
			newHour=hour+newHour;
		}
		newDay=day+newDay;
		return newDay+","+newHour+","+newMin;
	}
	/**
	 * 根据到期时间和当前时间比较判断日期加减
	 * 0按小时收费；1按天;2按月
	 * 1月   2天     3小时   4分钟  5M  6G
	 */
	public static String newDategiftLength(int type,int num,String data){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar now=Calendar.getInstance();
		try {
			Date test = DateUtil.parse(data);
			now.setTime(test);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(type==3){//3按小时
			now.add(Calendar.HOUR, num);

		}else if(type==2){//2按天
			now.add(Calendar.DAY_OF_YEAR, num);
		}else if(type==1){//1按月
			now.add(Calendar.MONTH, num);
		}

		return DateUtil.format(now.getTime());
	}


	/**
	 * 字符串转换为 Date  传入字符串格式
	 * @param strDate 时间
	 * @param pattern 字符串 格式
	 * @return 如果传入字符串为null，或者空字符串，则返回null 
	 */
	public static Date string2Date(String strDate, String pattern) {
		if (StringUtils.isBlank(strDate)) {
			return null;
		}
		try {
			return DateUtils.parseDate(strDate.trim(),new String[]{pattern});
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 两个时间相减
	 * @param firsttime
	 * @param secondtime
	 * @return
	 */
	public static long subtractTime(String firsttime ,String secondtime) {

		return string2Date(firsttime,PATTERN_STANDARD).getTime() -string2Date(secondtime,PATTERN_STANDARD).getTime();
	}
	//将指定格式的字符串转换为时间戳为秒的字符串
	public static String dateToStr(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	/**
	 * @Description 计算购买时间的时间戳，毫秒
	 * @return
	 * @throws ParseException
	 */
	public static long  millisecond(int t,int s,int f) throws ParseException {
		long tm = t * 24 * 60 * 60 * 1000;// 一小时的毫秒数
        long sm = s * 60 * 60 * 1000;
        long fm = f * 60 * 1000;
        long millisecond = tm+sm+fm;
		return millisecond;
	}
	/**
	 * 时间戳转分钟
	 * @param millisecond
	 * @return
	 */
	public static long dayHour(long dateDH)throws ParseException{
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long nh = 1000 * 60 * 60;// 一小时的毫秒数
		long nm = 1000 * 60;// 一分钟的毫秒数
		long day = dateDH / nd;// 计算差多少天
		long hour = dateDH % nd / nh;// 计算差多少小时
		long min = dateDH % nd % nh / nm;// 计算差多少分钟
		
		if (day < 0 && hour !=  0) {
			return hour*60+min;
		}
		if (hour <= 0 && day <= 0) {
			return min;
		}
		long days = (day*24*60) + hour*60+min;//最终计算出分钟
		return days;
	}
	//时间戳转时间
	public static String dateDH(long dateDH) throws ParseException {
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long nh = 1000 * 60 * 60;// 一小时的毫秒数
		long nm = 1000 * 60;// 一分钟的毫秒数
		long day = dateDH / nd;// 计算差多少天
		
		long hour = dateDH % nd / nh;// 计算差多少小时
		long min = dateDH % nd % nh / nm;// 计算差多少分钟
		
		
		if (day <= 0 ) {
			return hour + "时" + min + "分钟";
		}
		if (hour <= 0 && day <= 0) {
			return "0时" + min + "分钟";
		}
		return day + "天" + hour + "时"+min + "分钟";
	}
	public static String timestamp2String(Timestamp timestamp) {
		if (timestamp == null) {
			return null;
		}
		return DateFormatUtils.format(timestamp, PATTERN_STANDARD);
	}
	
	  /* 
     * 将时间戳转换为时间
     */
    public static String stampToDate(Integer ms){
   	    long msl=(long)ms*1000;  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        String str= "";
        if(ms!=null){  
            str=sdf.format(msl);  
        } 
       return str;
    }
    
    /**
     * 时间戳，自1970年以来的秒数
     * @return
     */
    public static String create_timestamp() { 
        return String.valueOf(System.currentTimeMillis()/1000); 
      }
    
    /**
     * 月数加减
     * @Description: 
     * @param date 日期
     * @param n 加上n天 当n为负数时为减去n月
     * @return 返回yyyy-MM-dd 格式的日期
     * @Date		2016年7月5日 下午5:31:13
     */
    public static Date changeMonth(Date beginDate , int n){
    	try {
	    	SimpleDateFormat dft = new SimpleDateFormat(PATTERN_STANDARD);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(beginDate);
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + n);
			Date endDate = dft.parse(dft.format(calendar.getTime()));
			return endDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
	/**
     * 日期加减 
     * @Description: 
     * @param date 日期
     * @param n 加上n天 当n为负数时为减去n天
     * @return 返回yyyy-MM-dd 格式的日期
     * @Date		2016年7月5日 下午5:31:13
     * @Author		cuimiao
     */
    public static Date changeDate(Date beginDate , int n){
    	try {
	    	SimpleDateFormat dft = new SimpleDateFormat(PATTERN_STANDARD);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(beginDate);
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + n);
			Date endDate = dft.parse(dft.format(calendar.getTime()));
			return endDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * 给时间加上几个小时
     * @param day 当前时间 格式：yyyy-MM-dd HH:mm:ss
     * @param hour 需要加的时间
     * @return
     */
    public static Date addDateMinut(Date day, int hour){  
    	Date newDay = day;
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_STANDARD);
        String date = format.format(day);   
        System.out.println("front:" + date); //显示输入的日期  
        Calendar cal = Calendar.getInstance();   
        cal.setTime(day);   
        cal.add(Calendar.HOUR, hour);// 24小时制   
        newDay = cal.getTime();   
        System.out.println("after:" + format.format(newDay));  //显示更新后的日期 
        cal = null;   
        return newDay;   

    }
    

	public static void main(String[] args) {
//		String a=DateUtil.newDategiftLength(3,1,"2015-10-08 17:00:00");
//		log.error(a);
//		String times=DateUtil.dateAdd("9天,23小时,45分钟", "9天,2小时,55分钟");
//		try {
//			log.error(DateUtil.dateDiff1(14879523));
//			
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		changeMonth(new Date(), 23);
	}
}
