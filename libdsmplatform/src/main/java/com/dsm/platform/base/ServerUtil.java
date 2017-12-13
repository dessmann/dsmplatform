package com.dsm.platform.base;

import android.text.TextUtils;
import android.util.Log;

import com.dsm.platform.DsmLibrary;
import com.dsm.platform.R;
import com.dsm.platform.listener.OnServerUnitListener;
import com.dsm.platform.util.SharedPreferencesUtil;
import com.dsm.platform.util.log.LogUtil;
import com.dsm.platform.util.net.NoHttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
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
        NoHttpUtil.getInstance(DsmLibrary.application).asyncPostStringNoEncryptRequest(url, buildHeader(), data, buildCommonResponseListener(url, clazz, onServerUnitListener));
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

    private static NoHttpUtil.CommonResponseListener buildCommonResponseListener(final String url, final Class clazz, final OnServerUnitListener onServerUnitListener) {
        return new NoHttpUtil.CommonResponseListener(clazz) {

            @Override
            public void requestSuccess(List data, String msg) {
                getToken(url, data, clazz);
                onServerUnitListener.success(data, msg);
            }

            @Override
            public void requestFailed(String error, int loglever) {
                onServerUnitListener.failure(error, loglever);
            }
        };
    }

    /**
     * 登陆、注册、忘记密码等涉及到登陆的接口成功时获取token
     *
     * @param url
     * @param data
     * @param clazz
     */
    private static void getToken(String url, List data, Class clazz) {
        try {
            if (url.endsWith("/xiaodi/user/login.action")
                    || url.endsWith("/xiaodi/user/register.action")
                    || url.endsWith("/xiaodi/user/updateUserPassDirectly.action")) {
                String token;
                if (clazz != null) {
                    Field field = clazz.getDeclaredField("token");
                    field.setAccessible(true);
                    token = field.get(data.get(0)).toString();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray((String) data.get(0));
                        token = jsonArray.getJSONObject(0).getString("token");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        JSONObject jsonObject = new JSONObject((String) data.get(0));
                        token = jsonObject.getString("token");
                    }
                }
                SharedPreferencesUtil.putString(DsmLibrary.application, "token", token);
                LogUtil.i(TAG, "NoHttpUtil.token=" + token);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "getToken:" + e.getMessage());
        }
    }

    public static void uploadFile(String url, Map<String, String> postMap, String fileKey, File file, final OnServerUnitListener onServerUnitListener) {
        if (onServerUnitListener == null) {
            LogUtil.e(TAG, "onServerUnitListener == null");
            return;
        }
        if (TextUtils.isEmpty(url) || !(url.startsWith("http://") || url.startsWith("https://"))) {
            onServerUnitListener.failure(DsmLibrary.application.getString(R.string.url_valide_failure), Log.ERROR);
            return;
        }
        if (TextUtils.isEmpty(fileKey)) {
            onServerUnitListener.failure(DsmLibrary.application.getString(R.string.file_attr_validate_failure), Log.ERROR);
            return;
        }
        if (file == null) {
            onServerUnitListener.failure(DsmLibrary.application.getString(R.string.file_validate_failure), Log.ERROR);
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
            listener.onFailure(DsmLibrary.application.getString(R.string.url_valide_failure), Log.ERROR);
            return;
        }
        if (TextUtils.isEmpty(fileFolder) || TextUtils.isEmpty(filename)) {
            listener.onFailure(DsmLibrary.application.getString(R.string.file_path_validate_failure), Log.ERROR);
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
            onServerUnitListener.failure(DsmLibrary.application.getString(R.string.pic_url_validate_failure), Log.ERROR);
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
            NoHttpUtil.getInstance(DsmLibrary.application).asyncPostStringEncryptRequest(url, buildHeader(), param, buildCommonResponseListener(url, clazz, onServerUnitListener));
            return;
        }
        NoHttpUtil.getInstance(DsmLibrary.application).asyncPostStringNoEncryptRequest(url, buildHeader(), param, buildCommonResponseListener(url, clazz, onServerUnitListener));
    }
}
