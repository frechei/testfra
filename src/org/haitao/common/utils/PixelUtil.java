package org.haitao.common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;


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
     *
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
     *
     * @param value   the value
     * @param context the context
     * @return the int
     */
    public static int getScreenWidth(Context activity) {
        // 获取屏幕密度（方法2） 
        DisplayMetrics dm = new DisplayMetrics(); 
        dm = activity.getResources().getDisplayMetrics(); 
        return  dm.widthPixels;      // 屏幕宽（像素，如：480px） 
    }
    public  static float getScreenScale(Context context) {
        float scale = (float)context.getResources().getDisplayMetrics().densityDpi;
        float h =(scale / 160.0F);
        return h;
    }
    /**
     * 获取屏幕宽度
     *
     * @param value   the value
     * @param context the context
     * @return the int
     */
    public static float getScreenWidthDp(Context activity) {
    	// 获取屏幕密度（方法2） 
    	DisplayMetrics dm = new DisplayMetrics(); 
    	dm = activity.getResources().getDisplayMetrics(); 
    	return  dm.xdpi;      // 屏幕宽（像素，如：480px） 
    }
    /**
     * 获取屏幕高度
     *
     * @param value   the value
     * @param context the context
     * @return the int
     */
    public static int getScreenHeight(Context activity) {
    	// 获取屏幕密度（方法2） 
    	DisplayMetrics dm = new DisplayMetrics(); 
    	dm = activity.getResources().getDisplayMetrics(); 
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
}
