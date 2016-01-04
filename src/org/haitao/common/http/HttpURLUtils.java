package org.haitao.common.http;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
public class HttpURLUtils {
	
	private static final String SERVLET_POST = "POST" ;
	private static final String SERVLET_GET = "GET" ;
	
	private static String prepareParam(Map<String,Object> paramMap){
		StringBuffer sb = new StringBuffer();
		if(paramMap.isEmpty()){
			return "" ;
		}else{
			for(String key: paramMap.keySet()){
				String value = (String)paramMap.get(key);
				if(sb.length()<1){
					sb.append(key).append("=").append(value);
				}else{
					sb.append("&").append(key).append("=").append(value);
				}
			}
			return sb.toString();
		}
	}
	
	public static String  doPost(String urlStr,Map<String,Object> paramMap ) {
		URL url;
		HttpURLConnection conn = null;
		StringBuilder response = null;
		try {
			url = new URL(urlStr);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod(SERVLET_POST);
			String paramStr = prepareParam(paramMap);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			OutputStream os = conn.getOutputStream();     
			os.write(paramStr.toString().getBytes("utf-8"));     
			os.close();   		
			BufferedReader red = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line ;
			response = new StringBuilder();
			while( (line =red.readLine()) != null ){
				response.append(line);
			}
			red.close();
		}catch (MalformedURLException e) {
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return  response ==null ?null:response.toString();
	}
	
	public static String doGet(String urlStr,Map<String,Object> paramMap ){
		
		URL url;
		HttpURLConnection conn = null;
		StringBuilder response = null;
		String paramStr = prepareParam(paramMap);
		if(paramStr == null || paramStr.trim().length()<1){
			
		}else{
			urlStr +="?"+paramStr;
		}
		try {
			url = new URL(urlStr);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod(SERVLET_GET);
			conn.setRequestProperty("Content-Type","text/html; charset=UTF-8");
			
			conn.connect();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line ;
			response = new StringBuilder();
			while( (line =br.readLine()) != null ){
				response.append(line);
			}
			br.close();
		}catch (MalformedURLException e) {
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return  response ==null ?null:response.toString();
	}
	public abstract  class HttpCallBack {

	    protected void start(){

	    }

	    protected void progressUpdate( Integer progress ){

	    }

	    protected void finished( String result ) {
	    }

	    protected void cancle(){

	    }

	}
	
}
