package com.dsm.platform.listener;

/**
 * 库加载监听器
 */

public interface OnLibraryLoadListener {
    void loadSuccess(Object data);
    void loadFailure(Integer msgCode);
}
