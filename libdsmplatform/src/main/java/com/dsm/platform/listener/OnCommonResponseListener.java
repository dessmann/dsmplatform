package com.dsm.platform.listener;

import com.dsm.platform.util.log.LogUtil;

/**
 * 核心响应处理器
 */

public abstract class OnCommonResponseListener {

    private static final String TAG = OnCommonResponseListener.class.getSimpleName();

    /**
     * 结束请求
     */
    public void finish(String tag, final boolean status, final Object data, final int msgCode){
        LogUtil.i(TAG, "finish\ntag=" + tag + "\nstatus=" + status + "\ndata=" + data + "\nmsgCode=" + msgCode);
        if(status){
            onSuccess(data);
        }else{
            onFailure(msgCode);
        }
    }

    /**
     * 请求成功
     */
    public abstract void onSuccess(Object data);

    /**
     * 请求失败
     */
    public abstract void onFailure(int msgCode);
}
