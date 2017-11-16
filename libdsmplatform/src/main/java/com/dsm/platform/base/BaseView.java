package com.dsm.platform.base;

/**
 * 基础界面逻辑接口
 *
 * @author SJL
 * @date 2016/12/21
 */

public interface BaseView {

    /**
     * 显示提交弹窗
     *
     * @return
     */
    void showSubmitDialog();
    /**
     * 显示加载弹窗
     *
     * @return
     */
    void showLoadDialog();

    /**
     * 取消加载弹窗
     *
     * @return
     */
    void dismissProgressDialog();

    /**
     * 提示
     */
    void toast(String msg);
}
