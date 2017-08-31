package org.haitao.common.user;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import org.haitao.common.utils.AESUtils;
import org.haitao.common.utils.SharedPreferencesUtil;

/**
 * Description 
 * Author by wangHaitao(a758277560@gmail.com)
 * Created  on 2015/11/19.
 * Version 1.0
 */
public class UserUtils {

    private static final String KEY_USER ="user";
    private static final String KEY_TOKEN ="token";

    
    /**
     * 存储对象
     * @param context
     * @param obj
     */
    public static void saveObject(Context context,Object obj){
    	if(obj!=null)
        saveObject(context,obj,obj.getClass().getName());
    }

    /**
     * 存储对象
     * @param context
     * @param obj
     * @param key
     */
    public static void saveObject(Context context,Object obj,String key){
        if(obj==null){
            SharedPreferencesUtil.delete(context, key);
        }else{
            String userStr = JSON.toJSONString(obj);
            SharedPreferencesUtil.saveByDes(context, key,AESUtils.encrypt(AESUtils.AES_KEY, userStr));
        }
    }
    /**
     * 获取对象
     * @param context
     * @param clazz
     * @return
     */
    public static <T> T getObject(Context context, Class<T> clazz){
    	if(clazz!=null)
    	return getObject(context, clazz, clazz.getName());
    	return null;
    }
    
    /**
     * 获取对象
     * @param context
     * @param clazz
     * @param key
     * @return
     */
    public static <T> T getObject(Context context, Class<T> clazz ,String key){
    	String userStr =  SharedPreferencesUtil.getByDes(context, key);
    	if (null ==userStr ||"".equals(userStr)) {
    		return null;
    	}
    	return (T) JSON.parseObject(AESUtils.decrypt(AESUtils.AES_KEY, userStr),clazz);
    }
    public static <T> void delObject(Context context,Class<T> clazz){
    	if(clazz!=null)
    	 SharedPreferencesUtil.delete(context, clazz.getName());
    }
    public static <T> void delObject(Context context,String key){
    	if(key!=null)
    		SharedPreferencesUtil.delete(context,key);
    }
    /**
     * 获取user
     * @param context
     * @param clazz
     * @return
     */
    public static <T> T getUser(Context context, Class<T> clazz ){
        return getObject(context, clazz, KEY_USER);
    }
    

    /**
     * 获取user
     * @param context
     * @param user
     */
    public static void saveUser(Context context,Object user){
       saveObject(context, user, KEY_USER);
    }


    public static void saveToken (Context context,String token){
        if(token==null){
            SharedPreferencesUtil.delete( context, KEY_TOKEN);
        }else{
            SharedPreferencesUtil.saveByDes(context, KEY_TOKEN,AESUtils.encrypt(AESUtils.AES_KEY,token));
        }
    }

    public static String getToken(Context context){
        String userStr =  SharedPreferencesUtil.getByDes(context, KEY_TOKEN);
        if (null ==userStr || "".equals(userStr)) {
            return null;
        }
        return AESUtils.decrypt(AESUtils.AES_KEY, userStr);
    }
}
