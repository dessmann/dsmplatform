package com.dsm.platform.config;

/**
 * 本地保存数据键名
 *
 * @author SJL
 * @date 2017/2/22
 */

public class ConfigSharedPreferences {
    /**
     * 用户名
     */
    public static final String USERNAME = "username";
    /**
     * 密码
     */
    public static final String PASSWORD = "password";
    /**
     * 最近一次登录的时间
     */
    public static String LOGIN_TIME = "login_time";
    /**
     * 最近一次高级设置时间
     */
    public static final String SENIOR_CHECK_TIME = "senior_check_time";
    /**
     * 当前设备MAC
     */
    public static String CURRENT_DEVICE_MAC = "current_device_mac";
    /**
     * 当前锁MAC
     */
    public static String CURRENT_DEVICE_LOCK_MAC = "current_device_lock_mac";
    /**
     * 当前手环MAC
     */
    public static String CURRENT_DEVICE_BONG_MAC = "current_device_bong_mac";
    /**
     * 最后同步数据的手环MAC
     */
    public static String LAST_DEVICE_BONG_MAC = "last_device_bong_mac";
    /**
     * APP版本号
     */
    public static final String APP_VERSION = "app_version";
    /**
     * 显示授权弹窗
     */
    public static final String SHOW_AUTH_ALERT = "show_auth_alert";
    /**
     * APP DEX版本
     */
    public static String APP_DEX_VERSION = "app_dex_version";

    //蒙版
    /**
     * 首页添加设备按钮蒙版
     */
    public static final String GUIDE_MAIN_ADD = "guide_main_add";

    //猫眼推送
    /**
     * 指定该管理员帐号下的猫眼可以接收猫眼推送信息
     */
    public static final String PUSH_MANAGER_ACCOUNT = "push_account";
}
