package org.haitao.common.utils;


import org.haitao.common.logger.LoggerPrinter;

/**
 * <b>decription:</b>  log 工具类 <br>
 * <b>creat:</b>  2016-8-5 下午3:32:54 
 * @author haitao
 * @version 1.0
 */
public class AppLog {
	private static LoggerPrinter printer = new LoggerPrinter();
	static {
		printer.init("log");  
		// default PRETTYLOGGER or use just init()
	}
	static boolean DEBUG=true;
	
	/**
	 * 只有debug模式下才会输出log
	 * @param debug
	 */
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
	public static void json(Object  msg){
		if (DEBUG) {
			if (msg==null) {
				show("warn", "loge----null",LogEnum.e);
			}else{
				show("json", msg.toString(), LogEnum.j);
			}
		}
	}
	public static void jsonAppend(Object  msg, String  json){
		if (DEBUG) {
			if (msg==null && json==null) {
				show("warn", "loge----null",LogEnum.e);
			}else{
				show("json", msg.toString()+JsonFormatTool.formatJson(json), LogEnum.e);
			}
		}
	}
	private static void show(String tag,String str,LogEnum logEnum) {
		switch (logEnum) {
				case i:
					printer.e( str,tag);
					break;
				case v:
					printer.v(str, tag);
					break;
				case d:
					printer.d(str, tag);
					break;
				case e:
					printer.e( str,tag);
					break;
				case w:
					printer.w(str, tag);
					break;
				case j:
					printer.json(tag);
					break;
			}

	}
	public enum LogEnum {
		i, v, d, e,w,j;
	}
}

