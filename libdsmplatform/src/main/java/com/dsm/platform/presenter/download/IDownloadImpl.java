package com.dsm.platform.presenter.download;

import android.content.Context;
import android.util.Log;

import com.dsm.platform.R;
import com.dsm.platform.base.ServerUtil;
import com.dsm.platform.util.log.LogUtil;
import com.dsm.platform.util.net.NoHttpUtil;

import java.io.File;

/**
 * Created by dccjll on 2017/3/24.
 * 通用的下载
 */

public class IDownloadImpl implements IDownload {

    private static final String TAG = IDownloadImpl.class.getSimpleName();
    private final Context context;
    private final IDownloadView iDownloadView;

    public IDownloadImpl(Context context, IDownloadView iDownloadView) {
        this.context = context;
        this.iDownloadView = iDownloadView;
    }

    @Override
    public void download() {
        String filePath = iDownloadView.getLocalStorageFilePath();
        String fileName = iDownloadView.getLocalStorageFileName();
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        String wholePath = filePath + fileName;
        if (new File(wholePath).exists()) {
            LogUtil.i(TAG, "本地已存在要下载的文件，忽略下载，直接从本地读取");
            iDownloadView.onDownloadSuccess(wholePath);
            return;
        }
        ServerUtil.downloadFile(
                iDownloadView.getDownloadUrl(),
                iDownloadView.getLocalStorageFilePath(),
                iDownloadView.getLocalStorageFileName(),
                new NoHttpUtil.DownloadResponseListener() {
                    @Override
                    public void onProgress(int progress) {
                        LogUtil.i(TAG, "正在下载文件，url=" + iDownloadView.getDownloadUrl() + "\n当前进度：" + progress);
                    }

                    @Override
                    public void onSuccess(final String filePath) {
                        iDownloadView.onDownloadSuccess(filePath);
                    }

                    @Override
                    public void onFailure(final String msg, final int loglever) {
                        iDownloadView.onDownloadFailure(loglever == Log.WARN ? msg : context.getString(R.string.download_failure_please_check_network));
                    }
                }
        );
    }
}
