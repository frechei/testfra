package org.haitao.common.utils;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * <b>decription:</b>  log 工具类 <br>
 * <b>creat:</b>  2016-8-5 下午3:32:54 
 * @author haitao
 * @version 1.0
 */
public class AppLog {
	
	  private static final int DEBUG = 3;
	  private static final int ERROR = 6;
	  private static final int ASSERT = 7;
	  private static final int INFO = 4;
	  private static final int VERBOSE = 2;
	  private static final int WARN = 5;

	  /**
	   * Android's max limit for a log entry is ~4076 bytes,
	   * so 4000 bytes is used as chunk size since default charset
	   * is UTF-8
	   */
	  private static final int CHUNK_SIZE = 4000;

	  /**
	   * It is used for json pretty print
	   */
	  private static final int JSON_INDENT = 4;



	  /**
	   * tag is used for the Log, the name is a little different
	   * in order to differentiate the logs easily with the filter
	   */
	  private static String tag="log";

	 private static boolean IS_DEBUG=true;
	
	/**
	 * 只有debug模式下才会输出log
	 * @param debug
	 */
	public static void setDedug (boolean debug){
		IS_DEBUG =debug;
	}
	
	public static void i(String tag,Object msg){
		if (IS_DEBUG) {
			log(INFO, tag, checkNotNull(msg));
		}
	}
	public static void v(String tag,Object  msg){
		if (IS_DEBUG) {
			 log(VERBOSE, tag, checkNotNull(msg));
		}
	}
	public static void d(String tag,Object  msg){
		if (IS_DEBUG) {
			 log(DEBUG, tag, checkNotNull(msg));
		}
	}
	public static void e(String tag,Object  msg){
		if (IS_DEBUG) {
			 log(ERROR, tag, checkNotNull(msg));
		}
	}
	public static void w(String tag,Object  msg){
		if (IS_DEBUG) {
			 log(WARN, tag, checkNotNull(msg));
		}
	}
	public static void e(Object  msg){
		e("APP_LOG", msg);
	}
	private  static String checkNotNull(Object  msg){
		if(null==msg){
			return "msg is null";
		}
		return  msg.toString();
	}
	/**
	 * @param msg
	 * @param json
	 */
	public static void jsonAppend(String msg, String  json){
		if (IS_DEBUG) {
			log(ERROR, tag, msg+JsonFormatTool.formatJson(checkNotNull(json)));
		}
	}


	  /**
	   * Formats the json content and print it
	   *
	   * @param json the json content
	   */
	   public void json(String json) {
	    if (TextUtils.isEmpty(json)) {
	      d(tag,"Empty/Null json content");
	      return;
	    }
	    try {
	      if (json.startsWith("{")) {
	        JSONObject jsonObject = new JSONObject(json);
	        String message = jsonObject.toString(JSON_INDENT);
	        d(tag,message);
	        return;
	      }
	      if (json.startsWith("[")) {
	        JSONArray jsonArray = new JSONArray(json);
	        String message = jsonArray.toString(JSON_INDENT);
	        d(tag,message);
	      }
	    } catch (JSONException e) {
	      e(tag,e.getCause().getMessage() + "\n" + json);
	    }
	  }

	  /**
	   * Formats the json content and print it
	   *
	   * @param xml the xml content
	   */
	   public void xml(String xml) {
	    if (TextUtils.isEmpty(xml)) {
	      d(tag,"Empty/Null xml content");
	      return;
	    }
	    try {
	      Source xmlInput = new StreamSource(new StringReader(xml));
	      StreamResult xmlOutput = new StreamResult(new StringWriter());
	      Transformer transformer = TransformerFactory.newInstance().newTransformer();
	      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	      transformer.transform(xmlInput, xmlOutput);
	      d(tag,xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
	    } catch (TransformerException e) {
	      e(tag,e.getCause().getMessage() + "\n" + xml);
	    }
	  }


	  /**
	   * This method is synchronized in order to avoid messy of logs' order.
	   */
	  private static synchronized void log(int logType, String tag,String msg) {
//	    if (settings.getLogLevel() == LogLevel.NONE) {
//	      return;
//	    }
	    //get bytes of message with system's default charset (which is UTF-8 for Android)
	    byte[] bytes = msg.getBytes();
	    int length = bytes.length;
	    if (length <= CHUNK_SIZE) {
	      logContent(logType, tag, msg);
	      return;
	    }
	    for (int i = 0; i < length; i += CHUNK_SIZE) {
	      int count = Math.min(length - i, CHUNK_SIZE);
	      //create a new String with system's default charset (which is UTF-8 for Android)
	      logContent(logType, tag, new String(bytes, i, count));
	    }
	  }

	  private static void logContent(int logType, String tag, String chunk) {
	    String[] lines = chunk.split(System.getProperty("line.separator"));
	    for (String line : lines) {
	      logChunk(logType, tag,  line);
	    }
	  }

	  private static void logChunk(int logType, String tag, String chunk) {
	    switch (logType) {
	      case ERROR:
	    	  Log.e(tag, chunk);
	        break;
	      case INFO:
	    	  Log.i(tag, chunk);
	        break;
	      case VERBOSE:
	    	  Log.v(tag, chunk);
	        break;
	      case WARN:
	    	  Log.w(tag, chunk);
	        break;
	      case ASSERT:
	    	  Log.wtf(tag, chunk);
	        break;
	      case DEBUG:
	        // Fall through, log debug by default
	      default:
	    	  Log.d(tag, chunk);
	        break;
	    }
	  }

	/**
	 * 但是当我们没在AndroidManifest.xml中设置其debug属性时:
	 * 使用Eclipse运行这种方式打包时其debug属性为true,使用Eclipse导出这种方式打包时其debug属性为法false.
	 * 在使用ant打包时，其值就取决于ant的打包参数是release还是debug.
	 * 因此在AndroidMainifest.xml中最好不设置android:debuggable属性置，而是由打包方式来决定其值.
	 * @param context
	 * @return
	 */
	public static boolean initDebug(Context context) {
		try {
			ApplicationInfo info= context.getApplicationInfo();
			IS_DEBUG =(info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
		} catch (Exception e) {

		}
		return IS_DEBUG;
	}
}

