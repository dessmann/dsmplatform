package com.dsm.platform.base;

import android.text.TextUtils;
import android.util.Log;

import com.dsm.platform.DsmLibrary;
import com.dsm.platform.listener.OnServerUnitListener;
import com.dsm.platform.util.SharedPreferencesUtil;
import com.dsm.platform.util.log.LogUtil;
import com.dsm.platform.util.net.NoHttpUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务器工具类
 */

@SuppressWarnings("ALL")
public class ServerUtil {

    private static final String TAG = "ServerUtil";

    public static void request(String url, Map<String, String> data, Class clazz, OnServerUnitListener onServerUnitListener) {
        NoHttpUtil.getInstance(DsmLibrary.application).asyncPostStringNoEncryptRequest(url, buildHeader(), data, buildCommonResponseListener(clazz, onServerUnitListener));
    }

    public static Map<String, String> buildStringData(String[] keys, String[] values) {
        Map<String, String> data = new HashMap<>();
        if (keys == null || values == null || keys.length != values.length) {
            return null;
        }
        for (int index = 0; index < keys.length; index++) {
            data.put(keys[index], values[index]);
        }
        return data;
    }

    public static Map<String, Object> buildObjectData(String[] keys, Object[] values) {
        Map<String, Object> data = new HashMap<>();
        if (keys == null || values == null || keys.length != values.length) {
            return null;
        }
        for (int index = 0; index < keys.length; index++) {
            data.put(keys[index], values[index]);
        }
        return data;
    }

    private static Map<String, String> buildHeader() {
        Map<String, String> header = new HashMap<>();
        String token = SharedPreferencesUtil.getString(DsmLibrary.application, "token", "");
        if (!TextUtils.isEmpty(token)) {
            header.put("token", token);
        }
        return header;
    }

    private static NoHttpUtil.CommonResponseListener buildCommonResponseListener(final Class clazz, final OnServerUnitListener onServerUnitListener) {
        return new NoHttpUtil.CommonResponseListener(clazz) {

            @Override
            public void requestSuccess(List data, String msg) {
                try {
                    if (clazz != null) {
                        String token = clazz.getDeclaredMethod("getToken").invoke(data.get(0)).toString();
                        if(!TextUtils.isEmpty(token)) {
                            SharedPreferencesUtil.putString(DsmLibrary.application, "token", token);
                        }
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }
                onServerUnitListener.success(data, msg);
            }

            @Override
            public void requestFailed(String error, int loglever) {
                onServerUnitListener.failure(error, loglever);
            }
        };
    }

    public static void uploadFile(String url, Map<String, String> postMap, String fileKey, File file, final OnServerUnitListener onServerUnitListener) {
        if (onServerUnitListener == null) {
            LogUtil.e(TAG, "onServerUnitListener == null");
            return;
        }
        if (TextUtils.isEmpty(url) || !(url.startsWith("http://") || url.startsWith("https://"))) {
            onServerUnitListener.failure("url验证失败", Log.ERROR);
            return;
        }
        if (TextUtils.isEmpty(fileKey)) {
            onServerUnitListener.failure("文件键验证失败", Log.ERROR);
            return;
        }
        if (file == null) {
            onServerUnitListener.failure("文件校验失败", Log.ERROR);
            return;
        }
        NoHttpUtil.getInstance(DsmLibrary.application).uploadFile(url, postMap, fileKey, file, new NoHttpUtil.CommonResponseListener() {

            @Override
            public void requestSuccess(List data, String msg) {
                onServerUnitListener.success(data, msg);
            }

            @Override
            public void requestFailed(String error, int loglever) {
                onServerUnitListener.failure(error, loglever);
            }
        });
    }

    public static void downloadFile(String url, String fileFolder, String filename, NoHttpUtil.DownloadResponseListener listener) {
        if (listener == null) {
            LogUtil.e(TAG, "NoHttpUtil.DownloadResponseListener == null");
            return;
        }
        if (TextUtils.isEmpty(url) || !(url.startsWith("http://") || url.startsWith("https://"))) {
            listener.onFailure("url验证失败", Log.ERROR);
            return;
        }
        if (TextUtils.isEmpty(fileFolder) || TextUtils.isEmpty(filename)) {
            listener.onFailure("文件在本地的存储路径或文件名验证失败", Log.ERROR);
            return;
        }
        NoHttpUtil.getInstance(DsmLibrary.application).download(url, fileFolder, filename, listener);
    }

    public static void requestImage(String url, final OnServerUnitListener onServerUnitListener) {
        if (onServerUnitListener == null) {
            LogUtil.e(TAG, "onServerUnitListener == null");
            return;
        }
        if (TextUtils.isEmpty(url) || !(url.startsWith("http://") || url.startsWith("https://"))) {
            onServerUnitListener.failure("图片url验证失败", Log.ERROR);
            return;
        }
        NoHttpUtil.getInstance(DsmLibrary.application).sendImageRequest(url, new NoHttpUtil.CommonResponseListener() {

            @Override
            public void requestSuccess(List data, String msg) {
                onServerUnitListener.success(data, msg);
            }

            @Override
            public void requestFailed(String error, int loglever) {
                onServerUnitListener.failure(error, loglever);
            }
        });
    }

    public static void doAjax(boolean encrypt, String url, Map<String, String> param, Class clazz, OnServerUnitListener onServerUnitListener) {
        if (encrypt) {
            NoHttpUtil.getInstance(DsmLibrary.application).asyncPostStringEncryptRequest(url, buildHeader(), param, buildCommonResponseListener(clazz, onServerUnitListener));
            return;
        }
        NoHttpUtil.getInstance(DsmLibrary.application).asyncPostStringNoEncryptRequest(url, buildHeader(), param, buildCommonResponseListener(clazz, onServerUnitListener));
    }
}
