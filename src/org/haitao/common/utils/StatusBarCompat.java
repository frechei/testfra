package org.haitao.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by zhy on 15/9/21.
 */
public class StatusBarCompat
{
    private static final int INVALID_VAL = -1;
   // private static final int COLOR_DEFAULT = Color.parseColor("#FF0288D1");
   private static final int COLOR_DEFAULT = Color.parseColor("#20000000");

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
    /**
     * 设置根布局参数
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private static void setRootView(Activity activity) {
        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        rootView.setFitsSystemWindows(true);
        rootView.setClipToPadding(true);
    }
}
