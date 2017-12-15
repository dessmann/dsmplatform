package com.dsm.platform;

import android.app.Application;
import android.text.TextUtils;

import com.dsm.platform.base.BaseMsgCode;
import com.dsm.platform.base.User;
import com.dsm.platform.util.log.LogUtil;
import com.umeng.analytics.MobclickAgent;
import com.yolanda.nohttp.NoHttp;

import net.tsz.afinal.FinalDb;

/**
 * Created by Administrator on 2016/12/12.
 * 库加载初始化，在app启动时调用
 */

public class DsmLibrary {

    private static final String TAG = "DsmLibrary";
    private static DsmLibrary dsmLibrary;
    public static final int DefaultDeviceUserCapacity = 150;//默认的设备用户容量
    public static final int DefaultDeviceFingerCapacity = 80;//默认的设备指纹容量
    public static final int DefaultDeviceLocalFingerCapacity = 20;//默认的设备本地指纹容量
    public static final int MaskGuideType_700 = 1;//700蓝牙蒙版样式
    public static final int MaskGuideType_820 = 2;//820蓝牙蒙版样式
    public static final int MaskGuideType_510 = 3;//510蓝牙蒙版样式
    private static boolean inited = false;//是否已经初始化
    public static String SPC_CURRENT_MAC;//存储当前设备信息的本地sharedPerference键名称
    public static String FILE_CURRENT_USER;//存储当前用户信息的本地文件路径

    public static DsmLibrary getInstance() {
        if (dsmLibrary == null) {
            synchronized (DsmLibrary.class) {
                if (dsmLibrary == null) {
                    dsmLibrary = new DsmLibrary();
                }
            }
        }
        return dsmLibrary;
    }

    public static Application application;
    private FinalDb finalDb;
    private User user;

    public synchronized void init(Application application, FinalDb finalDb, boolean enableConsoleLog, boolean enableFileLog, String logPath, String logFileName, String releaseLogPath) {
        if (!inited) {//是否已经初始化作同步管理，避免多次被初始化
            if (application == null) {
                throw new IllegalArgumentException("application is null");
            }
            inited = true;
            this.finalDb = finalDb;
            //接收应用程序上下文参数
            DsmLibrary.application = application;
            new BaseMsgCode();
            //初始化网络请求框架
            NoHttp.initialize(application);
            //UMeng统计
            MobclickAgent.setScenarioType(application, MobclickAgent.EScenarioType.E_UM_NORMAL);
            //日志配置
            initLogConfig(enableConsoleLog, enableFileLog, logPath, logFileName, releaseLogPath);
            //
            FILE_CURRENT_USER = application.getFilesDir() + "data/" + application.getPackageName() + "/cache/current_user_info.cache";
            LogUtil.e(TAG, "============德施曼基础模块加载完成===============");
        }
    }

    private void initLogConfig(boolean enableConsoleLog, boolean enableFileLog, String logPath, String logFileName, String releaseLogPath) {
        LogUtil.LOG_SWITCH = enableConsoleLog;//日志开关
        LogUtil.LOG_WRITE_TO_FILE = enableFileLog;//日志写入SD卡文件开关，若为false则将日志写入应用程序内部私有空间
        LogUtil.LOG_FILEPATH = logPath;//输出的日志在sd卡上的存储路径
        if (!TextUtils.isEmpty(logFileName)) {
            LogUtil.LOG_FILENAME = logFileName;//日志文件的名称
        }
        LogUtil.LOG_FILEPATH_RELEASE = releaseLogPath;//输出的日志在应用程序内部私有空间的存储路径
        LogUtil.delFile();
    }

    public FinalDb getFinalDb() {
        return finalDb;
    }

    public User getUser() {
        return user;
    }

    public DsmLibrary setUser(User user) {
        this.user = user;
        return this;
    }
}
