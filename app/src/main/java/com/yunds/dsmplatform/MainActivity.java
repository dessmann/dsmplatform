package com.yunds.dsmplatform;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.dsm.platform.DsmLibrary;
import com.dsm.platform.base.BaseMsgCode;
import com.dsm.platform.base.ServerUtil;
import com.dsm.platform.listener.OnServerUnitListener;
import com.dsm.platform.util.ToastUtil;
import com.dsm.platform.util.log.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String logPath = Environment.getExternalStorageDirectory() + "/" + getPackageName().substring(getPackageName().lastIndexOf(".") + 1) + "/Log/";
        String logFileName = "log.txt";
        String releaseLogPath = getFilesDir() + "data/" + getPackageName() + "/cache/Log/";
        DsmLibrary.getInstance().init(getApplication(), null, true, false, logPath, logFileName, releaseLogPath);
        LogUtil.e(TAG, "onCreate,codeMap=" + BaseMsgCode.codeMap);
        findViewById(R.id.loginBtn).setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginBtn) {
            String url = "https://115.236.188.90:4437/xiaodi/user/login.action";
            Map<String, String> param = new HashMap<>();
            param.put("account", "18668165280");
            param.put("password", "12345678");
            param.put("appVersion", "");
            param.put("oemId", "29");
            progressDialog.show();
            ServerUtil.request(url, param, null, new OnServerUnitListener() {
                @Override
                public void success(List data, String msg) {
                    progressDialog.dismiss();
                    LogUtil.e(TAG, "success,codeMap=" + BaseMsgCode.codeMap);
                    Log.i(TAG, "登录成功\ndata=" +data + "\nmsg=" + msg);
                    ToastUtil.showToast("登录成功");
                }

                @Override
                public void failure(Integer msgCode) {
                    progressDialog.dismiss();
                    LogUtil.e(TAG, "failure,codeMap=" + BaseMsgCode.codeMap);
                    Log.i(TAG, "登录失败\nerror=" + BaseMsgCode.parseBLECodeMessage(msgCode));
                    ToastUtil.showToast("登录失败");
                }
            });
        }
    }
}
