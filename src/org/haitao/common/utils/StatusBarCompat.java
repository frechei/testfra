package org.haitao.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;
import android.annotation.SuppressLint;
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
import android.widget.FrameLayout;

/**
 * <b>decription:</b> 状态栏工具  <br>
 * <b>creat:</b>  2016-8-5 下午3:58:19 
 * @author haitao
 * @version 1.0
 */
@SuppressLint("NewApi")
public class StatusBarCompat{
	  // 检测MIUI
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    private static int isMeizu = -1;//-1 没有校验 0是
    private static int isMiui = -1;//-1 没有校验 0是
    public static boolean FITSSYSTEMWINDOWS=false;
    /**
     * 只有android:fitsSystemWindows false 才能透过
     * 穿透到状态栏
     * @param activity
     */
    public  static void setTranslucentDiff(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);  //需要设置这个才能设置状态栏颜色
            statusColor(activity,Color.TRANSPARENT) ;//设置状态栏颜色
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            contentView.setPadding(0, 0, 0, 0);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ){
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            statusColor(activity,Color.TRANSPARENT) ;//设置状态栏颜色
            contentView.setPadding(0, 0, 0, 0);
        }

    }
    /**
     * 设置状态栏颜色
     * @param activity
     * @param statusColor
     */
    public static void compat(Activity activity, int statusColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);  //需要设置这个才能设置状态栏颜色
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            if (statusColor!=-88)
            statusColor(activity,statusColor) ;//设置状态栏颜色
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (FITSSYSTEMWINDOWS){
                contentView.setPadding(0, 0, 0, 0);
            }else {
               contentView.setPadding(0, getStatusBarHeight(activity), 0, 0); //FITSSYSTEMWINDOWSfalse+ View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN 要设计间距
            }

        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (FITSSYSTEMWINDOWS){
                contentView.setPadding(0, 0, 0, 0);
            }else {
                contentView.setPadding(0, getStatusBarHeight(activity), 0, 0);
            }
            if (statusColor!=-88)
            statusColor(activity,statusColor) ;//设置状态栏颜色
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );
        }
    }

    public static void statusColor(Activity activity, int statusColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(statusColor);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
            int index =0;
            ViewGroup  mViewGroup=  (ViewGroup)activity.getWindow().getDecorView();
            if (mViewGroup.getChildCount()>0){
                index =mViewGroup.getChildCount()-1;
            }
            View statusBarView = mViewGroup.getChildAt(index);
            //改变颜色时避免重复添加statusBarView
            if (statusBarView != null && "hadAdd".equals(statusBarView.getTag()) )
            {
                statusBarView.setBackgroundColor(statusColor);
                return;
            }
            statusBarView = new View(activity);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(statusColor);
            statusBarView.setTag("hadAdd");
            statusBarView.setLayoutParams(lp);
            mViewGroup.addView(statusBarView,index+1);
        }
    }

    public static void hidebar(Activity activity) {

        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;  //防止系统栏隐藏时内容区域大小发生变化

        uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;  //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态栏遮住。
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);  //需要设置这个才能设置状态栏颜色
        uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.INVISIBLE;
        activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
    public static void showbar(Activity activity) {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;  //防止系统栏隐藏时内容区域大小发生变化
        uiFlags |= View.SYSTEM_UI_FLAG_VISIBLE;
        activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void setFullScreen(Activity activity) {
        if ( Build.VERSION.SDK_INT >=11){
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        compat(activity, Color.parseColor("#00000000"));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void compatWhiteBlack(Activity activity) {

        if(isMIUI()){
            statusColor(activity,-88);
            setStatusBarDarkModeMIUI(true,activity);

        }else if(isMeizu()){
            statusColor(activity,-88);
            setStatusBarDarkModeMeizu(true,activity);
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                compat(activity,Color.WHITE);
                activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            }else {
                //有些手机4.4 不能设置
                //compat(activity,Color.BLACK);
                compat(activity,Color.parseColor("#BDBDBD"));
            }
        }

    }

    /**
     * 是否小米
     * @return    参数
     * @return boolean
     */
    public static boolean isMIUI() {
        if(isMiui==-1){
            Properties prop = new Properties();
            boolean isMIUI;
            try {
                prop.load(new FileInputStream(new File(Environment
                        .getRootDirectory(), "build.prop")));
            } catch (IOException e) {
                e.printStackTrace();
                isMiui=1;
                return false;
            }
            isMIUI = prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
            isMiui= isMIUI?0:1;
            return isMIUI;
        }else{
            return isMiui==0;
        }

    }
    /**
     * 是否魅族
     * @return boolean
     */
    public static boolean isMeizu() {
        if(isMeizu==-1){
            if("Meizu".equals(android.os.Build.MANUFACTURER)){
                isMeizu=0;
                return true;
            }
            try {
                // Invoke Build.hasSmartBar()
                final Method method = Build.class.getMethod("hasSmartBar");
                isMeizu=(method != null)?0:1;
                return method != null;
            } catch (final Exception e) {
                isMeizu =1;
                return false;
            }
        }else{
            return isMeizu==0;
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
    public static boolean setStatusBarDarkModeMeizu(boolean darkmode, Activity activity) {
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
                if (darkmode) {
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
