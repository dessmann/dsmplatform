package com.dsm.platform.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.dsm.platform.R;
import com.dsm.platform.util.PermisstionUtil;
import com.dsm.platform.util.RxBus;
import com.dsm.platform.util.ToastUtil;
import com.dsm.platform.util.log.LogUtil;
import com.umeng.analytics.MobclickAgent;

import rx.functions.Action1;

/**
 * DsmBaseActivity
 *
 * @author SJL
 * @date 2017/2/17
 */

public abstract class DsmBaseActivity extends Activity {
    private static final String TAG = "DsmBaseActivity";
    private static final String APP_DESTORY = "com.dsm.dream.destory";
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        initRxBus(this);
        PermisstionUtil.requestPermissions(mContext, PermisstionUtil.STORAGE, PermisstionUtil.STORAGE_CODE, getString(R.string.permission_storage), null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        RxBus.unSubscibe(this);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtil.i(TAG, "onRequestPermissionsResult");
        PermisstionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void initRxBus(Object subscriber) {
        RxBus.subscribe(subscriber, String.class, new Action1<String>() {
            @Override
            public void call(String message) {
                if (APP_DESTORY.equals(message)) {
                    finish();
                }
            }
        }, APP_DESTORY);
    }

    public static void exitApp() {
        RxBus.post(APP_DESTORY, APP_DESTORY);
    }

    public void toast(String tag, String msg){
        ToastUtil.showToastLong(tag, msg);
    }
}
