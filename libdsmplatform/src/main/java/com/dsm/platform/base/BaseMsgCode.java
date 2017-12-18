package com.dsm.platform.base;

import android.util.Log;
import android.util.SparseArray;

import com.dsm.platform.DsmLibrary;
import com.dsm.platform.R;

/**
 * 作者：dccjll<br>
 * 创建时间：2017/11/10 09 53 星期五<br>
 * 功能描述：<br>bong手环蓝牙栈消息码
 */

public class BaseMsgCode {

    public static final SparseArray<String> codeMap = new SparseArray<>();

    static {
        String[] lockMsgCodeArr = DsmLibrary.application.getResources().getStringArray(R.array.baseMsgCode);
        try {
            for (String lockMsgCode : lockMsgCodeArr) {
                String[] arr = lockMsgCode.split("#");
                codeMap.put(Integer.parseInt(arr[0]), arr[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 转换消息码
     */
    public static String parseBLECodeMessage(int bleCode) {
        String originBleMsg = null;
        try {
            String bleString = codeMap.get(bleCode);
            String[] bleStringArr = bleString.split("\\|");
            originBleMsg = bleStringArr[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return originBleMsg;
    }

    /**
     * 获取消息码对应的描述信息等级
     */
    public static int getBLECodeMessageLevel(int bleCode) {
        int bleLevel = Log.INFO;
        try {
            String bleLogLevelString = getBLECodeMessageLevelMessage(bleCode);
            if ("VERBOSE".equalsIgnoreCase(bleLogLevelString)) {
                bleLevel = Log.VERBOSE;
            } else if ("DEBUG".equalsIgnoreCase(bleLogLevelString)) {
                bleLevel = Log.DEBUG;
            } else if ("INFO".equalsIgnoreCase(bleLogLevelString)) {
                bleLevel = Log.INFO;
            } else if ("WARN".equalsIgnoreCase(bleLogLevelString)) {
                bleLevel = Log.WARN;
            } else if ("ERROR".equalsIgnoreCase(bleLogLevelString)) {
                bleLevel = Log.ERROR;
            } else if ("ASSERT".equalsIgnoreCase(bleLogLevelString)) {
                bleLevel = Log.ASSERT;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bleLevel;
    }

    /**
     * 获取消息码对应的描述信息等级
     */
    public static String getBLECodeMessageLevelMessage(int bleCode) {
        String bleLevelMessage = "INFO";
        try {
            String bleString = codeMap.get(bleCode);
            String[] bleStringArr = bleString.split("\\|");
            bleLevelMessage = bleStringArr[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bleLevelMessage;
    }

    /**
     * 判断消息码表示的消息是否是前台用户需要的消息
     */
    public static boolean logForUser(int msgCode) {
        return getBLECodeMessageLevel(msgCode) <= Log.INFO;
    }

    /**
     * 判断消息码表示的消息是否是系统的消息
     */
    public static boolean logForSystem(int msgCode) {
        return getBLECodeMessageLevel(msgCode) > Log.INFO;
    }

    /**
     * 获取消息码表示的消息，如果消息码为非用户消息，则显示为替换消息
     */
    public static String getMessage(int msgCode, String replaceMsg) {
        if (logForUser(msgCode)) {
            return parseBLECodeMessage(msgCode);
        }
        return replaceMsg;
    }
}
