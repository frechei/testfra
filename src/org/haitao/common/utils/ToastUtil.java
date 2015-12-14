package org.haitao.common.utils;

import android.app.Application;
import android.widget.Toast;


public class ToastUtil {

	private static long time = 2000;
	private static long showTime = 0;
	/**
	 * 设置时间间隔 默认2秒
	 * @param time
	 */
	public  static void setIntervalTime(long _time){
		time = _time;
	}
	private static Application ap ;
	public static void init (Application _ap){
		ap =_ap;
	}
	public static void shortShowToast( String content) {
		if (ap==null || !show()) {
			return;
		}
		Toast.makeText(ap, content, Toast.LENGTH_SHORT).show();
	}
	public static void shortShowLogin() {
		if (ap==null || !show()) {
			return;
		}
		Toast.makeText(ap, "请先登录", Toast.LENGTH_SHORT).show();
	}
	public static void longShowToast( String content) {
		if (ap==null || !show()) {
			return;
		}
		Toast.makeText(ap, content, Toast.LENGTH_SHORT).show();
	}
	
	private static boolean show(){
		if ((System.currentTimeMillis() - showTime) > time) {
			showTime = System.currentTimeMillis();
			return true;
		}
		return false;
			
	}
}
