package org.haitao.common.utils;



import android.util.Log;

/** 
* @Description: 更加好用的log
* @author haitao 
* @date 2015-11-20 上午10:41:21 
* @version V1.0   
*/
public class AppLog {

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
	public static void json(String tag,Object  msg){
		if (DEBUG) {
			if (msg==null) {
				show(tag, "logjson----null",LogEnum.w);
			}else{
				show(tag, JsonFormatTool.formatJson(msg.toString()),LogEnum.w);
			}
		}
	}
	public static void e(Object  msg){
		e("APP_LOG", msg);
	}
	public static void show(String tag,String message,LogEnum logEnum) {
		int maxLength = 4000;
		byte[] bytes = message.getBytes();
		int length = bytes.length;		
		if (length <= maxLength) {
			log(tag, message,logEnum);
		}else{
			for(int i=0;i<length;i+=maxLength){
				int count = Math.min(length-i, maxLength);
				log(tag, new String(bytes,i,count),logEnum);
			}
		}
	}
	private static void  log(String tag,String str,LogEnum logEnum){
		switch (logEnum) {
		case i:
			Log.i(tag, str);
			break;
		case v:
			Log.v(tag,str);
			break;
		case d:
			Log.d(tag, str);
			break;
		case e:
			Log.e(tag,str);
			break;
		case w:
			Log.w(tag, str);
			break;
		}
	}
	public enum LogEnum {
		i, v, d, e,w;
	}
}

