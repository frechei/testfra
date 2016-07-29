package org.haitao.common.utils;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


/**   
 * toast  通知被关闭也可以弹出
 * @Package org.haitao.common.utils 
 * @Description: TODO
 * @author wang  
 * @date 2016-3-31 下午5:07:18 
 * @version V1.0   
 */
public class ToastUtil {

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
	private static long time = 2000;
	private static long showTime = 0;
	private static int layout_id = 0;
	private static int text_id = 0;
	private static boolean sysEnable = true;
	
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
		check();
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
		if (ap==null ) {
			return;
		}
		if(layout_id>0){
			if(isShow()){
				return;
			}
			LayoutInflater inflater = LayoutInflater.from(ap);  
			View layout = inflater.inflate(layout_id, null);
			TextView text = (TextView) layout.findViewById(text_id);
			if(resId<0){
				text.setText(content);
			}else{
				text.setText(resId);
			}
			if(sysEnable){
				Toast toast = new Toast(ap);  
				toast.setGravity( gravity, xOffset, yOffset);
				toast.setDuration(Toast.LENGTH_SHORT);
				toast.setView(layout);  
				toast.show();
			}else{
				CustomToast.makeText(ap,layout,Toast.LENGTH_SHORT).show();
			}
			
		}else{
			showToast(content,resId,true);
		}
	}
	public static void shortShowToast( String content) {
		showToast(content,-1,true);
	}
	public static void shortShowToast(int resId) {
		showToast(null,resId,true);
	}
	public static void shortShowLogin() {
		showToast("请先登录",-1,true);
	}
	public static void longShowToast( String content) {
		showToast(content,-1,false);
	}
	public static void longShowToast( int resId) {
		showToast(null,resId,false);
	}
	private static void showToast(String content,int resId,boolean isShort){
		if (ap==null || isShow()) {
			return;
		}
		if(sysEnable){
			AppLog.e("en"+content+resId);
			Toast.makeText(ap, resId>0?ap.getResources().getString(resId):content, isShort?Toast.LENGTH_SHORT:Toast.LENGTH_LONG).show();
		}else{
			AppLog.e("nn"+content+resId);
			CustomToast.makeText(ap, resId>0?ap.getResources().getString(resId):content, isShort?Toast.LENGTH_SHORT:Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * 是否正在显示
	 * @return
	 */
	private static boolean isShow(){
		if ((System.currentTimeMillis() - showTime) > time) {
			showTime = System.currentTimeMillis();//没有显示可以显示  所以要更新时间
			return false;
		}
		return true;
	}
	
	/**
	 * 为了性能只检查一次
	 * @return
	 */
	private static boolean check(){
		return sysEnable =isNotificationEnabled(ap);
	}
	@TargetApi(19)
	public static boolean isNotificationEnabled(Context context){

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Class appOpsClass = null; /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());

            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);

            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (Integer)opPostNotificationValue.get(Integer.class);
            return ((Integer)checkOpNoThrowMethod.invoke(mAppOps,value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
