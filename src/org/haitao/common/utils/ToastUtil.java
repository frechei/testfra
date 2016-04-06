package org.haitao.common.utils;


import android.app.Application;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


/**   
 * toast
 * @Package org.haitao.common.utils 
 * @Description: TODO
 * @author wang  
 * @date 2016-3-31 下午5:07:18 
 * @version V1.0   
 */
public class ToastUtil {

	private static long time = 2000;
	private static long showTime = 0;
	private static int layout_id = 0;
	private static int text_id = 0;
	
	public static void setLayoutId(int layout_id,int text_id ) {
		ToastUtil.layout_id = layout_id;
		ToastUtil.text_id = text_id;
	}
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
	/** 
	 * 自定义样式
	 * @Title: shortShowCustom 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param content    设定文件 
	 * @return void    返回类型 
	 */
	public static void shortShowCustom(int resId) {
		shortShowCustom(resId,null,Gravity.CENTER,0,0);
	}
	public static void shortShowCustom( String content) {
		shortShowCustom(content,Gravity.CENTER,0,0);
	}
	public static void shortShowCustom(int resId,int gravity, int xOffset, int yOffset) {
		shortShowCustom(resId,null,gravity,xOffset,yOffset);
	}
	public static void shortShowCustom(String content,int gravity, int xOffset, int yOffset) {
		shortShowCustom(-1,content,gravity,xOffset,yOffset);
	}
	private static void shortShowCustom(int resId,String content,int gravity, int xOffset, int yOffset) {
		if (ap==null || !show()) {
			return;
		}
		if(layout_id>0){
			Toast toast = new Toast(ap);  
			LayoutInflater inflater = LayoutInflater.from(ap);  
			View layout = inflater.inflate(layout_id, null);
			toast.setGravity( gravity, xOffset, yOffset);
			TextView text = (TextView) layout.findViewById(text_id);
			toast.setDuration(Toast.LENGTH_SHORT);
			if(resId<0){
				text.setText(content);
			}else{
				text.setText(resId);
			}
			toast.setView(layout);  
			toast.show();
		}
	}
	public static void shortShowToast( String content) {
		if (ap==null || !show()) {
			return;
		}
		Toast.makeText(ap, content, Toast.LENGTH_SHORT).show();
	}
	public static void shortShowToast(int resId) {
		if (ap==null || !show()) {
			return;
		}
		Toast.makeText(ap, resId, Toast.LENGTH_SHORT).show();
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
	public static void longShowToast( int resId) {
		if (ap==null || !show()) {
			return;
		}
		Toast.makeText(ap, resId, Toast.LENGTH_SHORT).show();
	}
	
	private static boolean show(){
		if ((System.currentTimeMillis() - showTime) > time) {
			showTime = System.currentTimeMillis();
			return true;
		}
		return false;
	}
}
