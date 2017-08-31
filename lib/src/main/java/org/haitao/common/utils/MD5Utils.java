package org.haitao.common.utils;

import java.security.MessageDigest;

public class MD5Utils {

    /**
     * md5 32位加密小写
     * @param plainText
     * @return
     */
    public final static String get32MD5(String plainText) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = plainText.getBytes();
            //使用MD5创建MessageDigest对象
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte b = md[i];
                //System.out.println((int)b);
                //将没个数(int)b进行双字节加密
                str[k++] = hexDigits[b >> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * d5 32位加密大写
     * @param plainText
     * @return
     */
    public final static String get32MD5UpperCase(String plainText) {
        String result = get32MD5(plainText);
        if (result!=null){
            return  result.toUpperCase();
        }
        return  result;
    }

    /**
     * d5 16位加密小写
     * @param plainText
     * @return
     */
    public final static String get16MD5(String plainText) {
        String result = get32MD5(plainText);
        if (result!=null){
            return  result.substring(8,24);
        }
        return  result;
    }

    /**
     * d5 16位加密大写
     * @param plainText
     * @return
     */
    public final static String get16MD5UpperCase(String plainText) {
        String result = get16MD5(plainText);
        if (result!=null){
            return  result.toUpperCase();
        }
        return  result;
    }

}
