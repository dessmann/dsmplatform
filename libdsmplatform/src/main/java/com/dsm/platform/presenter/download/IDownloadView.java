package com.dsm.platform.presenter.download;

/**
 * Created by dccjll on 2017/3/24.
 * 通用的下载视图接口
 */

public interface IDownloadView {
    String getDownloadUrl();//要下载的文件的url地址
    String getLocalStorageFilePath();//下载的文件在本地的存储路径
    String getLocalStorageFileName();//下载的文件在本地的存储名称
    void onDownloadSuccess(String wholePathWithFileName);//下载成功，返回下载的文件在本地的完整存储路径
    void onDownloadFailure(String usefulErrorMsg);//下载失败
}
