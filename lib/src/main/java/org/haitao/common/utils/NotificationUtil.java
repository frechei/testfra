package org.haitao.common.utils;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * 系统通知栏
 * @author Administrator
 */
public class NotificationUtil
{
	/**
	 * 在系统栏上显示一个点击自动消失的提示信息
	 * @param context	上下文
	 * @param NotuifiID 提示栏的ID
	 * @param IconID	图标ID
	 * @param showmsg	未打开显示滚动的文字
	 * @param title		拖下任务栏 看见的标题
	 * @param content	拖下任务栏 看见的文本
	 * @param intent	点击提示信息将要执行的操作
	 */
	public static void showNotification(Context context,int NotuifiID,int IconID,String showmsg,String title,String content,Intent intent)
	{
		show(context, android.app.Notification.FLAG_AUTO_CANCEL, NotuifiID, IconID, showmsg, title, content, intent);
	}
	/**
	 * 在系统栏上显示一个不会被清理的提示信息
	 * @param context	上下文
	 * @param NotuifiID 提示栏的ID
	 * @param IconID	图标ID
	 * @param showmsg	未打开显示滚动的文字
	 * @param title		拖下任务栏 看见的标题
	 * @param content	拖下任务栏 看见的文本
	 * @param intent	点击提示信息将要执行的操作
	 */
	public static void showNotificationNoClear(Context context,int NotuifiID,int IconID,String showmsg,String title,String content,Intent intent)
	{
		show(context, android.app.Notification.FLAG_NO_CLEAR, NotuifiID, IconID, showmsg, title, content, intent);
	}
	/**
	 * 在系统栏上显示一个一直存在的的提示信息
	 * @param context	上下文
	 * @param NotuifiID 提示栏的ID
	 * @param IconID	图标ID
	 * @param showmsg	未打开显示滚动的文字
	 * @param title		拖下任务栏 看见的标题
	 * @param content	拖下任务栏 看见的文本
	 * @param intent	点击提示信息将要执行的操作
	 */
	public static void showNotificationAlwaysIn(Context context,int NotuifiID,int IconID,String showmsg,String title,String content,Intent intent)
	{
		show(context, android.app.Notification.FLAG_INSISTENT, NotuifiID, IconID, showmsg, title, content, intent);
	}
	
	@SuppressLint("NewApi")
	private static void show(Context context,int flags,int NotuifiID,int IconID,String showmsg,String title,String content,Intent intent)
	{
//		//获取手机系统里的通知管理器
//		NotificationManager NM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//		
//		//实例化一个notification
//		android.app.Notification notification = new android.app.Notification(IconID,showmsg,System.currentTimeMillis());
//		notification.flags = flags;
//		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
//		notification.setLatestEventInfo(context, title, content, pIntent);
//		NM.notify(NotuifiID, notification);
		
		
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(context);//新建Notification.Builder对象
		PendingIntent pIntent = PendingIntent.getActivity(context, 0,  intent, 0);
		//PendingIntent点击通知后所跳转的页面
		builder.setContentTitle(title); 
		builder.setContentText( content);
		builder.setSmallIcon(IconID);
		builder.setContentIntent(pIntent);//执行intent
		Notification notification = builder.getNotification();//将builder对象转换为普通的notification
		notification.flags |= flags;//点击通知后通知消失
		manager.notify(NotuifiID,notification);//运行notification
	}
	/**
	 * 清除通知
	 * @param context	上下文
	 * @param id		通知ID
	 */
	public static void clear(Context context,int id)
	{
		//获取手机系统里的通知管理器
		NotificationManager NM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		NM.cancel(id);
	}
	
}
