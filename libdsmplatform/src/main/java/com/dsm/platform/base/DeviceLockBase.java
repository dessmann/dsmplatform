package com.dsm.platform.base;

import net.tsz.afinal.annotation.sqlite.Table;

/**
 * DeviceLockBase
 *
 * @author SJL
 * @date 2017/6/14
 */
@Table(name = "device_lock_base")
public class DeviceLockBase {
    private Long serialVersionUID = 0L;

    public Long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setSerialVersionUID(Long serialVersionUID) {
        this.serialVersionUID = serialVersionUID;
    }

    private Integer id;
    private Integer propertyId;  //属性Id(app暂时无用处)
    /**
     * 锁具型号+具体固件版本信息
     * 匹配字段
     */
    private String meterType;  //锁具详细类型(如：D820_1)
    private String appVersion; //固件版本 (如：V0.2.002_20161124）
    /**
     * 锁具，固件基本信息
     */
    private String devCode;  //设备大类 (如S800，D820)
    private String devName;  //设备名称(App端是否需要使用未知)
    private Integer devType; //设备类型 (11.锁  13.门禁  16.保险箱)
    private String devTypeCode;  //设备类型 (lock.锁 safe.保险箱)

    /**
     * 协议加密模块
     */
    private Integer cipherType;//加密芯片类型： 0 为无加密 1. Gemato加密芯片
    private Integer encryptFlag;//固件 是否支持公司自己的加密协议
    /**
     * 用户模块
     */
    private Integer userFlag;//是否支持添加用户 0 否 1，是    (如果添加用户页面无需动态配置，可忽略)
    private Integer userCapacity;//最大用户容量 (如 现在的锁具类型是150个用户)
    /**
     * 指纹模块
     */
    private Integer fingerFlag;//是否支持添加指纹 0 否 1，是  (如 保险箱无法录制指纹等)
    private Integer fingerCapacity;//手机录入指纹容量  (如 D820支持60枚指纹，其他类型支持 80枚)
    private Integer localFingerFlag;//是否支持本地指纹上传    0 否 1，是
    private Integer localFingerCapacity;//本地指纹最大容量 (暂时无此功能的限制，可忽略)
    private Integer fingerConfirmFlag;//固件协议是否支持 指纹录入是否需要确认 0.否， 1.是) (53协议是需要确认)
    /**
     * 外接设备支持模块
     */
    private Integer smartKeyFlag; //是否支持智能钥匙 0.否 1.是
    private Integer bongFlag; //是否支持添加智能手环 0.否 1.是
    /**
     * 开门方式
     */
    private Integer passwordFlag;//是否支持锁具密码设置  0 否 1，是 (App段是否需要 动态配置该页面，未知)
    private Integer tempPasswordFlag; //是否支持临时密码  0.否 1.是
    private Integer tempKeyFlag; //是否支持临时钥匙  0.否 1.是
    private Integer phoneFlag; //是否支持手机开门方式 0.否 1.是
    private String phoneOpenType;//手机支持的开门方式配置(0.不用手机，1.手机密码 2.摇一摇 3.手势 4.点一点) 支持多种开门方式 则用,(半角逗号)分割  如 0,1,2
    /**
     * 开门记录模块
     */
    private Integer recordUploadFlag;//锁具是否支持开门记录上传 0.否 1.是
    private Integer recordUploadDefault;//固件 开门记录上传的默认值 0.关 ， 1.开
    /**
     * wifi模块
     */
    private Integer wifiDefault;//设备是否支持wifi切换功能，1表示支持 0表示不支持
    private Integer wifiTestFlag; //是否支持WIFI推送检测 0. 否， 1.是
    /**
     * 是否支持亲情模块 Integer 0 否 1是
     */
    private Integer moduleLoveFlag;
    /**
     * 是否支持报警模块 Integer 0 否 1是
     */
    private Integer moduleAlarmFlag;
    /**
     * 蒙版的配置<br>
     * 默认是1<br>
     */
    private Integer guidType;//1.t700蓝牙蒙版样式  2.D820蓝牙蒙版样式 3.510蓝牙蒙版样式

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Integer propertyId) {
        this.propertyId = propertyId;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDevCode() {
        return devCode;
    }

    public void setDevCode(String devCode) {
        this.devCode = devCode;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public Integer getDevType() {
        return devType;
    }

    public void setDevType(Integer devType) {
        this.devType = devType;
    }

    public String getDevTypeCode() {
        return devTypeCode;
    }

    public void setDevTypeCode(String devTypeCode) {
        this.devTypeCode = devTypeCode;
    }

    public Integer getCipherType() {
        return cipherType;
    }

    public void setCipherType(Integer cipherType) {
        this.cipherType = cipherType;
    }

    public Integer getEncryptFlag() {
        return encryptFlag;
    }

    public void setEncryptFlag(Integer encryptFlag) {
        this.encryptFlag = encryptFlag;
    }

    public Integer getUserFlag() {
        return userFlag;
    }

    public void setUserFlag(Integer userFlag) {
        this.userFlag = userFlag;
    }

    public Integer getUserCapacity() {
        return userCapacity;
    }

    public void setUserCapacity(Integer userCapacity) {
        this.userCapacity = userCapacity;
    }

    public Integer getFingerFlag() {
        return fingerFlag;
    }

    public void setFingerFlag(Integer fingerFlag) {
        this.fingerFlag = fingerFlag;
    }

    public Integer getFingerCapacity() {
        return fingerCapacity;
    }

    public void setFingerCapacity(Integer fingerCapacity) {
        this.fingerCapacity = fingerCapacity;
    }

    public Integer getLocalFingerCapacity() {
        return localFingerCapacity;
    }

    public void setLocalFingerCapacity(Integer localFingerCapacity) {
        this.localFingerCapacity = localFingerCapacity;
    }

    public Integer getFingerConfirmFlag() {
        return fingerConfirmFlag;
    }

    public void setFingerConfirmFlag(Integer fingerConfirmFlag) {
        this.fingerConfirmFlag = fingerConfirmFlag;
    }

    public Integer getSmartKeyFlag() {
        return smartKeyFlag;
    }

    public void setSmartKeyFlag(Integer smartKeyFlag) {
        this.smartKeyFlag = smartKeyFlag;
    }

    public Integer getBongFlag() {
        return bongFlag;
    }

    public void setBongFlag(Integer bongFlag) {
        this.bongFlag = bongFlag;
    }

    public Integer getPasswordFlag() {
        return passwordFlag;
    }

    public void setPasswordFlag(Integer passwordFlag) {
        this.passwordFlag = passwordFlag;
    }

    public Integer getTempPasswordFlag() {
        return tempPasswordFlag;
    }

    public void setTempPasswordFlag(Integer tempPasswordFlag) {
        this.tempPasswordFlag = tempPasswordFlag;
    }

    public Integer getTempKeyFlag() {
        return tempKeyFlag;
    }

    public void setTempKeyFlag(Integer tempKeyFlag) {
        this.tempKeyFlag = tempKeyFlag;
    }

    public Integer getPhoneFlag() {
        return phoneFlag;
    }

    public void setPhoneFlag(Integer phoneFlag) {
        this.phoneFlag = phoneFlag;
    }

    public String getPhoneOpenType() {
        return phoneOpenType;
    }

    public void setPhoneOpenType(String phoneOpenType) {
        this.phoneOpenType = phoneOpenType;
    }

    public Integer getRecordUploadFlag() {
        return recordUploadFlag;
    }

    public void setRecordUploadFlag(Integer recordUploadFlag) {
        this.recordUploadFlag = recordUploadFlag;
    }

    public Integer getRecordUploadDefault() {
        return recordUploadDefault;
    }

    public void setRecordUploadDefault(Integer recordUploadDefault) {
        this.recordUploadDefault = recordUploadDefault;
    }

    public Integer getWifiDefault() {
        return wifiDefault;
    }

    public void setWifiDefault(Integer wifiDefault) {
        this.wifiDefault = wifiDefault;
    }

    public Integer getWifiTestFlag() {
        return wifiTestFlag;
    }

    public void setWifiTestFlag(Integer wifiTestFlag) {
        this.wifiTestFlag = wifiTestFlag;
    }

    public Integer getModuleLoveFlag() {
        return moduleLoveFlag;
    }

    public void setModuleLoveFlag(Integer moduleLoveFlag) {
        this.moduleLoveFlag = moduleLoveFlag;
    }

    public Integer getModuleAlarmFlag() {
        return moduleAlarmFlag;
    }

    public void setModuleAlarmFlag(Integer moduleAlarmFlag) {
        this.moduleAlarmFlag = moduleAlarmFlag;
    }

    public Integer getGuidType() {
        return guidType;
    }

    public void setGuidType(Integer guidType) {
        this.guidType = guidType;
    }

    public Integer getLocalFingerFlag() {
        return localFingerFlag;
    }

    public void setLocalFingerFlag(Integer localFingerFlag) {
        this.localFingerFlag = localFingerFlag;
    }

    @Override
    public String toString() {
        return "DeviceLockBase{" +
                "serialVersionUID=" + serialVersionUID +
                ", id=" + id +
                ", propertyId=" + propertyId +
                ", meterType(设备型号)='" + meterType + '\'' +
                ", appVersion(软件版本)='" + appVersion + '\'' +
                ", devCode(设备大类)='" + devCode + '\'' +
                ", devName(设备名称)='" + devName + '\'' +
                ", devType=(设备类型)" + devType +
                ", devTypeCode(设备类型描述码)='" + devTypeCode + '\'' +
                ", cipherType(gemalto加密芯片类型)=" + cipherType + ("1".equalsIgnoreCase(cipherType + "") ? "(是gemalto加密设备)" : "(不是gemalto加密设备)") +
                ", encryptFlag(是否支持公司自己的加密协议)=" + encryptFlag + ("1".equalsIgnoreCase(encryptFlag + "") ? "(支持公司自己的加密协议)" : "(不支持公司自己的加密协议)") +
                ", userFlag(是否支持添加用户)=" + userFlag + ("1".equalsIgnoreCase(userFlag + "") ? "(支持添加用户)" : "(不支持添加用户)") +
                ", userCapacity(最大用户容量)=" + userCapacity +
                ", fingerFlag(是否支持添加指纹)=" + fingerFlag + ("1".equalsIgnoreCase(fingerFlag + "") ? "(支持添加指纹)" : "(不支持添加指纹)") +
                ", fingerCapacity(手机录入指纹容量)=" + fingerCapacity +
                ", localFingerCapacity(本地指纹最大容量)=" + localFingerCapacity +
                ", fingerConfirmFlag(固件是否支持指纹录入确认协议)=" + fingerConfirmFlag + ("1".equalsIgnoreCase(fingerConfirmFlag + "") ? "(支持指纹录入确认协议)" : "(不支持指纹录入确认协议)") +
                ", smartKeyFlag(是否支持智能钥匙)=" + smartKeyFlag + ("1".equalsIgnoreCase(smartKeyFlag + "") ? "(支持智能钥匙)" : "(不支持智能钥匙)") +
                ", bongFlag(是否支持添加智能手环)=" + bongFlag + ("1".equalsIgnoreCase(bongFlag + "") ? "(支持添加智能手环)" : "(不支持添加智能手环)") +
                ", passwordFlag(是否支持锁具密码设置)=" + passwordFlag + ("1".equalsIgnoreCase(passwordFlag + "") ? "(支持锁具密码设置)" : "(不支持锁具密码设置)") +
                ", tempPasswordFlag(是否支持临时密码)=" + tempPasswordFlag + ("1".equalsIgnoreCase(tempPasswordFlag + "") ? "(支持临时密码)" : "(不支持临时密码)") +
                ", tempKeyFlag(是否支持临时钥匙)=" + tempKeyFlag + ("1".equalsIgnoreCase(tempKeyFlag + "") ? "(支持临时钥匙)" : "(不支持临时钥匙)") +
                ", phoneFlag(是否支持手机开门)=" + phoneFlag + ("1".equalsIgnoreCase(phoneFlag + "") ? "(支持手机开门)" : "(不支持手机开门)") +
                ", phoneOpenType(手机支持的开门方式配置【0.不用手机，1.手机密码 2.摇一摇 3.手势 4.点一点) 支持多种开门方式 则用,(半角逗号)分割  如 0,1,2】='" + phoneOpenType + '\'' +
                ", recordUploadFlag(锁具是否支持开门记录上传)=" + recordUploadFlag + ("1".equalsIgnoreCase(recordUploadFlag + "") ? "(支持开门记录上传)" : "(不支持开门记录上传)") +
                ", recordUploadDefault(开门记录上传的默认值)=" + recordUploadDefault + ("1".equalsIgnoreCase(recordUploadDefault + "") ? "(开门记录上传默认开启)" : "(开门记录上传默认关闭)") +
                ", wifiDefault(设备是否支持wifi切换功能)=" + wifiDefault + ("1".equalsIgnoreCase(wifiDefault + "") ? "(支持wifi切换功能)" : "(不支持wifi切换功能)") +
                ", wifiTestFlag(是否支持WIFI推送检测)=" + wifiTestFlag + ("1".equalsIgnoreCase(wifiTestFlag + "") ? "(支持WIFI推送检测)" : "(不支持WIFI推送检测)") +
                ", moduleLoveFlag(是否支持亲情模块)=" + moduleLoveFlag + ("1".equalsIgnoreCase(moduleLoveFlag + "") ? "(支持亲情模块)" : "(不支持亲情模块)") +
                ", moduleAlarmFlag(是否支持报警模块)=" + moduleAlarmFlag + ("1".equalsIgnoreCase(moduleAlarmFlag + "") ? "(支持报警模块)" : "(不支持报警模块)") +
                ", guidType(蒙版的配置【1.t700蓝牙蒙版样式  2.D820蓝牙蒙版样式 3.510蓝牙蒙版样式】)=" + guidType +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DeviceLockBase)) {
            return false;
        }
        DeviceLockBase deviceLockBase = (DeviceLockBase) obj;
        devTypeCode = devTypeCode == null ? "" : devTypeCode;
        return ((devType + "").equalsIgnoreCase(deviceLockBase.getDevType() +"")
                || devTypeCode.equalsIgnoreCase(deviceLockBase.getDevTypeCode()))
                && meterType.equalsIgnoreCase(deviceLockBase.getMeterType())
                && appVersion.equalsIgnoreCase(deviceLockBase.getAppVersion());
    }
}
