package org.haitao.common.utils;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * <b>decription:</b> AES加密  <br>
 * <b>creat:</b>  2015-7-22 上午9:28:31 
 * @author haitao
 * @version 1.0
 */
public class CommonCipher {
	
	public static final String AES_KEY = "neiquan";
	
	/**
	 * 加密 方法
	 * @param key
	 * @param src
	 * @return
	 */
	public static String encrypt(String key, String src) {   
        byte[] rawKey;
        byte[] result = null;
		try {
			rawKey = getRawKey(key.getBytes());
			result = encrypt(rawKey, src.getBytes());   
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
        return toHex(result);   
    }   
    /**
     * 解密方法
     * @param key
     * @param encrypted
     * @return
     */
    public static String decrypt(String key, String encrypted) {
    	AppLog.e("decrypt="+encrypted);
        byte[] rawKey;
		try {
			rawKey = getRawKey(key.getBytes());
			byte[] enc = toByte(encrypted);   
			byte[] result = decrypt(rawKey, enc);   
			return new String(result);   
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return null;
    }   
  
    private static byte[] getRawKey(byte[] seed) throws Exception {   
        KeyGenerator kgen = KeyGenerator.getInstance("AES"); 
        // SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法
         SecureRandom sr = null;
       if (android.os.Build.VERSION.SDK_INT >=17) {
       sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
       } else {
        sr = SecureRandom.getInstance("SHA1PRNG");
       } 
        sr.setSeed(seed);   
        kgen.init(256, sr); //256 bits or 128 bits,192bits
        SecretKey skey = kgen.generateKey();   
        byte[] raw = skey.getEncoded();   
        return raw;   
    }   
  
       
    private static byte[] encrypt(byte[] key, byte[] src) throws Exception {   
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");   
        Cipher cipher = Cipher.getInstance("AES");   
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);   
        byte[] encrypted = cipher.doFinal(src);   
        return encrypted;   
    }   
  
    private static byte[] decrypt(byte[] key, byte[] encrypted) throws Exception {   
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");   
        Cipher cipher = Cipher.getInstance("AES");   
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);   
        byte[] decrypted = cipher.doFinal(encrypted);   
        return decrypted;   
    }   
  
    public static String toHex(String txt) {   
        return toHex(txt.getBytes());   
    }   
    public static String fromHex(String hex) {   
        return new String(toByte(hex));   
    }   
       
    private static byte[] toByte(String hexString) {   
        int len = hexString.length()/2;   
        byte[] result = new byte[len];   
        for (int i = 0; i < len; i++)   
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();   
        return result;   
    }   
  
    private static String toHex(byte[] buf) {   
        if (buf == null)   
            return "";   
        StringBuffer result = new StringBuffer(2*buf.length);   
        for (int i = 0; i < buf.length; i++) {   
            appendHex(result, buf[i]);   
        }   
        return result.toString();   
    }   
    private final static String HEX = "0123456789ABCDEF";   
    private static void appendHex(StringBuffer sb, byte b) {   
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));   
    }   

	/**
	 * 获取MD5 结果字符串
	 * 
	 * @param source
	 * @return
	 */
	public static String encode(byte[] source) {
		String s = null;
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); 
			char str[] = new char[16 * 2]; 
			int k = 0; 
			for (int i = 0; i < 16; i++) { 
				byte byte0 = tmp[i]; 
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; 
				str[k++] = hexDigits[byte0 & 0xf]; 
			}
			s = new String(str); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
}

