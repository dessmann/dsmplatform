package com.dsm.platform.listener;

/**
 * wifi配置检测接口
 */

public interface OnCheckWifiConfigListener {
    void onNotFoundWifi(String error, int loglevel);
    void onAddWifiConfigFailure(String error, int loglevel);
    void onConnectWifiFailure(String error, int loglevel);
    void onConnectWifiSuccess(String nid, String ssid);
}
