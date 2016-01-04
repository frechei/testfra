package org.haitao.common.bean;


/**
 * 对联系人信息必要的属性进行封装 以便于对联系人进行操作
 */
public class Contact{
	private String name;
	private String email = "";
	private String number;
	public static final String MimeType_Email = "vnd.android.cursor.item/email_v2";
	public static final String MimeType_Number = "vnd.android.cursor.item/phone_v2";
	public static final String MimeType_Name = "vnd.android.cursor.item/name";

	public Contact()
	{
		
	}

	/**
	 * 对 联系人进行封装
	 * 
	 * @param name 		姓名
	 * @param email 	Email邮箱
	 * @param number 	号码
	 */
	public Contact(String name, String email, String number)
	{
		this.name = name;
		this.email = email;
		this.number = number;
	}

	/**
	 * 对 联系人进行封装
	 * 
	 * @param name 		姓名
	 * @param number 	号码
	 */
	public Contact(String name, String number)
	{
		this.name = name;
		this.number = number;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}
}