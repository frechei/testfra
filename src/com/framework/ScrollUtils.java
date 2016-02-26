package com.framework;

import org.haitao.common.utils.AppLog;
import android.os.Handler;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ScrollUtils {

	private static int i=0;
	private static int k=5000;
	private static boolean loop =true;
	public static void scroll(final HorizontalScrollView hv,String str,final TextView name_tv){
		
		name_tv.setText(str);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				loop=true;
				k=5000;
				i=0;
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						while (i<k && loop) {
							AppLog.e("i----------------->>>>>"+i);
							name_tv.post(new Runnable() {
								@Override
								public void run() {
									//name_tv.setPadding(hv.getWidth(), 0, hv.getWidth(), 0);
									LinearLayout.LayoutParams lp = (LayoutParams) name_tv.getLayoutParams();
									lp.leftMargin=hv.getWidth();
									lp.rightMargin=hv.getWidth();
									name_tv.setLayoutParams(lp);
									i=i+1;
									hv.scrollTo(i, 0);
									k = hv.getWidth()*2+name_tv.getWidth();
								}
							});
							try {
								Thread.sleep(5);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						AppLog.e("i--------------------------------------------break=i"+i+"flag="+loop);
					}
				}).start();
			}
		},80);
	}
	public static void stop(){
		loop=false;
		i=0;
	}
}
