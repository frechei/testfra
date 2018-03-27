package org.haitao.common.utils;

import android.util.Base64;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


/**
 * <b>decription:</b>  DES BAEE64 <br>
 * <b>creat:</b>  2016-10-21 上午11:48:07 
 * @author haitao
 * @version 1.0
 */
public class DESUtil {


    /**
     * 加密
     * @param datasource byte[]
     * @param password   String
     * @return byte[]
     */
    public static byte[] encrypt(byte[] datasource, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            // 现在，获取数据并加密
            // 正式执行加密操作
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     *
     * @param datasource datasource
     * @param password   String
     * @return byte[]
     */
    public static String encryptToString(String datasource, String password) {
        byte[] result = encrypt(datasource.getBytes(), password);
        if (result == null)
            return null;
        return Base64.encodeToString(result, Base64.DEFAULT);
    }

    /**
     * 解密
     *
     * @param datasource datasource
     * @param password   String
     * @return byte[]
     */
    public static String decryptToString(String datasource, String password) {

        byte[] base64Array = Base64.decode(datasource, Base64.DEFAULT);
        byte[] contentArray = decrypt(base64Array, password);
        return new String(contentArray);
    }

    /**
     * 解密
     * @param src      byte[]
     * @param password String
     * @return byte[]
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, String password) {
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom random = new SecureRandom();
            // 创建一个DESKeySpec对象
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            // 创建一个密匙工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // 将DESKeySpec对象转换成SecretKey对象
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // 真正开始解密操作
            return cipher.doFinal(src);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}