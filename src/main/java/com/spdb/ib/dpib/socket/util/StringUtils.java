package com.spdb.ib.dpib.socket.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {

	public static boolean isNullOrEmpty(String str){
		if(str==null || "".equals(str)){
			return true;
		}
		return false;
	}
	
	public static long getTime(){
		return System.currentTimeMillis();
	}
	public static long getTime(long time){
		return System.currentTimeMillis()-time;
	}
	//	时间格式转化
	public static String getLogTime(Date logtime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String st = sdf.format(logtime);
		return st;
	}
	
	public static long stringTimeToLong(final String nowtime) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		long date = 0L;
		try {
			date = dateFormat.parse(nowtime).getTime();
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		System.out.println(stringTimeToLong("2008-09-11 23:25:26"));
//	}

}
