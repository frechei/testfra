package org.haotao.common.utils;


import org.haotao.common.logger.AndroidLogTool;
import org.haotao.common.logger.LogLevel;
import org.haotao.common.logger.LoggerAll;

public class AppLog {
	static {
		LoggerAll
				.init("log")                 // default PRETTYLOGGER or use just init()
				.methodCount(3)                 // default 2
				.logLevel(LogLevel.NONE)        // default LogLevel.FULL
				.methodOffset(2)                // default 0
				.logTool(new AndroidLogTool()); // custom log tool, optional
	}
	static boolean DEBUG=true;
	public static void setDedug (boolean debug){
		DEBUG =debug;
	}
	public static void i(String tag,Object msg){
		if (DEBUG) {
			if (msg==null) {
				show(tag, "logi----null",LogEnum.i);
			}else{
				show(tag, msg.toString(),LogEnum.i);
			}
		}
	}
	public static void v(String tag,Object  msg){
		if (DEBUG) {
			if (msg==null) {
				show(tag, "logv----null",LogEnum.v);
			}else{
				show(tag, msg.toString(),LogEnum.v);
			}
		}
	}
	public static void d(String tag,Object  msg){
		if (DEBUG) {
			if (msg==null) {
				show(tag, "logd----null",LogEnum.d);
			}else{
				show(tag, msg.toString(),LogEnum.d);
			}
		}
	}
	public static void d(Class<?>  cls,String msg){
		if (DEBUG) {
			show(cls.toString(), msg,LogEnum.d);
		}
	}
	public static void e(String tag,Object  msg){
		if (DEBUG) {
			if (msg==null) {
				show(tag, "loge----null",LogEnum.e);
			}else{
				show(tag, msg.toString(), LogEnum.e);
			}
		}
	}
	public static void w(String tag,Object  msg){
		if (DEBUG) {
			if (msg==null) {
				show(tag, "logi----null",LogEnum.w);
			}else{
				show(tag, msg.toString(),LogEnum.w);
			}
		}
	}
	public static void e(Object  msg){
		e("APP_LOG", msg);
	}
	public static void show(String tag,String str,LogEnum logEnum) {
		//System.out.print(str);
		switch (logEnum) {
				case i:
					LoggerAll.e( str,tag);
					break;
				case v:
					LoggerAll.v(str, tag);
					break;
				case d:
					LoggerAll.d(str, tag);
					break;
				case e:
					LoggerAll.e( str,tag);
					break;
				case w:
					LoggerAll.w(str, tag);
					break;
			}
//		int index = 0;
//		int maxLength = 4000;
//		String sub;
//		if (str.length() <= maxLength) {
//			sub = str;
//			switch (logEnum) {
//				case i:
//					Log.i(tag, str);
//					break;
//				case v:
//					Log.v(tag, str);
//					break;
//				case d:
//					Log.d(tag, str);
//					break;
//				case e:
//					Log.e(tag, str);
//					break;
//				case w:
//					Log.w(tag, str);
//					break;
//			}
//		}else{
//			while (index < str.length()) {
//				if (str.length()-index<maxLength) {
//					sub = str.substring(index, str.length());
//				}else{
//					sub = str.substring(index, index+maxLength);
//				}
//				index += maxLength;
//				switch (logEnum) {
//					case i:
//						Log.i(tag, sub);
//						break;
//					case v:
//						Log.v(tag,sub);
//						break;
//					case d:
//						Log.d(tag, sub);
//						break;
//					case e:
//						//System.out.print("syso print=" + sub);
//						Log.e(tag, "log print="+ sub);
//						LoggerAll.e(sub);
//						break;
//					case w:
//						Log.w(tag, sub);
//						break;
//				}
//			}
//		}
	}
	public enum LogEnum {
		i, v, d, e,w;
	}
}

