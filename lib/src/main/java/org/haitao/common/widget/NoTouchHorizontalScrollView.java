package org.haitao.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.ListView;

/**
 * Description
 * Author by wangHaitao(a758277560@gmail.com)
 * Created  on 2015/11/17.
 * Version 1.0
 */
public class NoTouchHorizontalScrollView extends  HorizontalScrollView {

    public NoTouchHorizontalScrollView(Context context) {
        super(context);
    }

    public NoTouchHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoTouchHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

	
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
            return true;// true 拦截事件自己处理，禁止向下传递
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
            return false;// false 自己不处理此次事件以及后续的事件，那么向上传递给外层view
    }
}
