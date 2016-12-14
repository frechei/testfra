package org.haitao.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * <b>decription:</b> 状态栏工具  <br>
 * <b>creat:</b>  2016-8-5 下午3:58:19 
 * @author haitao
 * @version 1.0
 */
public class StatusBarCompat
{
    private static final int INVALID_VAL = -1;
    private static final int COLOR_DEFAULT = Color.parseColor("#20000000");
	
	// 检测MIUI 
	private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
	private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
	private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
	
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void compat(Activity activity, int statusColor)
    {
        //getWindow().setStatusBarColor(Color.parseColor("#00FFFFFF"));
        //activity.getWindow().setStatusBarColor( statusColor);// 穿透状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (statusColor != INVALID_VAL)
            {
                activity.getWindow().setStatusBarColor(statusColor);
            }
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            int color = COLOR_DEFAULT;
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (statusColor != INVALID_VAL)
            {
                color = statusColor;
            }
//            View statusBarView = contentView.getChildAt(0);
//            //改变颜色时避免重复添加statusBarView
//            if (statusBarView != null && statusBarView.getMeasuredHeight() == getStatusBarHeight(activity))
//            {
//                statusBarView.setBackgroundColor(color);
//                return;
//            }
//            statusBarView = new View(activity);
//            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    getStatusBarHeight(activity));
//            statusBarView.setBackgroundColor(color);
//            contentView.addView(statusBarView, lp);
            
            
            int index =0;
			if (contentView.getChildCount()>0){
				index =contentView.getChildCount()-1;
			}
			View statusBarView = contentView.getChildAt(index);

			//改变颜色时避免重复添加statusBarView
			if (statusBarView != null && "hadAdd".equals(statusBarView.getTag()) )
			{
				statusBarView.setBackgroundColor(color);
				return;
			}
			statusBarView = new View(activity);
			ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					getStatusBarHeight(activity));
			statusBarView.setBackgroundColor(color);
			statusBarView.setTag("hadAdd");
			statusBarView.setLayoutParams(lp);
			contentView.addView(statusBarView,index+1);
        }

    }

    public static void compat(Activity activity)
    {
        compat(activity, INVALID_VAL);
    }


    public static int getStatusBarHeight(Context context)
    {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    
    /**
     * 使状态栏透明(5.0以上半透明效果,不建议使用)
     *
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param activity 需要设置的activity
     */
    public static void setTranslucentDiff(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setRootView(activity);
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void setFullScreen(Activity activity) {
    	if ( Build.VERSION.SDK_INT >=11){
    		activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		}
		StatusBarCompat.compat(activity, Color.parseColor("#00000000"));
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void compatWhite(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			compat(activity,Color.WHITE);
		}
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void compatWhiteBlack(Activity activity) {
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    		activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    		compat(activity,Color.WHITE);
    	}else {
            compat(activity,Color.BLACK);
        }
    }
    /**
     * 设置根布局参数
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private static void setRootView(Activity activity) {
        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        rootView.setFitsSystemWindows(true);
        rootView.setClipToPadding(true);
    }
	/**
	 * 是否小米
	* @return    参数
	* @return boolean 
	*/
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
	/**
	* 是否魅族  
	* @return boolean 
	*/
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
