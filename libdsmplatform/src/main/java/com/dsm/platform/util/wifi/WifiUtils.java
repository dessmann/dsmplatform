package com.dsm.platform.util.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;

import com.dsm.platform.util.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class WifiUtils {

	private static final String TAG = "WifiUtils";
	private final WifiManager localWifiManager;//提供Wifi管理的各种主要API，主要包含wifi的扫描、建立连接、配置信息等
	//private List<ScanResult> wifiScanList;//ScanResult用来描述已经检测出的接入点，包括接入的地址、名称、身份认证、频率、信号强度等
	private List<WifiConfiguration> wifiConfigList;//WIFIConfiguration描述WIFI的链接信息，包括SSID、SSID隐藏、password等的设置
	private WifiInfo wifiConnectedInfo;//已经建立好网络链接的信息
	private WifiLock wifiLock;//手机锁屏后，阻止WIFI也进入睡眠状态及WIFI的关闭
	
	public WifiUtils(Context context){
		localWifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
	}
	
    //检查WIFI状态
	public int wifiCheckState(){
		return localWifiManager.getWifiState();
	}
	
	//开启WIFI
	public void wifiOpen(){
		if(!localWifiManager.isWifiEnabled()){
			localWifiManager.setWifiEnabled(true);
		}
	}
	
	//关闭WIFI
	public void wifiClose(){
		if(!localWifiManager.isWifiEnabled()){
			localWifiManager.setWifiEnabled(false);
		}
	}
	
	//扫描wifi
	public void wifiStartScan(){
		localWifiManager.startScan();
	}
	
	//得到Scan结果
	public List<ScanResult> getScanResults(){
		return localWifiManager.getScanResults();//得到扫描结果
	}

	//Scan结果转为Sting
	public List<String> scanResultToString(List<ScanResult> list){
		List<String> strReturnList = new ArrayList<>();
		for(int i = 0; i < list.size(); i++){
			ScanResult strScan = list.get(i);
			String str = strScan.toString();
			boolean bool = strReturnList.add(str);
			if(!bool){
				Log.i("scanResultToSting","Addfail");
			}
		}
		return strReturnList;
	}
	
	//得到Wifi配置好的信息
	public void getConfiguration(){
		wifiConfigList = localWifiManager.getConfiguredNetworks();//得到配置好的网络信息
//		for(int i =0;i<wifiConfigList.size();i++){
//			Log.i("getConfiguration",wifiConfigList.get(i).SSID);
//			Log.i("getConfiguration",String.valueOf(wifiConfigList.get(i).networkId));
//			Log.i("getConfiguration",String.valueOf(wifiConfigList.get(i).preSharedKey));
//			Log.i("getConfiguration", wifiConfigList.get(i).toString());
//		}
	}
	//判定指定WIFI是否已经配置好,依据WIFI的地址BSSID,返回NetId
	public int isConfiguration(String SSID){
		Log.i("isConfiguration", String.valueOf(wifiConfigList.size()));
		for(int i = 0; i < wifiConfigList.size(); i++){
			Log.i(wifiConfigList.get(i).SSID, String.valueOf( wifiConfigList.get(i).networkId));
			if(wifiConfigList.get(i).SSID.equals(SSID)){//地址相同
				return wifiConfigList.get(i).networkId;
			}
		}
		return -1;
	}

	//添加指定WIFI的配置信息,原列表不存在此SSID
	public int addWifiConfig(List<ScanResult> wifiList, String ssid, String pwd){
		int wifiId = -1;
		for(int i = 0;i < wifiList.size(); i++){
			ScanResult wifi = wifiList.get(i);
			if(wifi.SSID.equals(ssid)){
				Log.i("addWifiConfig","equals");
				WifiConfiguration wifiCong = new WifiConfiguration();
				wifiCong.SSID = "\""+wifi.SSID+"\"";//\"转义字符，代表"
				wifiCong.preSharedKey = "\""+pwd+"\"";//WPA-PSK密码
				wifiCong.hiddenSSID = false;
				wifiCong.status = WifiConfiguration.Status.ENABLED;
				wifiId = localWifiManager.addNetwork(wifiCong);//将配置好的特定WIFI密码信息添加,添加完成后默认是不激活状态，成功返回ID，否则为-1
				if(wifiId != -1){
					return wifiId;
				}
			}
		}
		return wifiId;
	}

	//添加指定WIFI的配置信息
	public int addWifiConfig(String ssid, String pwd){
		int wifiId;
		Log.i("addWifiConfig","equals");
//		getConfiguration();
//		for(int i = 0;i < wifiConfigList.size(); i++){
//			WifiConfiguration wifi = wifiConfigList.get(i);
//			if(wifi.SSID.equals("\""+ssid+"\"")){
//				if (!localWifiManager.removeNetwork(wifi.networkId)) {
//					LogUtil.e("addWifiConfig", "移除网络失败,id=" + wifi.networkId);
//				} else {
//					LogUtil.i("addWifiConfig", "移除网络成功,id=" + wifi.networkId);
//				}
//			}
//		}
		WifiConfiguration wifiCong = new WifiConfiguration();
		wifiCong.SSID = "\""+ssid+"\"";//\"转义字符，代表"
		wifiCong.preSharedKey = "\""+pwd+"\"";//WPA-PSK密码
		wifiCong.hiddenSSID = false;
		wifiCong.status = WifiConfiguration.Status.ENABLED;
		wifiId = localWifiManager.addNetwork(wifiCong);//将配置好的特定WIFI密码信息添加,添加完成后默认是不激活状态，成功返回ID，否则为-1
		return wifiId;
	}

	public boolean removeWifiConfig(String ssid){
		Log.i("removeWifiConfig","ssid=" + ssid);
		getConfiguration();
		for(int i = 0;i < wifiConfigList.size(); i++){
			WifiConfiguration wifi = wifiConfigList.get(i);
			if(wifi.SSID.equals("\"" + ssid + "\"")){
				Log.i("removeWifiConfig","请求移除wifi配置，id=" + wifi.networkId);
				if (!localWifiManager.removeNetwork(wifi.networkId)) {
					Log.i("removeWifiConfig","移除wifi配置失败，id=" + wifi.networkId);
					return false;

				}
				Log.i("removeWifiConfig","移除wifi配置成功，id=" + wifi.networkId);
				return true;
			}
		}
		return false;
	}
	
	//连接指定Id的WIFI
	public boolean connectWifi(int wifiId){
		for(int i = 0; i < wifiConfigList.size(); i++){
			WifiConfiguration wifi = wifiConfigList.get(i);
			if(wifi.networkId == wifiId){
				return localWifiManager.enableNetwork(wifiId, true);
			}
		}
		return false;
	}
	
	//创建一个WIFILock
	public void createWifiLock(String lockName){
		wifiLock = localWifiManager.createWifiLock(lockName);
	}
	
	//锁定wifilock
	public void acquireWifiLock(){
		wifiLock.acquire();
}

	//解锁WIFI
	public void releaseWifiLock(){
		if(wifiLock.isHeld()){//判定是否锁定
			wifiLock.release();
		}
	}

	//得到建立连接的信息
	private void getConnectedInfo(){
		wifiConnectedInfo = localWifiManager.getConnectionInfo();
	}
	//得到连接的MAC地址
	public String getConnectedMacAddr(){
		return (wifiConnectedInfo == null)? "NULL":wifiConnectedInfo.getMacAddress();
	}

	//得到连接的名称SSID
	public String getConnectedSSID(){
		getConnectedInfo();
//		return (wifiConnectedInfo == null)? "NULL":wifiConnectedInfo.getSSID();
		if(wifiConnectedInfo == null || wifiConnectedInfo.getSupplicantState() != SupplicantState.COMPLETED) {
			return null;
		}
		String ssid = wifiConnectedInfo.getSSID();
//		if(ssid.startsWith("\"")){
//			ssid.substring(1);
//		}
//		if (ssid.endsWith("\"")){
//			ssid.substring(0, ssid.length() - 1);
//		}
		LogUtil.i(TAG, "ssid=" +ssid);
		return ssid.replace("\"", "");
	}

	//得到连接的IP地址
	public int getConnectedIPAddr(){
		return (wifiConnectedInfo == null)? 0:wifiConnectedInfo.getIpAddress();
	}

	//得到连接的ID
	public int getConnectedID(){
		getConnectedInfo();
		return (wifiConnectedInfo == null)? -1:wifiConnectedInfo.getNetworkId();
	}


	public static int getEncrypPasswordType(String capabilities) {
		if (capabilities.contains("WPA2") && capabilities.contains("CCMP")) {
			// sEncrypType = "AES";
			// sAuth = "WPA2";
			return 1;
		} else if (capabilities.contains("WPA2")
				&& capabilities.contains("TKIP")) {
			// sEncrypType = "TKIP";
			// sAuth = "WPA2";
			return 2;
		} else if (capabilities.contains("WPA")
				&& capabilities.contains("TKIP")) {
			// EncrypType = "TKIP";
			// sAuth = "WPA";
			return 2;
		} else if (capabilities.contains("WPA")
				&& capabilities.contains("CCMP")) {
			// sEncrypType = "AES";
			// sAuth = "WPA";
			return 1;
		} else if (capabilities.contains("WEP")) {
			return 3;
		} else {
			// sEncrypType = "NONE";
			// sAuth = "OPEN";
			return 0;
		}
	}
}
