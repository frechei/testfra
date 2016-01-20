package com.framework;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				showPpw();
			}
		},3000);
	}
	private void showPpw(){
		View view = getLayoutInflater().inflate(R.layout.test, null);
		PopupWindow popupWindowPayInfo = new PopupWindow(view, AbsListView.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindowPayInfo.setBackgroundDrawable(new BitmapDrawable());
        // 设置PopupWindow的弹出和消失效果
        // popupWindow.setAnimationStyle(R.style.AlertChooserAnimation);
        backgroundAlpha(0.6f);
        popupWindowPayInfo.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindowPayInfo.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindowPayInfo.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM, 0, 0);
        // 添加pop窗口关闭事件
       // popupWindowPayInfo.setOnDismissListener(new poponDismissListener());
//        mPaytype = (TextView) view.findViewById(R.id.pay_type_tv);
//        TextView tv = (TextView) view.findViewById(R.id.total_money);
//        tv.setText("￥" + needPay);
//        ImageView pay1 = (ImageView) view.findViewById(R.id.delete_btn);
//        RelativeLayout pay2 = (RelativeLayout) view.findViewById(R.id.pay_type);
//        TextView pay3 = (TextView) view.findViewById(R.id.commit);
//        password_lay = (LinearLayout) view.findViewById(R.id.password_lay);
//        passwordEt = (EditText) view.findViewById(R.id.password);
//        pay1.setOnClickListener(this);
//        pay2.setOnClickListener(this);
//        pay3.setOnClickListener(this);
	}
	 public void backgroundAlpha(float bgAlpha) {
	        WindowManager.LayoutParams lp = getWindow().getAttributes();
	        lp.alpha = bgAlpha; // 0.0-1.0
	        getWindow().setAttributes(lp);
	    }
}
