package com.dsm.platform.listener;

/**
 * wifi配置检测接口
 */

public interface OnCheckWifiConfigListener {
    void onConnectWifiFailure(int msgCode);
    void onConnectWifiSuccess(String nid, String ssid);
}
