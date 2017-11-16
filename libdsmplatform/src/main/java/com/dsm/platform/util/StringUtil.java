package com.dsm.platform.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by dessmann on 16/7/11.
 * 简单的字符串工具
 */
public class StringUtil {

    /**
     * 将一个用":"连接的字符串以":"为分隔反转
     * @param st    原字符串
     * @param toUpperCase   强制转换为大写
     * @return  转换结果
     */
    public static String reserveString(String st, boolean toUpperCase) {
        String[] starr = st.split(":");
        StringBuilder sBuffer = new StringBuilder();
        for (int i = starr.length - 1; i >= 0; i--) {
            sBuffer.append(starr[i]).append(":");
        }
        String reserveString = sBuffer.toString().substring(0, sBuffer.toString().lastIndexOf(":"));
        if(toUpperCase){
            reserveString = reserveString.toUpperCase(Locale.US);
        }
        return reserveString;
    }

    /**
     * 新建一个可以添加属性的文本对象
     * @param hintText  文本
     * @param hintTextSize  文本字体大小
     * @return  文本对象
     */
    public static SpannableString buildSpannableString(String hintText, int hintTextSize){
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString(hintText);
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(hintTextSize,true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 构建字符串请求的数据
     *
     * @param keys   属性
     * @param values 数据
     */
    public static Map<String, String> buildStringData(String[] keys, String[] values) {
        Map<String, String> data = new HashMap<>();
        if (keys == null || values == null || keys.length != values.length) {
            return null;
        }
        for (int index = 0; index < keys.length; index++) {
            data.put(keys[index], values[index]);
        }
        return data;
    }

    /**
     * 判断一个字符串是纯数字
     */
    public static boolean isNumeric(String str){
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

//    public static String[] parseWeekArr(String[] weekArr) {
//        if (weekArr == null) {
//            return null;
//        }
//        String[] parsedWeekArr = new String[weekArr.length];
//        for (int i = 0; i < weekArr.length; i++) {
//
//            if (weekArr[i].equalsIgnoreCase("7")) {
//                parsedWeekArr[i] = "1";
//            } else {
//
//            }
//        }
//        return parsedWeekArr;
//    }
}
