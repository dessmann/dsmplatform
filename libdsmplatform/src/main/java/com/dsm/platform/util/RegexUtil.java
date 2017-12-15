package com.dsm.platform.util;

import android.text.TextUtils;

import com.dsm.platform.base.BaseMsgCode;
import com.dsm.platform.util.log.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dessmann on 16/7/12.
 */
public class RegexUtil {
    private static final String tag = RegexUtil.class.getSimpleName();

    // 正则通用验证
    private static boolean regexCheck(String res, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(res);
        return matcher.matches();
    }

    // 正则验证 x到y个任意字符
    public static boolean regex_X_Y_any_Chars(String res, int x, int y) {
        // if(regexCheck(res,
        // "^[A-Za-z_]+[A-Za-z0-9_]+$")){//只包含字母、数字、下划线，并且只能以字母或者下划线开头
        // return true;
        // }
        return !(x < 0 || y < 0 || x >= y) && regexCheck(res, "^.{" + x + "," + y + "}$");
    }

    // 正则验证 x到y个中文字符
    public static boolean regex_X_Y_chinese_Chars(String res, int x, int y) {
        return !(x < 0 || y < 0 || x >= y) && regexCheck(res, "^[\u4E00-\u9FA5]{" + x + "," + y + "}$");
    }

    // 正则验证 x到y个阿拉伯数字
    public static boolean regex_X_Y_numbers_Chars(String res, int x, int y) {
        return !(x < 0 || y < 0 || x >= y) && regexCheck(res, "^[0-9]{" + x + "," + y + "}$");
    }

    // 验证字符串res是否是x位的不是空白符的字符串
    public static boolean regex_X_Not_Empty_Chars(String res, int x) {
        return x > 0 && regexCheck(res, "^(\\S){" + x + "}$");
    }

    // 正则验证 11位数字手机号码
    public static boolean regexMobile(String res) {
        // if(regexCheck(res, "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$")){
        // return true;
        // }
        // return false;

        //客户端只验证长度
        return regexCheck(res, "^[0-9]{11}$");
        //		if (regexCheck(res, "^[1]([3]|[4]|[5]|[7]|[8])[0-9]{9}$")) {
//			return true;
//		}
//		return false;
    }

    // 身份证验证
    public static boolean regexIDCard(String res) {
        return regexCheck(res, "^\\d{15}|(\\d{17}([0-9]|X|x))$");
    }

    //X位纯数字
    public static boolean regexXNumbers(String res, int x) {
        return regexCheck(res, "^[0-9]{" + x + "}$");
    }

    /**
     * @param str
     * @param minLength 最短长度
     * @param maxLength 最长长度
     * @return
     */
    public static boolean regexChars(String str, int minLength, int maxLength) {
        return regexCheck(str, "^.{" + minLength + "," + maxLength + "}$");
    }

    /**
     * @param str
     * @param minLength 最短长度
     * @param maxLength 最长长度
     * @return
     */
    public static boolean regexChinese(String str, int minLength, int maxLength) {
        return regexCheck(str, "^[\u4E00-\u9FA5]{" + minLength + "," + maxLength + "}$");
    }

    /**
     * 通过正则表达式来判断。只允许显示字母、数字和汉字。
     *
     * @param str
     * @return
     */
    public static String regexSpecialChar(String str) {
        // 只允许字母、数字和汉字
        String reg = "[^a-zA-Z0-9\u4E00-\u9FA5.]";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str.replaceAll(" ", ""));
        return matcher.replaceAll(" ").trim();
    }

    /**
     * 是否是普通数字密码
     *
     * @param source
     * @return
     */
    public static boolean isSimpleNumber(String source) {
        return regexContinueString(source, source.length()) || regex6AscDesc(source);
    }

    //匹配连号
    private static boolean regexContinueString(String source, int continueSize) {
        if (TextUtils.isEmpty(source) || continueSize <= 1) {
            LogUtil.e(tag, "字符串为空或者连号标志小于1");
            return false;
        }
        Pattern pattern = Pattern.compile("([\\d])\\1{" + (continueSize-1) + "}");
        Matcher matcher = pattern.matcher(source);
        if (matcher.matches()) {
            LogUtil.e(tag, source + "是" + continueSize + "位连号");
            return true;
        }
        return false;
    }

    //匹配6位顺增或顺降
    private static boolean regex6AscDesc(String source) {
        if (TextUtils.isEmpty(source)) {
            LogUtil.e(tag, "字符串为空");
            return false;
        }
        Pattern pattern = Pattern.compile("(?:(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){5}|(?:9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){5})\\d");
        Matcher matcher = pattern.matcher(source);
        if (matcher.matches()) {
            LogUtil.e(tag, source + "是6位顺增或顺降");
            return true;
        }
        return false;
    }

    public static class CheckResult {
        private boolean result;
        private String msg;

        CheckResult(boolean result, String msg) {
            this.result = result;
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }
    }

    // 身份证号合法性验证
    // 支持15位和18位身份证号
    // 支持地址编码、出生日期、校验位验证
    public static CheckResult checkCardId(String cardId) {
        String city = "{\"11\":\"北京\",\"12\":\"天津\",\"13\":\"河北\",\"14\":\"山西\",\"15\":\"内蒙古\"," +
                "\"21\":\"辽宁\",\"22\":\"吉林\",\"23\":\"黑龙江 \",\"31\":\"上海\",\"32\":\"江苏\",\"33\":\"浙江\"," +
                "\"34\":\"安徽\",\"35\":\"福建\",\"36\":\"江西\",\"37\":\"山东\",\"41\":\"河南\",\"42\":\"湖北 \"," +
                "\"43\":\"湖南\",\"44\":\"广东\",\"45\":\"广西\",\"46\":\"海南\",\"50\":\"重庆\",\"51\":\"四川\"," +
                "\"52\":\"贵州\",\"53\":\"云南\",\"54\":\"西藏 \",\"61\":\"陕西\",\"62\":\"甘肃\",\"63\":\"青海\"," +
                "\"64\":\"宁夏\",\"65\":\"新疆\",\"71\":\"台湾\",\"81\":\"香港\",\"82\":\"澳门\",\"91\":\"国外\"}";

        String regex_15 = "^\\d{6}\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}$";
        String regex_18 = "^\\d{6}(18|19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|X)$";

        if (TextUtils.isEmpty(cardId)) {
            return new CheckResult(false, BaseMsgCode.parseBLECodeMessage(-60028));
        }

        if (!cardId.matches(regex_15) && !cardId.matches(regex_18)) {
            return new CheckResult(false, BaseMsgCode.parseBLECodeMessage(-60029));
        }

        String place = "";
        try {
            JSONObject jsonObject = new JSONObject(city);
            place = jsonObject.getString(cardId.substring(0, 2));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(place)) {
            return new CheckResult(false, BaseMsgCode.parseBLECodeMessage(-60030));
        }

        if (cardId.length() == 18) {
            char[] singleCodes = cardId.toCharArray();
            int[] factor = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
            int[] parity = {1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2};
            int sum = 0;

            for (int i = 0; i < 17; i++) {
                sum += (singleCodes[i] - 48) * factor[i];
            }

            if (parity[sum % 11] != (singleCodes[17] - 48)) {
                return new CheckResult(false, BaseMsgCode.parseBLECodeMessage(-60031));
            }
        }

        return new CheckResult(true, BaseMsgCode.parseBLECodeMessage(60001));
    }

    // 隐藏手机号码前7位
    public static String protectPhoneNum(String phoneNum) {
        return phoneNum.replaceAll("\\d{7}(\\d{4})", "*******$1");
    }

    // 隐藏手机号码中间4位
    public static String protectPhoneNum2(String phoneNum) {
        return phoneNum.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    // 隐藏身份证号码
    public static String protectCardNum(String cardNum) {
        if (cardNum.length() == 18) {
            return cardNum.replaceAll("\\d{14}(\\w{4})", "**************$1");
        } else {
            return cardNum.replaceAll("\\d{11}(\\w{4})", "***********$1");
        }
    }

    // 校验MAC地址是否有效
    public static boolean checkMacAddr(String macAddr) {
        String regex = "(?i)[a-f\\d]{2}([:-]?)[a-f\\d]{2}(\\1[a-f\\d]{2}){4}";
        return macAddr.matches(regex);
    }

    // 将MAc地址转换成指定格式
    public static String formatMacAddr(String macAddr, String separator) {
        if (!macAddr.matches("(?i)[a-f\\d]{2}([:-]?)[a-f\\d]{2}(\\1[a-f\\d]{2}){4}")) {
            System.out.println("无效Mac地址");
            return null;
        }

        if (!separator.matches("[:-]")) {
            System.out.println("Parameter separate must be \":\" or \"-\"");
            return null;
        }

        return macAddr.replaceAll("(?i)([a-f\\d]{2})[:-]?(?!$)", "$1" + separator).toUpperCase(Locale.US);
    }
}
