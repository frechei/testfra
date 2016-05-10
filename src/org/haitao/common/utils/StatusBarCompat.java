package org.haitao.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

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
			View statusBarView = contentView.getChildAt(0);
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
			contentView.addView(statusBarView,0);
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
}
