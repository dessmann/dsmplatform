package com.dsm.platform.util;

import android.text.TextUtils;
import android.util.Log;

import com.dsm.platform.util.log.LogUtil;

import net.tsz.afinal.FinalDb;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by dccjll on 2017/5/9.
 * 应用内基本实用工具
 */

public class BaseUtil {
    private static final String TAG = "BaseUtil";

    /**
     * 只显示警告与信息级别的非空日志信息，否则，显示自定义的日志信息
     */
    public static String getMsgByLoglevel(String error, int loglevel, String diyMsg) {
        if ((loglevel == Log.WARN || loglevel == Log.INFO) && !TextUtils.isEmpty(error)) {
            return error;
        }
        return diyMsg;
    }

    /**
     * 验证设备信道密码
     */
    public static boolean checkChannelPwd(String channelpwd) {
        return !(TextUtils.isEmpty(channelpwd) || channelpwd.length() != 8);
    }

    /**
     * 计算设备信道密码字节数组
     *
     * @param channelpwd 设备信道密码
     * @return 计算结果
     */
    public static byte[] getChannelPwdBytes(String channelpwd) {
        byte[] channelpwdbytes = new byte[4];
        if (TextUtils.isEmpty(channelpwd) || channelpwd.length() != 8) {
            return null;
        }
        String[] lockchannelarray = new String[channelpwdbytes.length];
        for (int i = 0; i < lockchannelarray.length; i++) {
            lockchannelarray[i] = channelpwd.substring(i * 2, i * 2 + 2);
            channelpwdbytes[i] = (byte) Integer.parseInt(lockchannelarray[i], 16);
        }
        return channelpwdbytes;
    }

    /**
     * 转换13个字节的秘钥
     *
     * @param mobile 当前用户手机号码
     * @return 13个字节的秘钥
     */
    public static byte[] parse13Secretkeys(String mobile) {
        StringBuilder stringBuffer = new StringBuilder();
        if (TextUtils.isEmpty(mobile) || mobile.length() != 11) {
            LogUtil.e(TAG, "手机号码为空或长度不正确");
            return null;
        }
        stringBuffer.append(new SimpleDateFormat(("yyyyMMddHHmmss"), Locale.US).format(new Date())).append(mobile.substring(0, 1)).append(mobile);
        LogUtil.e(TAG, "13个字节秘钥=" + stringBuffer.toString());
        return ByteUtil.parseRadixStringToBytes(stringBuffer.toString(), 16);
    }

    /**
     * 验证开门秘钥
     */
    public static boolean checkOpenSecretKey(byte[] opensecretkey) {
        return !(opensecretkey == null || opensecretkey.length != 13);
    }

    /**
     * 验证手机号码
     */
    public static boolean checkMobile(String mobile) {
        return mobile != null && mobile.length() == 11;
    }



    /**
     * 获取数据
     *
     * @param clazz    类
     * @param strWhere where条件
     * @param orderBy  排序
     * @param <T>
     * @return
     */
    public static <T> T getDataFromDB(FinalDb finalDb,Class<T> clazz, String strWhere, String orderBy) {
        List<T> list = getDataListFromDB(finalDb,clazz, strWhere, orderBy);
        if (list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }

    /**
     * 获取列表数据
     *
     * @param clazz    类
     * @param strWhere where条件
     * @param orderBy  排序
     * @param <T>
     * @return
     */
    public static <T> List<T> getDataListFromDB(FinalDb finalDb,Class<T> clazz, String strWhere, String orderBy) {
        List<T> list;
        if (TextUtils.isEmpty(orderBy)) {
            list = finalDb.findAllByWhere(clazz, strWhere);
        } else {
            list = finalDb.findAllByWhere(clazz, strWhere, orderBy);
        }
        if (list == null) {
            list = new CopyOnWriteArrayList<>();
        }
        return list;
    }
}
