package com.legoproject.lego;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//格式化日期的工具方法
	public static String getDateString(Date date){
		return sdf.format(date);
		
	}

}
