/* 
 * @(#)StringHelper.java    Created on 2013-3-14
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package org.haitao.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * 字符串一些工具类
 * @author wang  
 * @date 2016-3-31 下午5:00:40
 * @version V1.0
 */
public class StringUtils {

    /**
     * 是否正常的字符串
     * 
     * @param text
     * @return
     */
    public static boolean isText(String text) {
        if (text == null || text.length() == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * bytes[]转换成Hex字符串,可用于URL转换，IP地址转换
     * 
     * @param bytes
     * @return
     */
    public static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 字节转换成合适的单位
     * 
     * @param value
     * @return
     */
    public static String prettyBytes(long value) {
        String args[] = { "B", "KB", "MB", "GB", "TB" };
        StringBuilder sb = new StringBuilder();
        int i;
        if (value < 1024L) {
            sb.append(String.valueOf(value));
            i = 0;
        }
        else if (value < 1048576L) {
            sb.append(String.format("%.1f", value / 1024.0));
            i = 1;
        }
        else if (value < 1073741824L) {
            sb.append(String.format("%.2f", value / 1048576.0));
            i = 2;
        }
        else if (value < 1099511627776L) {
            sb.append(String.format("%.3f", value / 1073741824.0));
            i = 3;
        }
        else {
            sb.append(String.format("%.4f", value / 1099511627776.0));
            i = 4;
        }
        sb.append(' ');
        sb.append(args[i]);
        return sb.toString();
    }

    /**
     * 将Excepiton信息转换成String字符串.
     * 
     * @param t
     * @return
     */
    public static String exceptionToString(Throwable throwable) {
        if (throwable == null) {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            throwable.printStackTrace(new PrintStream(baos));
        }
        finally {
            try {
                baos.close();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                System.gc();
            }
        }
        return baos.toString();
    }
    /**
     * 是否是null 或者空字符串
     * true 是null 
     * false 不是null
     * @return
     */
    public static boolean isEmpty(String str){
    	if(null==str || "".equals(str.trim())){
    		return true;
    	}
    	return false;
    }
    /**
     * 是否是null 或者空字符串
     * true 是null 
     * false 不是null
     * @return
     */
    public static boolean isEmpty(CharSequence str){
    	if(null==str || "".equals(str.toString().trim())){
    		return true;
    	}
    	return false;
    }
    /** 
     * @Description:     生成随机数字和字母区分大小写  
     * @param @param     length
     * @return String    返回类型 
     */
     public static String getStringRandom(int length) {  
           
         String val = "";  
         Random random = new Random();  
           
         //参数length，表示生成几位随机数  
         for(int i = 0; i < length; i++) {  
               
             String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
             //输出字母还是数字  
             if( "char".equalsIgnoreCase(charOrNum) ) {  
                 //输出是大写字母还是小写字母  
                 int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                 val += (char)(random.nextInt(26) + temp);  
             } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                 val += String.valueOf(random.nextInt(10));  
             }  
         }  
         return val;  
     }  
     /** 
      * @Description:     生成随机字母区分大小写  
      * @param @param     length
      * @return String    返回类型 
      */
     public static String getStringRandomChar(int length) {  
     	
     	String val = "";  
     	Random random = new Random();  
     	//参数length，表示生成几位随机数  
     	for(int i = 0; i < length; i++) {  
     		int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
 			val += (char)(random.nextInt(26) + temp);  
     	}  
     	return val;  
     }  
     /** 
    * @Description: list 拼接成string
    * @param  list
    * @param  split
    * @return String    返回类型 
    */
    public static String ListToString(List<String> list,String split) {  
    	 if(list==null){
    		 return null;
    	 }
    	 StringBuilder sb = new StringBuilder();  
    	 //参数length，表示生成几位随机数  
    	 for(String ob:list) {  
    		sb.append(split).append(ob);
    	 }  
    	 return sb.toString().replaceFirst(split, "");  
     }  
    /** 
     * @Description: list 拼接成string
     * @param  object[]
     * @param  split
     * @return String    返回类型 
     */
    public static String ArrayToString(String object[],String split) {  
    	if(object==null){
    		return null; 
    	}
    	StringBuilder sb = new StringBuilder();  
    	//参数length，表示生成几位随机数  
    	for(Object ob:object) {  
    		sb.append(split).append(ob);
    	}  
    	return sb.toString().replaceFirst(split, "");  
    }  
    /** 
    * @Description: 
    * @param  object 原来的字符
    * @param  newObj 新的字符串
    * @param  split  分隔符
    * @return String    返回类型 
    */
    public static String StringToString(String object,String newObj,String split) {  
    	if(object==null || "" .equals(object)){
    		return newObj.toString(); 
    	}
    	object =object+split+newObj;
    	return object.toString();  
    }  
	/** 
	* @Description: 格式化数字
	* @param num
	* @return String    返回类型 
	*/
    public  static String formatUnit(int num){
		String numStr =String.valueOf(num);
		String numReslut =numStr;
		if(numStr.length()>3){
			DecimalFormat df = new DecimalFormat("#.0"); 
			if(numStr.length()<7){
				numReslut=df.format(num/1000.0).replaceAll("\\.0", "")+"k";
			}else {
				// 百万换
				numReslut=df.format(num/10000.0).replaceAll("\\.0", "")+"w";
			}
		}
		return numReslut;
		
	}
     
}
