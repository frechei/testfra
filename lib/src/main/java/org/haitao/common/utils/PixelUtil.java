package org.haitao.common.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;


/** 像素转换工具
 * @author MarkMjw
 */
public class PixelUtil {


    /**
     * dp转 px.
     *
     * @param value   the value
     * @param context the context
     * @return the int
     */
    public static int dp2px( Context context,float value) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int) (value * (scale / 160) + 0.5f);
    }

    /**
     * px转dp.
     *
     * @param value   the value
     * @param context the context
     * @return the int
     */
    public static int px2dp(float value, Context context) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int) ((value * 160) / scale + 0.5f);
    }


    /**
     * sp转px.
     *
     * @param value   the value
     * @param context the context
     * @return the int
     */
    public static int sp2px(float value, Context context) {
        Resources r;
        if (context == null) {
            r = Resources.getSystem();
        } else {
            r = context.getResources();
        }
        float spvalue = value * r.getDisplayMetrics().scaledDensity;
        return (int) (spvalue + 0.5f);
    }


    /**
     * px转sp.
     * @param value   the value
     * @param context the context
     * @return the int
     */
    public static int px2sp(float value, Context context) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (value / scale + 0.5f);
    }
    
    /**
     * 获取屏幕宽度
     * @param context the context
     * @return the int
     */
    public static int getScreenWidth(Context context) {
        // 获取屏幕密度（方法2） 
        DisplayMetrics dm = new DisplayMetrics(); 
        dm = context.getResources().getDisplayMetrics(); 
        return  dm.widthPixels;      // 屏幕宽（像素，如：480px） 
    }
    public  static float getScreenScale(Context context) {
        float scale = (float)context.getResources().getDisplayMetrics().densityDpi;
        float h =(scale / 160.0F);
        return h;
    }
    /**
     * 获取屏幕宽度
     * @param context the context
     * @return the int
     */
    public static float getScreenWidthDp(Context context) {
    	// 获取屏幕密度（方法2） 
    	DisplayMetrics dm = new DisplayMetrics(); 
    	dm = context.getResources().getDisplayMetrics(); 
    	return  dm.xdpi;      // 屏幕宽（像素，如：480px） 
    }
    /**
     * 获取屏幕高度
     * @param context the context
     * @return the int
     */
    public static int getScreenHeight(Context context) {
    	// 获取屏幕密度（方法2） 
    	DisplayMetrics dm = new DisplayMetrics(); 
    	dm = context.getResources().getDisplayMetrics(); 
    	return  dm.heightPixels;      // 屏幕宽（像素，如：480px） 
    }

	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		if(result==0){
			return getStatusHeight2(context);
		}
		return result;
	}
	private static int getStatusHeight2(Context context) {

		int statusHeight = -1;
		try {
			Class clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusHeight;
	}
    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    public static boolean isPadSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        // 屏幕尺寸
        double screenInches = Math.sqrt(x + y);
        // 大于6尺寸则为Pad
        if (screenInches >= 6.0) {
            return true;
        }
        return false;
    }
}
