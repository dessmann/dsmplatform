package com.dsm.platform.util.contact;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import com.dsm.platform.util.PinYinUtil;
import com.dsm.platform.util.RegexUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by yanfa on 2016/7/18.
 * 获取手机联系人
 */
public class ContactUtil {
    private static final String[] PHONES_PROJECTION = {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };
    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**
     * 得到所有的联系人信息
     **/
    public static List<PhoneContact> getAllPhoneContacts(Context context) {
        List<PhoneContact> phoneContactList = getPhoneContact(context);
        List<PhoneContact> simContactList = getSIMContacts(context);
        for (PhoneContact contact : simContactList) {
            if (!isContainObject(phoneContactList, contact)) {
                phoneContactList.add(contact);
            }
        }
        return phoneContactList;
    }

    /*
     * 某个联系人是否已经存在于联系人列表中
    */
    private static boolean isContainObject(List<PhoneContact> phoneContactList, PhoneContact contact) {
        for (int i = 0; i < phoneContactList.size(); i++) {
            PhoneContact curContact = phoneContactList.get(i);
            if (curContact.getMobile().equals(contact.getMobile()) && curContact.getName().equals(contact.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取手机通讯录联系人
     *
     * @param context
     * @return
     */
    private static List<PhoneContact> getPhoneContact(Context context) {
        List<PhoneContact> list = new ArrayList<>();
        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri contentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            Cursor cursor = contentResolver.query(contentUri, PHONES_PROJECTION, null, null, null);

            while (cursor != null && cursor.moveToNext()) {
                String name = cursor.getString(0).replaceAll("'", "’").replaceAll("\"", "”");
                String mobile = cursor.getString(1).replace("+86", "").replace(" ", "").replace("-", "");
                String namePinYin = PinYinUtil.getPinYin(name).toUpperCase(Locale.US);
                PhoneContact contactInfo = new PhoneContact(name, mobile, namePinYin);
                if (RegexUtil.regexMobile(contactInfo.getMobile())) {
                    list.add(contactInfo);
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 得到手机SIM卡联系人人信息
     **/
    private static List<PhoneContact> getSIMContacts(Context context) {
        List<PhoneContact> list = new ArrayList<>();
        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = Uri.parse("content://icc/adn");
            Cursor cursor = contentResolver.query(uri, PHONES_PROJECTION, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                String name = cursor.getString(PHONES_DISPLAY_NAME_INDEX).replaceAll("'", "’").replaceAll("\"", "”");
                String mobile = cursor.getString(PHONES_NUMBER_INDEX).replace("+86", "").replace(" ", "").replace("-", "");
                String namePinYin = PinYinUtil.getPinYin(name).toUpperCase(Locale.US);
                PhoneContact contactInfo = new PhoneContact(name, mobile, namePinYin);
                if (RegexUtil.regexMobile(contactInfo.getMobile())) {
                    list.add(contactInfo);
                }
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    public static String getContactName(Context context, String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return null;
        }
        ContentResolver cr = context.getContentResolver();
        //通过电话号码查找联系人
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return TextUtils.isEmpty(contactName) ? phoneNumber : contactName;
    }
}
