package com.dsm.platform.base;

import android.text.TextUtils;

import com.dsm.platform.DsmLibrary;
import com.dsm.platform.util.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取锁具基础数据
 */
public class DeviceLockBaseUtil {

    private static final String TAG = "DeviceLockBaseUtil";
    private static List<DeviceLockBase> deviceLockBaseList = new ArrayList<>();

    /**
     * 获取锁具基础数据
     * 1、meterType为空，meterType='' and appVersion=''
     * 2、appVersion为空，appVersion=''
     * 3、都不为空，没有匹配到数据，获取2
     * 4、都不为空，匹配到多条数据，返回null
     */
    public static DeviceLockBase getDeviceLock(String deviceType, String meterType, String softwareVersion) {
        LogUtil.i(TAG, "请求获取配置，deviceType=" + deviceType + ",meterType=" + meterType + ",softwareVersion=" + softwareVersion);
        DeviceLockBase deviceLockBase = null;
        if (!TextUtils.isEmpty(deviceType)) {
            deviceType = deviceType.toUpperCase();
            if (TextUtils.isEmpty(meterType)) {
                //锁具型号为空，根据设备类型取数据
                meterType = "";
                softwareVersion = "";
            } else {
                meterType = meterType.toUpperCase();
                if (TextUtils.isEmpty(softwareVersion)) {
                    //固件版本号为空，根据设备类型和设备型号取数据
                    softwareVersion = "";
                } else {
                    softwareVersion = softwareVersion.toUpperCase();
                }
            }
            if ("temp".equalsIgnoreCase(deviceType)) {
                deviceType = "lock";
            }
            //先在内存查找
            deviceLockBase = getDeviceLockBaseOnRaw(deviceType, meterType, softwareVersion);
            if (deviceLockBase == null) {//内存中没找到，继续在本地数据库查找
                deviceLockBase = getDeviceLockBaseOnSqlite(deviceType, meterType, softwareVersion);
            }
        }
        return deviceLockBase;
    }

    /**
     * 内存查找设备配置信息
     */
    private static DeviceLockBase getDeviceLockBaseOnRaw(String deviceType, String meterType, String softwareVersion) {
        DeviceLockBase deviceLockBase = null;
        for (DeviceLockBase deviceLockBase_ :
                deviceLockBaseList) {
            if ((((deviceLockBase_.getDevType() + "").equalsIgnoreCase(deviceType)) || ((deviceLockBase_.getDevTypeCode() + "").equalsIgnoreCase(deviceType)))
                    && deviceLockBase_.getMeterType().equalsIgnoreCase(meterType)
                    && (TextUtils.isEmpty(softwareVersion) ? TextUtils.isEmpty(deviceLockBase_.getAppVersion()) : softwareVersion.length() > 8 ? deviceLockBase_.getAppVersion().contains(softwareVersion.substring(softwareVersion.length() -8)) : deviceLockBase_.getAppVersion().contains(softwareVersion))) {
                deviceLockBase = deviceLockBase_;
                break;
            }
        }
        if (deviceLockBase != null && !TextUtils.isEmpty(deviceType) && !TextUtils.isEmpty(meterType) && !TextUtils.isEmpty(softwareVersion)) {
            LogUtil.i(TAG, "内存中设备配置信息三个字段匹配成功,deviceLockBase=" + deviceLockBase);
        }
        if (deviceLockBase == null) {
            if (!TextUtils.isEmpty(softwareVersion)) {//内存中三个字段匹配无数据时按照两个字段的匹配
                deviceLockBase = getDeviceLockBaseOnRaw(deviceType, meterType, null);
                if (deviceLockBase != null) {
                    LogUtil.i(TAG, "内存中设备配置信息二个字段匹配成功,deviceLockBase=" + deviceLockBase);
                }
            } else {
                if (!TextUtils.isEmpty(meterType)) {//内存中两个字段匹配无数据时按照一个字段的匹配
                    deviceLockBase = getDeviceLockBaseOnRaw(deviceType, null, null);
                    if (deviceLockBase != null) {
                        LogUtil.i(TAG, "内存中设备配置信息一个字段匹配成功,deviceLockBase=" + deviceLockBase);
                    }
                }
            }
        }
        return deviceLockBase;
    }

    /**
     * 本地数据库查找设备配置信息
     */
    private static DeviceLockBase getDeviceLockBaseOnSqlite(String deviceType, String meterType, String softwareVersion) {
        DeviceLockBase deviceLockBase = null;
        String where = String.format("(devType='%s' or devTypeCode='%s') and meterType='%s' and %s",
                deviceType, deviceType, meterType, TextUtils.isEmpty(softwareVersion) ? "appVersion=''" : String.format("appVersion like '%%%s%%'", softwareVersion.length() > 8 ? softwareVersion.substring(softwareVersion.length() - 8, softwareVersion.length()) : softwareVersion));
        List<DeviceLockBase> list = DsmLibrary.getInstance().getFinalDb().findAllByWhere(DeviceLockBase.class, where);
        if (list == null || list.size() == 0) {
            if (!TextUtils.isEmpty(softwareVersion)) {
                //三个字段匹配无数据时按照两个字段的匹配
                deviceLockBase = getDeviceLockBaseOnSqlite(deviceType, meterType, null);
                if (deviceLockBase != null) {
                    LogUtil.i(TAG, "数据库中设备配置信息二个字段匹配成功,deviceLockBase=" + deviceLockBase);
                }
                if (deviceLockBase != null && !deviceLockBaseList.contains(deviceLockBase)) {
                    deviceLockBaseList.add(deviceLockBase);
                }
            } else {
                //两个字段匹配无数据时按照一个字段的匹配
                if (!TextUtils.isEmpty(meterType)) {
                    deviceLockBase = getDeviceLockBaseOnSqlite(deviceType, null, null);
                    if (deviceLockBase != null) {
                        LogUtil.i(TAG, "数据库中设备配置信息一个字段匹配成功,deviceLockBase=" + deviceLockBase);
                    }
                    if (deviceLockBase != null && !deviceLockBaseList.contains(deviceLockBase)) {
                        deviceLockBaseList.add(deviceLockBase);
                    }
                }
            }
        } else {
            deviceLockBase = list.get(0);
            if (deviceLockBase != null && !TextUtils.isEmpty(deviceType) && !TextUtils.isEmpty(meterType) && !TextUtils.isEmpty(softwareVersion)) {
                LogUtil.i(TAG, "数据库中设备配置信息三个字段匹配成功,deviceLockBase=" + deviceLockBase);
            }
            if (deviceLockBase != null && !deviceLockBaseList.contains(deviceLockBase)) {
                deviceLockBaseList.add(deviceLockBase);
            }
        }
        return deviceLockBase;
    }

    /**
     * 是否支持gemoto加密
     * 默认不支持
     */
    public static boolean supportGematoCipher(String deviceType, String meterType, String softwareVersion) {
        return !TextUtils.isEmpty(meterType) && supportGematoCipher(getDeviceLock(deviceType, meterType, softwareVersion));
    }

    /**
     * 是否支持gemoto加密
     * 默认不支持
     */
    public static boolean supportGematoCipher(DeviceLockBase deviceLockBase) {
        return deviceLockBase != null && "1".equalsIgnoreCase(deviceLockBase.getCipherType() + "");
    }

    /**
     * 是否支持德施曼密码加密
     * 默认不支持
     */
    public static boolean supportDsmPwdCipher(String deviceType, String meterType, String softwareVersion) {
        return !(TextUtils.isEmpty(meterType) || TextUtils.isEmpty(softwareVersion)) && supportDsmPwdCipher(getDeviceLock(deviceType, meterType, softwareVersion));
    }

    /**
     * 是否支持德施曼密码加密
     * 默认不支持
     */
    private static boolean supportDsmPwdCipher(DeviceLockBase deviceLockBase) {
        return deviceLockBase != null && "1".equalsIgnoreCase(deviceLockBase.getEncryptFlag() + "");
    }

    /**
     * 是否有用户模块
     * 默认有
     */
    public static boolean hasUserModule(String deviceType, String meterType, String softwareVersion) {
        return TextUtils.isEmpty(meterType) || TextUtils.isEmpty(softwareVersion) || hasUserModule(getDeviceLock(deviceType, meterType, softwareVersion));
    }

    /**
     * 是否有用户模块
     * 默认有
     */
    public static boolean hasUserModule(DeviceLockBase deviceLockBase) {
        return deviceLockBase == null || "1".equalsIgnoreCase(deviceLockBase.getUserFlag() + "");
    }

    /**
     * 获取设备用户容量
     * 默认为{LockSDKLibrary.DefaultDeviceUserCapacity}
     */
    public static int getDeviceUserCapacity(String deviceType, String meterType, String softwareVersion) {
        if (TextUtils.isEmpty(meterType) || TextUtils.isEmpty(softwareVersion)) {
            return DsmLibrary.DefaultDeviceUserCapacity;
        }
        return getDeviceUserCapacity(getDeviceLock(deviceType, meterType, softwareVersion));
    }

    /**
     * 获取设备用户容量
     * 默认为{LockSDKLibrary.DefaultDeviceUserCapacity}
     */
    private static int getDeviceUserCapacity(DeviceLockBase deviceLockBase) {
        if (deviceLockBase == null || deviceLockBase.getUserCapacity() == null || deviceLockBase.getUserCapacity() <= 0) {
            return DsmLibrary.DefaultDeviceUserCapacity;
        }
        return deviceLockBase.getUserCapacity();
    }

    /**
     * 是否有指纹模块
     * 默认有
     */
    public static boolean hasFingerModule(String deviceType, String meterType, String softwareVersion) {
        return TextUtils.isEmpty(meterType) || TextUtils.isEmpty(softwareVersion) || hasFingerModule(getDeviceLock(deviceType, meterType, softwareVersion));
    }

    /**
     * 是否有指纹模块
     * 默认有
     */
    public static boolean hasFingerModule(DeviceLockBase deviceLockBase) {
        return deviceLockBase == null || "1".equalsIgnoreCase(deviceLockBase.getFingerFlag() + "");
    }

    /**
     * 获取设备指纹容量
     * 默认为{LockSDKLibrary.DefaultDeviceFingerCapacity}
     */
    public static int getDeviceFingerCapacity(String deviceType, String meterType, String softwareVersion) {
        if (TextUtils.isEmpty(meterType) || TextUtils.isEmpty(softwareVersion)) {
            return DsmLibrary.DefaultDeviceFingerCapacity;
        }
        return getDeviceFingerCapacity(getDeviceLock(deviceType, meterType, softwareVersion));
    }

    /**
     * 获取设备指纹容量
     * 默认为{LockSDKLibrary.DefaultDeviceFingerCapacity}
     */
    private static int getDeviceFingerCapacity(DeviceLockBase deviceLockBase) {
        if (deviceLockBase == null || deviceLockBase.getFingerCapacity() == null || deviceLockBase.getFingerCapacity() <= 0) {
            return DsmLibrary.DefaultDeviceFingerCapacity;
        }
        return deviceLockBase.getFingerCapacity();
    }

    /**
     * 是否支持野指纹
     * 默认不支持
     */
    public static boolean supportWildFinger(String deviceType, String meterType, String softwareVersion) {
        return !(TextUtils.isEmpty(meterType) || TextUtils.isEmpty(softwareVersion)) && supportWildFinger(getDeviceLock(deviceType, meterType, softwareVersion));
    }

    /**
     * 是否支持野指纹
     * 默认不支持
     */
    private static boolean supportWildFinger(DeviceLockBase deviceLockBase) {
        return deviceLockBase != null && "1".equalsIgnoreCase(deviceLockBase.getLocalFingerFlag() + "");
    }

    /**
     * 获取设备本地指纹容量
     * 默认为{LockSDKLibrary.DefaultDeviceLocalFingerCapacity}
     */
    public static int getDeviceLocalFingerCapacity(String deviceType, String meterType, String softwareVersion) {
        if (TextUtils.isEmpty(meterType) || TextUtils.isEmpty(softwareVersion)) {
            return DsmLibrary.DefaultDeviceLocalFingerCapacity;
        }
        return getDeviceLocalFingerCapacity(getDeviceLock(deviceType, meterType, softwareVersion));
    }

    /**
     * 获取设备本地指纹容量
     * 默认为{LockSDKLibrary.DefaultDeviceLocalFingerCapacity}
     */
    private static int getDeviceLocalFingerCapacity(DeviceLockBase deviceLockBase) {
        if (deviceLockBase == null || deviceLockBase.getLocalFingerCapacity() == null || deviceLockBase.getLocalFingerCapacity() <= 0) {
            return DsmLibrary.DefaultDeviceLocalFingerCapacity;
        }
        return deviceLockBase.getLocalFingerCapacity();
    }

    /**
     * 是否支持录指纹确认协议
     * 默认不支持
     */
    public static boolean supportFingerComfirmProtocol(String deviceType, String meterType, String softwareVersion) {
        return !(TextUtils.isEmpty(meterType) || TextUtils.isEmpty(softwareVersion)) && supportFingerComfirmProtocol(getDeviceLock(deviceType, meterType, softwareVersion));
    }

    /**
     * 是否支持录指纹确认协议
     * 默认不支持
     */
    private static boolean supportFingerComfirmProtocol(DeviceLockBase deviceLockBase) {
        return deviceLockBase != null && "1".equalsIgnoreCase(deviceLockBase.getFingerConfirmFlag() + "");
    }

    /**
     * 是否有智能钥匙
     * 默认不支持
     */
    public static boolean hasSmartKeyModule(String deviceType, String meterType, String softwareVersion) {
        return !(TextUtils.isEmpty(meterType) || TextUtils.isEmpty(softwareVersion)) && hasSmartKeyModule(getDeviceLock(deviceType, meterType, softwareVersion));
    }

    /**
     * 是否有智能钥匙
     * 默认不支持
     */
    public static boolean hasSmartKeyModule(DeviceLockBase deviceLockBase) {
        return deviceLockBase != null && "1".equalsIgnoreCase(deviceLockBase.getSmartKeyFlag() + "");
    }

    /**
     * 是否有智能手环
     * 默认无
     */
    public static boolean hasBongModule(String deviceType, String meterType, String softwareVersion) {
        return !(TextUtils.isEmpty(meterType) || TextUtils.isEmpty(softwareVersion)) && hasBongModule(getDeviceLock(deviceType, meterType, softwareVersion));
    }

    /**
     * 是否有智能手环
     * 默认无
     */
    public static boolean hasBongModule(DeviceLockBase deviceLockBase) {
        return deviceLockBase != null && "1".equalsIgnoreCase(deviceLockBase.getBongFlag() + "");
    }

    /**
     * 是否支持锁具密码设置
     * 默认支持
     */
    public static boolean hasPasswordModule(String deviceType, String meterType, String softwareVersion) {
        return TextUtils.isEmpty(meterType) || TextUtils.isEmpty(softwareVersion) || hasPasswordModule(getDeviceLock(deviceType, meterType, softwareVersion));
    }

    /**
     * 是否支持锁具密码设置
     * 默认支持
     */
    public static boolean hasPasswordModule(DeviceLockBase deviceLockBase) {
        return deviceLockBase == null || "1".equalsIgnoreCase(deviceLockBase.getPasswordFlag() + "");
    }

    /**
     * 是否支持临时密码
     * 默认不支持
     */
    public static boolean hasTempPwdModule(String deviceType, String meterType, String softwareVersion) {
        return !(TextUtils.isEmpty(meterType) || TextUtils.isEmpty(softwareVersion)) && hasTempPwdModule(getDeviceLock(deviceType, meterType, softwareVersion));
    }

    /**
     * 是否支持临时密码
     * 默认不支持
     */
    public static boolean hasTempPwdModule(DeviceLockBase deviceLockBase) {
        return deviceLockBase != null && "1".equalsIgnoreCase(deviceLockBase.getTempPasswordFlag() + "");
    }

    /**
     * 是否支持临时钥匙
     * 默认支持
     */
    public static boolean hasTempKeyModule(String deviceType, String meterType, String softwareVersion) {
        return TextUtils.isEmpty(meterType) || TextUtils.isEmpty(softwareVersion) || hasTempKeyModule(getDeviceLock(deviceType, meterType, softwareVersion));
    }

    /**
     * 是否支持临时钥匙
     * 默认支持
     */
    public static boolean hasTempKeyModule(DeviceLockBase deviceLockBase) {
        return deviceLockBase == null || "1".equalsIgnoreCase(deviceLockBase.getTempKeyFlag() + "");
    }

    /**
     * 是否支持手机开门
     * 默认支持
     */
    public static boolean supportPhoneOpen(String deviceType, String meterType, String softwareVersion) {
        return TextUtils.isEmpty(meterType) || TextUtils.isEmpty(softwareVersion) || supportPhoneOpen(getDeviceLock(deviceType, meterType, softwareVersion));
    }

    /**
     * 是否支持手机开门
     * 默认支持
     */
    public static boolean supportPhoneOpen(DeviceLockBase deviceLockBase) {
        return deviceLockBase == null || "1".equalsIgnoreCase(deviceLockBase.getPhoneFlag() + "");
    }

    /**
     * 是否支持手机开门方式配置<br/>
     * 手机支持的开门方式配置(0.不用手机，1.手机密码 2.摇一摇 3.手势 4.点一点) 支持多种开门方式 则用,(半角逗号)分割  如 0,1,2<br/>
     * 默认支持
     */
    public static boolean supportPhoneOpenTypeConfig(DeviceLockBase deviceLockBase) {
        if (deviceLockBase == null || TextUtils.isEmpty(deviceLockBase.getPhoneOpenType())) {
            return true;
        }
        boolean support = true;
        String phoneOpenType = deviceLockBase.getPhoneOpenType();
        if (!(phoneOpenType.contains("0") || phoneOpenType.contains("1") || phoneOpenType.contains("2") || phoneOpenType.contains("3") || phoneOpenType.contains("4"))) {
            support = false;
        }
        return support;
    }

    /**
     * 是否支持手机密码开门方式配置<br/>
     * 手机支持的开门方式配置(0.不用手机，1.手机密码 2.摇一摇 3.手势 4.点一点) 支持多种开门方式 则用,(半角逗号)分割  如 0,1,2<br/>
     * 默认支持
     */
    public static boolean supportPhonePwdOpenTypeConfig(DeviceLockBase deviceLockBase) {
        if (deviceLockBase == null || TextUtils.isEmpty(deviceLockBase.getPhoneOpenType())) {
            return true;
        }
        boolean support = true;
        String phoneOpenType = deviceLockBase.getPhoneOpenType();
        if (!phoneOpenType.contains("1")) {
            support = false;
        }
        return support;
    }

    /**
     * 是否支持手机摇一摇开门方式配置<br/>
     * 手机支持的开门方式配置(0.不用手机，1.手机密码 2.摇一摇 3.手势 4.点一点) 支持多种开门方式 则用,(半角逗号)分割  如 0,1,2<br/>
     * 默认支持
     */
    public static boolean supportPhoneGestureOpenTypeConfig(DeviceLockBase deviceLockBase) {
        if (deviceLockBase == null || TextUtils.isEmpty(deviceLockBase.getPhoneOpenType())) {
            return true;
        }
        boolean support = true;
        String phoneOpenType = deviceLockBase.getPhoneOpenType();
        if (!phoneOpenType.contains("2")) {
            support = false;
        }
        return support;
    }

    /**
     * 是否支持开门记录上传
     * 默认不支持
     */
    public static boolean supportOpenRecordUpload(String deviceType, String meterType, String softwareVersion) {
        return !(TextUtils.isEmpty(meterType) || TextUtils.isEmpty(softwareVersion)) && supportOpenRecordUpload(getDeviceLock(deviceType, meterType, softwareVersion));
    }

    /**
     * 是否支持开门记录上传
     * 默认不支持
     */
    private static boolean supportOpenRecordUpload(DeviceLockBase deviceLockBase) {
        return deviceLockBase != null && "1".equalsIgnoreCase(deviceLockBase.getRecordUploadFlag() + "");
    }

    /**
     * 是否支持WIFI推送检测
     * 默认不支持
     */
    public static boolean supportWifiPushTest(String deviceType, String meterType, String softwareVersion) {
        return !(TextUtils.isEmpty(meterType) || TextUtils.isEmpty(softwareVersion)) && supportWifiPushTest(getDeviceLock(deviceType, meterType, softwareVersion));
    }

    /**
     * 是否支持WIFI推送检测
     * 默认不支持
     */
    private static boolean supportWifiPushTest(DeviceLockBase deviceLockBase) {
        return deviceLockBase != null && "1".equalsIgnoreCase(deviceLockBase.getWifiTestFlag() + "");
    }

    /**
     * 是否支持切换wifi开关状态
     * 默认不支持
     */
    public static boolean supportToggleWifi(String deviceType, String meterType, String softwareVersion) {
        return !(TextUtils.isEmpty(meterType) || TextUtils.isEmpty(softwareVersion)) && supportToggleWifi(getDeviceLock(deviceType, meterType, softwareVersion));
    }

    /**
     * 是否支持切换wifi开关状态
     * 默认不支持
     */
    private static boolean supportToggleWifi(DeviceLockBase deviceLockBase) {
        return deviceLockBase != null && "1".equalsIgnoreCase(deviceLockBase.getWifiDefault() + "");
    }

    /**
     * 是否有亲情模块
     * 默认有
     */
    public static boolean hasLoveModule(String deviceType, String meterType, String softwareVersion) {
        return TextUtils.isEmpty(meterType) || TextUtils.isEmpty(softwareVersion) || hasLoveModule(getDeviceLock(deviceType, meterType, softwareVersion));
    }

    /**
     * 是否有亲情模块
     * 默认有
     */
    public static boolean hasLoveModule(DeviceLockBase deviceLockBase) {
        return deviceLockBase == null || "1".equalsIgnoreCase(deviceLockBase.getModuleLoveFlag() + "");
    }

    /**
     * 是否有报警模块
     * 默认有
     */
    public static boolean hasAlarmModule(String deviceType, String meterType, String softwareVersion) {
        return TextUtils.isEmpty(meterType) || TextUtils.isEmpty(softwareVersion) || hasAlarmModule(getDeviceLock(deviceType, meterType, softwareVersion));
    }

    /**
     * 是否有报警模块
     * 默认有
     */
    public static boolean hasAlarmModule(DeviceLockBase deviceLockBase) {
        return deviceLockBase != null && "1".equalsIgnoreCase(deviceLockBase.getModuleAlarmFlag() + "");
    }

    /**
     * 获取蒙板类型
     * 默认为{LockSDKLibrary.MaskGuideType_700}
     */
    public static int getMaskGuide(String deviceType, String meterType) {
        if (TextUtils.isEmpty(meterType)) {
            return DsmLibrary.MaskGuideType_700;
        }
        return getMaskGuide(getDeviceLock(deviceType, meterType, null));
    }

    /**
     * 获取蒙板类型
     * 默认为{LockSDKLibrary.MaskGuideType_700}
     */
    private static int getMaskGuide(DeviceLockBase deviceLockBase) {
        if (deviceLockBase == null) {
            return DsmLibrary.MaskGuideType_700;
        }
        return deviceLockBase.getGuidType();
    }
}