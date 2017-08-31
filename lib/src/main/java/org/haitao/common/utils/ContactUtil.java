package org.haitao.common.utils;


import java.util.ArrayList;
import java.util.List;
import org.haitao.common.bean.Contact;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * 对电话本联系人的操作进行了一系列的封装
 * @author 肖蕾
 *
 */
public class ContactUtil
{
	/**
	 * 获取所有联系人
	 * @param context 上下文
	 * @return 对联系人信息封装的Contact的类
	 */
	public static List<Contact> getContacts(Context context)
	{
		List<Contact> contact_list = new ArrayList<Contact>();
		Contact tmp_Contact = null;
		// 1.查询raw_contact表获取联系人id
		ContentResolver resolver = context.getContentResolver();
		// 获取raw_contacts表对应的uri
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri data_uri = Uri.parse("content://com.android.contacts/data");
		Cursor cursor = resolver.query(uri, null, null, null, null);
		while (cursor.moveToNext())
		{
			String contact_id = cursor.getString(cursor .getColumnIndex("contact_id"));
			if (contact_id != null)
			{
				tmp_Contact = new Contact();
				Cursor data_cursor = resolver.query(data_uri, null, "raw_contact_id=?", new String[] { contact_id }, null);
				while (data_cursor.moveToNext())
				{
					String data1 = data_cursor.getString(data_cursor .getColumnIndex("data1"));
					String mimetype = data_cursor.getString(data_cursor .getColumnIndex("mimetype"));
					if (mimetype.equals(Contact.MimeType_Email))
					{
						tmp_Contact.setEmail(data1);
					} else if (mimetype.equals(Contact.MimeType_Name))
					{
						tmp_Contact.setName(data1);
					} else if (mimetype.equals(Contact.MimeType_Number))
					{
						tmp_Contact.setNumber(data1);
					}
					/**
					 * 这个是根据后面的mimetype来决定data1是什么类型的。、
					 */
				}
				contact_list.add(tmp_Contact);
				data_cursor.close();
			}
		}
		return contact_list;
	}
	
	/**
	 * 向系统插入联系人
	 * @param context 上下文
	 * @param contacts 一个或多个联系人，或联系人数组
	 * @return 		插入联系人成功返回true 否则返回false
	 */
	public static boolean insertContacts(Context context,Contact... contacts)
	{
		try
		{
			if (contacts != null)
			{
				ContentResolver resolver = context.getContentResolver();
				Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
				Uri data_uri = Uri.parse("content://com.android.contacts/data");
				ContentValues contact_id_values = null;
				ContentValues phoneValues = null;
				ContentValues emailValues = null;
				ContentValues nameValues = null;
				for (Contact contact : contacts)
				{
					// 必须知道最后一条联系人ID是多少
					Cursor cursor = resolver.query(uri, new String[] { "_id" }, null, null, null);
					cursor.moveToLast();
					int last_id = cursor.getInt(cursor.getColumnIndex("_id"));
					int new_id = ++last_id;
	
					// 1.向raw_contact表里添加联系人id
					contact_id_values = new ContentValues();
					contact_id_values.put("contact_id", new_id);
					resolver.insert(uri, contact_id_values);
	
					// 2.根据刚才的ID向data表里面添加数据
					phoneValues = new ContentValues();
					phoneValues.put("data1", contact.getNumber());
					phoneValues.put("mimetype", Contact.MimeType_Number);
					phoneValues.put("raw_contact_id", new_id);
					resolver.insert(data_uri, phoneValues);
	
					emailValues = new ContentValues();
					emailValues.put("data1", contact.getEmail());
					emailValues.put("mimetype", Contact.MimeType_Email);
					emailValues.put("raw_contact_id", new_id);
					resolver.insert(data_uri, emailValues);
	
					nameValues = new ContentValues();
					nameValues.put("data1", contact.getName());
					nameValues.put("mimetype", Contact.MimeType_Name);
					nameValues.put("raw_contact_id", new_id);
					resolver.insert(data_uri, nameValues);
				}
				/**
				 * 清理内存阶段
				 */
				contact_id_values = null;
				phoneValues = null;
				emailValues = null;
				nameValues = null;
				return true;
			}else 
			{
				return false;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
}
