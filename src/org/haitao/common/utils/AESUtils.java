package org.haitao.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * <b>decription:</b> AES加密 本方法没有采用常规的 将转换的结果进行base64 而是转换成hx <br>
 * <b>creat:</b>  2015-7-22 上午9:28:31 
 * @author haitao
 * @version 1.0
 */
public class AESUtils {
	
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
    /**
     * <p>
     * 文件加密
     * </p>
     *
     * @param key
     * @param sourceFilePath
     * @param destFilePath
     * @throws Exception
     */
    public static void encryptFile(String key, String sourceFilePath, String destFilePath) throws Exception {
        File sourceFile = new File(sourceFilePath);
        File destFile = new File(destFilePath);
        if (sourceFile.exists() && sourceFile.isFile()) {
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            destFile.createNewFile();
            InputStream in = new FileInputStream(sourceFile);
            OutputStream out = new FileOutputStream(destFile);
            SecretKeySpec secretKeySpec = new SecretKeySpec(getRawKey(key.getBytes()), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            CipherInputStream cin = new CipherInputStream(in, cipher);
            byte[] cache = new byte[1024];
            int nRead = 0;
            while ((nRead = cin.read(cache)) != -1) {
                out.write(cache, 0, nRead);
                out.flush();
            }
            out.close();
            cin.close();
            in.close();
        }
    }
    /**
     * <p>
     * 文件解密
     * </p>
     * @param key
     * @param sourceFilePath
     * @param destFilePath
     * @throws Exception
     */
    public static void decryptFile(String key, String sourceFilePath, String destFilePath) throws Exception {
        File sourceFile = new File(sourceFilePath);
        File destFile = new File(destFilePath);
        if (sourceFile.exists() && sourceFile.isFile()) {
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            destFile.createNewFile();
            FileInputStream in = new FileInputStream(sourceFile);
            FileOutputStream out = new FileOutputStream(destFile);
            SecretKeySpec secretKeySpec = new SecretKeySpec(getRawKey(key.getBytes()), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            CipherOutputStream cout = new CipherOutputStream(out, cipher);
            byte[] cache = new byte[1024];
            int nRead = 0;
            while ((nRead = in.read(cache)) != -1) {
                cout.write(cache, 0, nRead);
                cout.flush();
            }
            cout.close();
            out.close();
            in.close();
        }
    }

    /**
    * @Description:   转换密钥
    * @param seed
    * @throws Exception    参数
    * @return byte[] 
    */
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
   // 这种方式也可以获取密钥 密钥的处理不同结果不同
/*    private static byte[] getRawKey(byte[] key) {
	  SecretKey secretKey = new SecretKeySpec(key, "AES");
	  return  secretKey.getEncoded();
	}*/
       
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
       
    /**
    * @Description:hx 转换回来
    * @param hexString
    * @return    参数
    * @return byte[] 
    */
    private static byte[] toByte(String hexString) {   
        int len = hexString.length()/2;   
        byte[] result = new byte[len];   
        for (int i = 0; i < len; i++)   
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();   
        return result;   
    }   
  
    /**
    * @Description 流转化成hex
    * @param buf
    * @return 参数
    * @return String 
    */
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

}

