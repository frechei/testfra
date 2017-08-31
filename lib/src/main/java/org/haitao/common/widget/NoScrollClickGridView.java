/**
 * 
 */
package org.haitao.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**   
 * @Description  不能滑动的GridView
 * @author MR.Wang  
 * @date 2014-7-15 
 * @version V1.0   
 */
public class NoScrollClickGridView  extends NoScrollGridView {


	/**
	 * @param context
	 */
	public NoScrollClickGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}         
	/**
	 * @param context
	 * @param attrs
	 */
	public NoScrollClickGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public NoScrollClickGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){

		int expandSpec = MeasureSpec.makeMeasureSpec( Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 

	super.onMeasure(widthMeasureSpec, expandSpec);
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
