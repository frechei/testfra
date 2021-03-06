package org.haitao.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidateUtil {
	static boolean flag = false;
	static String regex = "";

	public static boolean check(String str, String regex) {
		try {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(str);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}


	/**验证非空
	 * @param notEmpty
	 * @return
	 */
	public static boolean checkNotEmpty(String notEmpty) {
		regex = "^\\s*$";
		return check(notEmpty, regex) ? false : true;
	}

	/**
	 * 验证邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		String regex = "^\\w+[-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$ ";
		return check(email, regex);
	}

	/**
	 * 验证手机号码
	 * 
	 * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、187、188、147
	 * 联通号码段:130、131、132、136、185、186、145 电信号码段:133、153、180、189
	 * 添加170
	 * @param cellphone
	 * @return
	 */
	public static boolean checkCellphone(String cellphone) {
		String regex = "^((13[0-9])|(14[5-7])|(15([0-9]))|(17([0-9]))|(18[0-9]))\\d{8}$";
		return check(cellphone, regex);
	}

	/**
	 * 验证固话号码
	 * 
	 * @param telephone
	 * @return
	 */
	public static boolean checkTelephone(String telephone) {
		String regex = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";
		return check(telephone, regex);
	}

	/**
	 * 验证传真号码
	 * 
	 * @param fax
	 * @return
	 */
	public static boolean checkFax(String fax) {
		String regex = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";
		return check(fax, regex);
	}

	/**
	 * 验证QQ号码
	 * 
	 * @param QQ
	 * @return
	 */
	public static boolean checkQQ(String QQ) {
		String regex = "^[1-9][0-9]{4,} $";
		return check(QQ, regex);
	}
	
	/**
	 * 验证是否有数字
	 * @param str
	 * @return
	 */
	public static boolean checkHasNum(String str) {
		String regex=".*[0-9]+.*";
		Matcher m2=Pattern.compile(regex).matcher(str);
		return m2.matches();
	}
	/**
	 * 验证是否有字母
	 * @param str
	 * @return
	 */
	public static boolean checkHasLetter(String str) {
		String regex=".*[a-zA-Z]+.*";
		Matcher m2=Pattern.compile(regex).matcher(str);
		return m2.matches();
	}
	/**
	 * 验证是否有字母
	 * @param str
	 * @return
	 */
	public static boolean checkIsOnlyNum(String str) {
		boolean result=str.matches("[0-9]+");
		return result;
	}
}