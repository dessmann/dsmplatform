package com.dsm.platform.util;

import com.dsm.platform.util.log.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化工具
 */

public class SerializableUtil {

    private static final String TAG = "SerializableUtil";

    /**
     * 将对象序列化到文件
     */
    public static void serializeObjectToFile(Object object, String path){
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.createNewFile()) {
                LogUtil.e(TAG, "创建文件失败");
                return;
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(object);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件中恢复对象
     */
    public static Object deSerializeObjectFromFile(String path){
        try {
            ObjectInputStream ois =  new ObjectInputStream(new FileInputStream(path));
            try {
                return ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
