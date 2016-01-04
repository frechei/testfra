package org.haitao.common.http;

import java.util.Map;
import org.haitao.common.http.HttpURLUtils.HttpCallBack;
import android.annotation.SuppressLint;
import android.os.AsyncTask;


/**
 * @author Administrator
 *  异步http
 */
public class AsyncTaskHttp extends AsyncTask<Object, Integer , String> {
	
/*  AsyncTask的三个泛型参数说明（三个参数可以是任何类型） 
	第一个参数：传入doInBackground()方法的参数类型 
	第二个参数：传入onProgressUpdate()方法的参数类型 
	第三个参数：传入onPostExecute()方法的参数类型，也是doInBackground()方法返回的类型*/
	
    private String url = null ;
    private HttpCallBack httpCallBack = null  ;
    private HttpURLUtils http  = null ;

    public AsyncTaskHttp(String url, Map<String,Object> param,HttpCallBack httpCallBack,boolean isPost) {
        this.url = url;
        this.httpCallBack = httpCallBack ;
        if (http == null ){
            http = new HttpURLUtils() ;
        }
        //开始执行
        execute(url,param,isPost) ;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if ( httpCallBack != null ){
            httpCallBack.start();
        }

    }
    @Override
    protected String doInBackground(Object... params) {
        if ( http == null ){
            http = new HttpURLUtils() ;
        }
        if((Boolean)params[3]){
        	return http.doPost((String)params[0],(Map<String,Object>)params[1]);
        }else{
        	return http.doGet((String)params[0],(Map<String,Object>)params[1]);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if ( httpCallBack != null ){
            httpCallBack.finished( result ) ;
        }

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        if ( httpCallBack != null ){
            httpCallBack.cancle();
        }

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values) ;

        if ( httpCallBack != null ){
            httpCallBack.progressUpdate( values[0] ) ;
        }
    }

    @SuppressLint("NewApi")
	@Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }
}

