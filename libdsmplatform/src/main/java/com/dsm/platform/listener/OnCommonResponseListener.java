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
    public void finish(String tag, final boolean status, final Object data, final String msg, final int loglevel){
        LogUtil.i(TAG, "finish\ntag=" + tag + "\nstatus=" + status + "\ndata=" + data + "\nmsg=" + msg + "\nloglevel=" + LogUtil.getLogTag(loglevel));
        if(status){
            onSuccess(data);
        }else{
            onFailure(msg, loglevel);
        }
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                if(status){
//                    onSuccess(data);
//                }else{
//                    onFailure(msg, loglevel);
//                }
//            }
//        });
    }

    /**
     * 请求成功
     */
    public abstract void onSuccess(Object data);

    /**
     * 请求失败
     */
    public abstract void onFailure(String error, int loglevel);
}
