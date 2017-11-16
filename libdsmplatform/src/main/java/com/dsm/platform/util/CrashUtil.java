package com.dsm.platform.util;

/**
 * Created by KL60022 on 16-1-21.
 */

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.dsm.platform.BuildConfig;
import com.dsm.platform.util.log.FileUtil;
import com.dsm.platform.util.log.LogUtil;

import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 需要在Application中注册，为了要在程序启动器就监控整个程序。
 */
public class CrashUtil implements Thread.UncaughtExceptionHandler {
	private static final String TAG = "CrashUtil";
	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// CrashUtil实例
	private static CrashUtil instance;
	// 程序的Context对象
	private Context mContext;
	// 用来存储设备信息和异常信息
	private final Map<String, String> infos = new HashMap<>();
	// 用于格式化日期,作为日志文件名的一部分
	private final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH", Locale.US);
    private static final String sd_dir = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/secondlock/error/";

    /** 获取CrashUtil实例 ,单例模式 */
	public static CrashUtil getInstance(Context context) {
		if (instance == null){
			instance = new CrashUtil(context);
		}
		return instance;
	}

	/** 保证只有一个CrashUtil实例 */
	private CrashUtil() {
	}

	private CrashUtil(Context context) {
		init(context);
	}

	/**
	 * 初始化
	 */
	private void init(Context context) {
		mContext = context;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashUtil为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				LogUtil.e(TAG, "error : " +  e);
			}
			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
			// 退出程序
			// ActivityManager am =(ActivityManager)
			// mContext.getSystemService(Context.ACTIVITY_SERVICE);
			// am.killBackgroundProcesses(mContext.getPackageName());
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		collectDeviceInfo();
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "app_error_tip", Toast.LENGTH_SHORT)
						.show();
				Looper.loop();
			}
		}.start();
		// 保存日志文件
		saveCatchInfo2File(ex);
		return true;
	}

	/**
	 * 手机设备信息
	 */
	private void collectDeviceInfo() {
		infos.put("appsoftversion", BuildConfig.VERSION_NAME);
		infos.put("phonename", android.os.Build.BRAND);
		infos.put("phonetype", android.os.Build.MODEL + ","
				+ android.os.Build.VERSION.SDK_INT);
		infos.put("phone", SharedPreferencesUtil.getString(mContext,"username",""));
		infos.put("time",new SimpleDateFormat("yyyyMMdd HH:mm:ss", Locale.US).format(new Date()));
	}

	public static String getFilePath() {
		String file_dir;
		// SD卡是否存在
		boolean isSDCardExist = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
		// Environment.getExternalStorageDirectory()相当于File file=new
		// File("/sdcard")
		boolean isRootDirExist = Environment.getExternalStorageDirectory()
				.exists();
		if (isSDCardExist && isRootDirExist) {
			file_dir = sd_dir;
		} else {
			// MyApplication.getInstance().getFilesDir()返回的路劲为/data/data/PACKAGE_NAME/files，其中的包就是我们建立的主Activity所在的包
            String dir = "/secondlock/error/";
            file_dir = dir;
		}
		return file_dir;
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCatchInfo2File(Throwable ex) {
		try {
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : infos.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				sb.append(key).append("=").append(value).append("\n");
			}

			Writer writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			ex.printStackTrace(printWriter);
			Throwable cause = ex.getCause();
			while (cause != null) {
				cause.printStackTrace(printWriter);
				cause = cause.getCause();
			}
			printWriter.close();
			String result = writer.toString();
			sb.append(result);

			String time = formatter.format(new Date());
			String fileName = "dsm-" + time + ".log";
            String path = getFilePath();
			infos.put("bugcontent", sb.toString());
			JSONObject jsonObject = new JSONObject(infos);
			LogUtil.i("crash",jsonObject.toString());
			FileUtil.writeFile(path + fileName, jsonObject.toString(),false);
			return fileName;
		} catch (Exception e) {
			LogUtil.e(TAG, "an error occured while writing file..." + e);
		}
		return null;
	}
}
