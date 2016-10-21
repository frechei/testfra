package org.haitao.common.logger;

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
 * LoggerAll is a wrapper for logging utils
 * But more pretty, simple and powerful
 */
public final class LoggerPrinter  {

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
  private String tag;

  /**
   * Localize single tag and method count for each thread
   */
  private final ThreadLocal<String> localTag = new ThreadLocal<String>();
  private final ThreadLocal<Integer> localMethodCount = new ThreadLocal<Integer>();


  /**
   * It is used to change the tag
   *
   * @param tag is the given string which will be used in LoggerAll
   */
   public void init(String tag) {
    if (tag == null) {
      throw new NullPointerException("tag may not be null");
    }
    if (tag.trim().length() == 0) {
      throw new IllegalStateException("tag may not be empty");
    }
    this.tag = tag;
  }

   public LoggerPrinter t(String tag, int methodCount) {
    if (tag != null) {
      localTag.set(tag);
    }
    localMethodCount.set(methodCount);
    return this;
  }

   public void d(String message, Object... args) {
    log(DEBUG, message, args);
  }

   public void e(String message, Object... args) {
    e(null, message, args);
  }

   public void e(Throwable throwable, String message, Object... args) {
    if (throwable != null && message != null) {
      message += " : " + throwable.toString();
    }
    if (throwable != null && message == null) {
      message = throwable.toString();
    }
    if (message == null) {
      message = "No message/exception is set";
    }
    log(ERROR, message, args);
  }

   public void w(String message, Object... args) {
    log(WARN, message, args);
  }

   public void i(String message, Object... args) {
    log(INFO, message, args);
  }

   public void v(String message, Object... args) {
    log(VERBOSE, message, args);
  }

   public void wtf(String message, Object... args) {
    log(ASSERT, message, args);
  }

  /**
   * Formats the json content and print it
   *
   * @param json the json content
   */
   public void json(String json) {
    if (TextUtils.isEmpty(json)) {
      d("Empty/Null json content");
      return;
    }
    try {
      if (json.startsWith("{")) {
        JSONObject jsonObject = new JSONObject(json);
        String message = jsonObject.toString(JSON_INDENT);
        d(message);
        return;
      }
      if (json.startsWith("[")) {
        JSONArray jsonArray = new JSONArray(json);
        String message = jsonArray.toString(JSON_INDENT);
        d(message);
      }
    } catch (JSONException e) {
      e(e.getCause().getMessage() + "\n" + json);
    }
  }

  /**
   * Formats the json content and print it
   *
   * @param xml the xml content
   */
   public void xml(String xml) {
    if (TextUtils.isEmpty(xml)) {
      d("Empty/Null xml content");
      return;
    }
    try {
      Source xmlInput = new StreamSource(new StringReader(xml));
      StreamResult xmlOutput = new StreamResult(new StringWriter());
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      transformer.transform(xmlInput, xmlOutput);
      d(xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
    } catch (TransformerException e) {
      e(e.getCause().getMessage() + "\n" + xml);
    }
  }


  /**
   * This method is synchronized in order to avoid messy of logs' order.
   */
  private synchronized void log(int logType, String msg, Object... args) {
//    if (settings.getLogLevel() == LogLevel.NONE) {
//      return;
//    }
    String tag = getTag(); 
    String message = createMessage(msg, args);
    //get bytes of message with system's default charset (which is UTF-8 for Android)
    byte[] bytes = message.getBytes();
    int length = bytes.length;
    if (length <= CHUNK_SIZE) {
      logContent(logType, tag, message);
      return;
    }
    for (int i = 0; i < length; i += CHUNK_SIZE) {
      int count = Math.min(length - i, CHUNK_SIZE);
      //create a new String with system's default charset (which is UTF-8 for Android)
      logContent(logType, tag, new String(bytes, i, count));
    }
  }





  private void logContent(int logType, String tag, String chunk) {
    String[] lines = chunk.split(System.getProperty("line.separator"));
    for (String line : lines) {
      logChunk(logType, tag,  line);
    }
  }

  private void logChunk(int logType, String tag, String chunk) {
    String finalTag = formatTag(tag);
    switch (logType) {
      case ERROR:
    	  Log.e(finalTag, chunk);
        break;
      case INFO:
    	  Log.i(finalTag, chunk);
        break;
      case VERBOSE:
    	  Log.v(finalTag, chunk);
        break;
      case WARN:
    	  Log.w(finalTag, chunk);
        break;
      case ASSERT:
    	  Log.wtf(finalTag, chunk);
        break;
      case DEBUG:
        // Fall through, log debug by default
      default:
    	  Log.d(finalTag, chunk);
        break;
    }
  }


  private String formatTag(String tag) {
    if (!TextUtils.isEmpty(tag) && !TextUtils.equals(this.tag, tag)) {
      return this.tag + "-" + tag;
    }
    return this.tag;
  }

  /**
   * @return the appropriate tag based on local or global
   */
  private String getTag() {
    String tag = localTag.get();
    if (tag != null) {
      localTag.remove();
      return tag;
    }
    return this.tag;
  }

  private String createMessage(String message, Object... args) {
	  if(args==null || message==null){
		  return "warn msg is null";
	  }
	  try {
		  return args.length == 0 ? message : String.format(message, args);
	} catch (Exception e) {
		return "log format error "+message;
	}
  }

}
