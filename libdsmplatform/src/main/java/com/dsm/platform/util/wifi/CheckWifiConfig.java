package com.dsm.platform.util.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;

import com.dsm.platform.base.BaseMsgCode;
import com.dsm.platform.listener.OnCheckWifiConfigListener;
import com.dsm.platform.util.SystemUtil;
import com.dsm.platform.util.log.LogUtil;

import java.util.List;

/**
 * wifi配置验证<br/>
 * 主要验证wifi密码是否正确
 */

public class CheckWifiConfig {

    private static final String TAG = "CheckWifiConfig";
    private int newNetworkId = -1;
    private int originNetworkId = -1;
    private boolean checkTimeout = false;
    private final NetworkConnectChangedReceiver networkConnectChangedReceiver = new NetworkConnectChangedReceiver();
    private static final int WIFI_CHECK_TIT_INTERVAL = 10 * 1000;//wifi检测的超时间隔
    private final Handler wifiCheckTimeoutHandler = new Handler();
    private final Runnable wifiCheckTimeoutRunnable = new Runnable() {//wifi检测超时任务
        @Override
        public void run() {
            synchronized (TAG) {
                checkTimeout = true;
                LogUtil.e(TAG, "检测wifi配置超时");
                wifiUtils.removeWifiConfig(ssid);
                CheckWifiConfig.this.context.unregisterReceiver(networkConnectChangedReceiver);
                if (originNetworkId != -1) {
                    wifiUtils.connectWifi(originNetworkId);
                }
                if (onCheckWifiConfigListener != null) {
                    onCheckWifiConfigListener.onConnectWifiFailure(-60020);
                }
            }
        }
    };
    private WifiUtils wifiUtils;
    private Context context;
    private String ssid;
    private String password;
    private OnCheckWifiConfigListener onCheckWifiConfigListener;

    private class NetworkConnectChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.i(TAG, "onReceive, intent=" + intent);
            if(intent.getAction().equalsIgnoreCase(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
                int linkWifiResult = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
                if (linkWifiResult == WifiManager.ERROR_AUTHENTICATING) {
                    LogUtil.e(TAG, "ERROR_AUTHENTICATING");
                    LogUtil.e(TAG, "WIFI密码错误");
                    wifiUtils.removeWifiConfig(ssid);
                    CheckWifiConfig.this.context.unregisterReceiver(this);
                    if (originNetworkId != -1) {
                        wifiUtils.connectWifi(originNetworkId);
                    }
                    if (onCheckWifiConfigListener != null) {
                        if (checkTimeout) {
                            return;
                        }
                        wifiCheckTimeoutHandler.removeCallbacks(wifiCheckTimeoutRunnable);
                        onCheckWifiConfigListener.onConnectWifiFailure(-60016);
                    }
                }
            } else if (intent.getAction().equalsIgnoreCase(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    int networkId = wifiInfo.getNetworkId();
                    String connectedWifiSsid = wifiInfo.getSSID();
                    LogUtil.i(TAG, "wifi连接成功，连接的wifi ssid为：" + connectedWifiSsid + ",newNetworkId=" + networkId);
                    if (connectedWifiSsid.equals("\"" + ssid + "\"")) {
                        if (networkId != -1 && networkId == CheckWifiConfig.this.newNetworkId) {
                            LogUtil.i(TAG, "wifi密码验证成功,wifi ssid:" + connectedWifiSsid + ",newNetworkId=" + networkId);
                            wifiUtils.removeWifiConfig(ssid);
                            CheckWifiConfig.this.context.unregisterReceiver(this);
                            if (originNetworkId != -1) {
                                wifiUtils.connectWifi(originNetworkId);
                            }
                            if (onCheckWifiConfigListener != null) {
                                while (!SystemUtil.checkNetworkAvailable(CheckWifiConfig.this.context)) {
                                    LogUtil.i(TAG, "休眠500ms,等待网络开启...");
                                    SystemClock.sleep(500);

                                }
                                LogUtil.i(TAG, "网络已开启");
                                if (checkTimeout) {
                                    return;
                                }
                                wifiCheckTimeoutHandler.removeCallbacks(wifiCheckTimeoutRunnable);
                                onCheckWifiConfigListener.onConnectWifiSuccess(networkId + "", ssid);
                            }
                        }
                    }
                }
            }

        }
    }

    /**
     * 配置任务执行的上下文<br/>
     * 该上下文必须支持注册与解注册系统广播，必须配置
     */
    public CheckWifiConfig setContext(Context context) {
        this.context = context;
        wifiUtils = new WifiUtils(context);
        return this;
    }

    /**
     * 配置wifi的ssid<br/>
     * 必须配置
     */
    public CheckWifiConfig setSsid(String ssid) {
        this.ssid = ssid;
        return this;
    }

    /**
     * 配置wifi的密码<br/>
     * 必须配置，并且不能小于8位字符
     */
    public CheckWifiConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * 配置交互接口<br/>
     * 可不配置，不配置时将无法接收到执行状态报告
     */
    public CheckWifiConfig setOnCheckWifiConfigListener(OnCheckWifiConfigListener onCheckWifiConfigListener) {
        this.onCheckWifiConfigListener = onCheckWifiConfigListener;
        return this;
    }

    /**
     * 执行验证wifi密码的操作
     */
    public void walk() {
        Integer result = checkParams();
        if (result != null) {
            LogUtil.e(TAG, BaseMsgCode.parseBLECodeMessage(result));
            if (onCheckWifiConfigListener != null) {
                onCheckWifiConfigListener.onConnectWifiFailure(result);
            }
            return;
        }
        checkConfig();
    }

    /**
     * 验证参数
     */
    private Integer checkParams() {
        LogUtil.i(TAG, "ssid=" + ssid + ",pwd=" + password);
        if (context == null) {
            return -60013;
        }
        if (TextUtils.isEmpty(ssid) || ssid.length() > 33) {
            return -60018;
        }
        if (!TextUtils.isEmpty(password) && (password.length() < 8 || password.length() > 65)) {
            return -60017;
        }
        return null;
    }

    /**
     * 具体的验证wifi操作流程
     */
    private void checkConfig() {
        wifiUtils.wifiOpen();
        wifiUtils.wifiStartScan();
        //0正在关闭,1WIFi不可用,2正在打开,3可用,4状态不可zhi
        while(wifiUtils.wifiCheckState() != WifiManager.WIFI_STATE_ENABLED){//等待Wifi开启
            LogUtil.i(TAG, "休眠500ms,等待网络开启...");
        }
        LogUtil.i(TAG, "网络已开启");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        List<ScanResult> scanResultList = wifiUtils.getScanResults();
                        boolean contains = false;
                        for (ScanResult scanResult : scanResultList) {
                            if (scanResult.SSID.equalsIgnoreCase(ssid)) {
                                contains = true;
                                break;
                            }
                        }
                        if (!contains) {
                            LogUtil.e(TAG, "wifi列表没有当前ssid的wifi");
                            if (onCheckWifiConfigListener != null) {
                                onCheckWifiConfigListener.onConnectWifiFailure(-60014);
                            }
                            return;
                        }
                        newNetworkId = wifiUtils.addWifiConfig(ssid, password);
                        if (newNetworkId == -1) {
                            LogUtil.e(TAG, "添加wifi配置失败");
                            if (onCheckWifiConfigListener != null) {
                                onCheckWifiConfigListener.onConnectWifiFailure(-60019);
                            }
                            return;
                        }
                        LogUtil.i(TAG, "已添加wifi，newNetworkId=" + newNetworkId);
                        wifiUtils.getConfiguration();
                        originNetworkId = wifiUtils.getConnectedID();
                        LogUtil.i(TAG, "当前连接的wifi，originNetworkId=" + originNetworkId);
                        wifiCheckTimeoutHandler.postDelayed(wifiCheckTimeoutRunnable, WIFI_CHECK_TIT_INTERVAL);
                        registerNetworkConnectChangeReceiver(context);
                        if (!wifiUtils.connectWifi(newNetworkId)) {
                            LogUtil.e(TAG, "连接wifi失败");
                            if (onCheckWifiConfigListener != null) {
                                if (checkTimeout) {
                                    return;
                                }
                                wifiCheckTimeoutHandler.removeCallbacks(wifiCheckTimeoutRunnable);
                                onCheckWifiConfigListener.onConnectWifiFailure(-60015);
                            }
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 注册网络状态广播接收器
     */
    private void registerNetworkConnectChangeReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        context.registerReceiver(networkConnectChangedReceiver, filter);
    }
}
