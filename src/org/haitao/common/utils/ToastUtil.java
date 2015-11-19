package org.haitao.common.utils;

import android.app.Application;
import android.widget.Toast;


public class ToastUtil {

	private static Application ap ;
	public static void init (Application _ap){
		ap =_ap;
	}
	public static void shortShowToast( String content) {
		if (ap==null) {
			return;
		}
		Toast.makeText(ap, content, Toast.LENGTH_SHORT).show();
	}
	public static void shortShowLogin() {
		if (ap==null) {
			return;
		}
		Toast.makeText(ap, "请先登录", Toast.LENGTH_SHORT).show();
	}
	public static void longShowToast( String content) {
		if (ap==null) {
			return;
		}
		Toast.makeText(ap, content, Toast.LENGTH_SHORT).show();
	}
}
