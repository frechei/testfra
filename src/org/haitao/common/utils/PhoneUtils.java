package org.haitao.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Properties;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;


/** 
* @Description: 跟手机相关的辅助类
* @author haitao 
* @date 2016-6-12 下午2:03:13 
* @version V1.0   
*/
public class PhoneUtils
{

	private static String deviceId;
	private static String phoneName;
	
	private PhoneUtils()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");

	}

	
	// 检测MIUI 
	private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
	private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
	private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

	public static boolean isMIUI() {

		Properties prop = new Properties();
		boolean isMIUI;
		try {
			prop.load(new FileInputStream(new File(Environment
					.getRootDirectory(), "build.prop")));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		isMIUI = prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
				|| prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
				|| prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
		return isMIUI;
	}
	public static boolean isMeizu() {
		
		if("Meizu".equals(android.os.Build.MANUFACTURER)){
			return true;
		 }
		try {
			// Invoke Build.hasSmartBar()
			final Method method = Build.class.getMethod("hasSmartBar");
			return method != null;
		} catch (final Exception e) {
			return false;
		}
	}
	/**
	 * 修改小米主题
	 * @param darkmode
	 * @param activity
	 */
	public static void setStatusBarDarkModeMIUI(boolean darkmode, Activity activity) {
	    Class<? extends Window> clazz = activity.getWindow().getClass();
	    try {
	        int darkModeFlag = 0;
	        Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
	        Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
	        darkModeFlag = field.getInt(layoutParams);
	        Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
	        extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	/**
	 * 修改魅族主题
	 * @param darkmode
	 * @param activity
	 */
	public static boolean setStatusBarDarkModeMeizu(boolean dark, Activity activity) {
	    boolean result = false;
	    Window window=activity.getWindow();
	    if (window != null) {
	        try {
	            WindowManager.LayoutParams lp = window.getAttributes();
	            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
	            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
	            darkFlag.setAccessible(true);
	            meizuFlags.setAccessible(true);
	            int bit = darkFlag.getInt(null);
	            int value = meizuFlags.getInt(lp);
	            if (dark) {
	                value |= bit;
	            } else {
	                value &= ~bit;
	            }
	            meizuFlags.setInt(lp, value);
	            window.setAttributes(lp);
	            result = true;
	        } catch (Exception e) {
	        }
	    }
	    return result;
	}
}
