package com.dsm.platform.listener;

import java.util.List;

/**
 * 网络请求监听器
 */

public interface OnServerUnitListener {
    void success(List data, String msg);
    void failure(String error, int loglevel);
}
