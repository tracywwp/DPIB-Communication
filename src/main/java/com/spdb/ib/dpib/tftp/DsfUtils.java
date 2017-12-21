package com.spdb.ib.dpib.tftp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/*******************************************************************************
 * 1. 将输入的字符串按照输入的分隔符拆分成子串存入缓存区，返回子串的数量 int Str2Array( String strSrc; 要转化的字符串
 * ISO8859_1 String Buf[]; 字符串缓存区 GB2312 char Separator; 分隔符 )
 * 
 * 
 * 2.将输入的字符串按照输入的分隔符拆分成子串存入缓存区，返回子串的数量 int Str2Array( String strSrc; 要转化的字符串
 * ISO8859_1 String Buf[]; 字符串缓存区 GB2312 int[] iBlock; 说明各个域的长度 )
 * 
 * 3.计算输入的字符串按照输入的分隔符拆分成子串的数量,返回子串的数量 int Str2ArrayCount(String, String) String
 * strSrc; 要转化的字符串 char Separator; 分隔符
 * 
 * Modified by Sunny at 20090212 日志打印方式修改，采用DsfLogger中的方法进行打印，增加finally关闭文件流
 * 
 * Modified by Sunny at 20090420
 * 修改Today方法，从配置文件中读取USE_SYS_DATE的值，TRUE-取系统时间，FALSE-从配置文件中取当前日期
 * 增加getPath方法，从配置文件中读取参数（配置文件信息不写进内存） 以上修改只适用于测试环境，如果是上生产，需要将以上修改还原
 * 
 * Modified wang.qiang 20090915 <br>
 * 添加公用方法：isOldProject(String operation) 判断指定的项目是否为缴费流水表改造以前的项目
 * 返回值：boolean(true:false) <br>
 * 为旧项目时，返回true，否则返回false
 * 
 * @author T-wuwp
 */

public class DsfUtils {
	public static long serialNo = 0;
	private static Properties confiProperties;

	/**
	 * Add by Sunny at 2014-6-21 用于测试，如果要上线，需要注释掉此方法
	 * 
	 * @param property
	 * @return
	 */
	public static String getPath(String property) {
		if (confiProperties == null) {
			synchronized (DsfUtils.class) {
				if (confiProperties == null) {
					confiProperties = new Properties();
					InputStream is = null;
					try {
						is=Thread.currentThread().getContextClassLoader().getResourceAsStream("TftpConfig.properties");
						confiProperties.load(is);
					} catch (IOException e) {
					} finally {
						if (is != null) {
							try {
								is.close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}
		}
		return confiProperties.getProperty(property);
	}
}
