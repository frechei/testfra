package org.haitao.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.Map;
import java.util.Set;

/**
 * @author miss
 *
 */
public class SharedPreferencesUtil {
	
	private  static String APP_NAME="neiquan";
	/**
	 * 设置存储位置
	 * @param fileName
	 */
	public static void setDefaultFileName(String fileName){
		APP_NAME= fileName;
	}
	public static boolean readIsFirstUse(Context context){
		SharedPreferences preferences=context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE );
		boolean isUse=preferences.getBoolean("firstUse", true);
		return isUse;
	}
	
	public static void writeIsFirstUse(Context context){
		SharedPreferences preferences=context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE );
		Editor editor=preferences.edit();
		editor.putBoolean("firstUse", false);
		editor.commit();
	}
    public static void add(Context context,Map<String, String> map) {
        Set<String> set = map.keySet();
        SharedPreferences preferences= context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
        Editor editor=preferences.edit();
        for (String key : set) {
            editor.putString(key, map.get(key));
        }
        editor.commit();
    }
    
    public static void add(Context context,String key,String content) {
    	SharedPreferences preferences= context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
    	Editor editor=preferences.edit();
    	editor.putString(key, content);
    	editor.commit();
    }
    public static void add(Context context,String key,boolean flag) {
    	SharedPreferences preferences= context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE );
    	Editor editor=preferences.edit();
    	editor.putBoolean(key, flag);
    	editor.commit();
    }
    /**
     * 获取信息
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context,String key){
    	SharedPreferences preferences= context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE );
    	if (preferences != null) {
            return preferences.getBoolean(key, true);
        }
        return true;
    }
    /**
     * 删除信息
     */
    public static void deleteAll(Context context) {
        SharedPreferences preferences= context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE );
        Editor editor=preferences.edit();
        editor.clear();
        editor.commit();
    }
 
    /**
     * 删除一条信息
     */
    public static void delete(Context context,String key){
        SharedPreferences preferences= context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE );
        Editor editor=preferences.edit();
        editor.remove(key);
        editor.commit();
    }
 
    /**
     * 获取信息
     * @param key
     * @return
     */
    public static String get(Context context,String key){
    	SharedPreferences preferences= context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE );
    	if (preferences != null) {
            return preferences.getString(key, "");
        }
        return "";
    }
    /**
     * des 存储
     * @param context
     * @param key
     * @param content
     */
    public static  void saveByDes(Context context,String key,String content) {
        saveByDes(context, APP_NAME, key, content);
    }
    public static  void saveByDes(Context context,String file,String key,String content) {
        if (content!=null) {
            SharedPreferences preferences= context.getSharedPreferences(file, Context.MODE_PRIVATE);
            Editor editor=preferences.edit();
            editor.putString(key, CommonCipher.encrypt(CommonCipher.AES_KEY, content));
            editor.commit();
        }
    }
    public static  String getByDes(Context context,String key) {
      return  getByDes(context,APP_NAME,key);
    }
    public static  String getByDes(Context context,String file,String key) {
        SharedPreferences preferences= context.getSharedPreferences(file, Context.MODE_PRIVATE );
        String content =  preferences.getString(key, "");
        if ("".equals(content)) {
            return null;
        }
        return CommonCipher.decrypt(CommonCipher.AES_KEY, content);
    }
}
