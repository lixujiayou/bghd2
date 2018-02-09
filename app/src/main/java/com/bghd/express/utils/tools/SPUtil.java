package com.bghd.express.utils.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lixu on 2017/8/8.
 */

public class SPUtil {
    public static String USER = "user";

    public static String USER_ISREMEMBER = "is_remember";
    public static String USER_NAME = "user_name";
    public static String USER_PHONE = "user_phone";
    public static String USER_PWD = "user_pwd";
    public static String USER_UID = "user_uid";

    public static String USER_TOKEN = "user_token";

    public static String TAG = "tag";
    public static String TAG_LOGIN = "tag_login";
    public static String TAG_METER = "tag_meter";




    public static String LOCATION_LAT = "lat";
    public static String LOCATION_LON = "lon";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SPUtil(Context context, String fileName) {
        preferences = context.getSharedPreferences(fileName, context.MODE_PRIVATE);
        editor = preferences.edit();
    }


    /**
     * 向SP存入指定key对应的数据
     * 其中value可以是String、boolean、float、int、long等各种基本类型的值
     * @param key
     * @param value
     */
    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 清空SP里所以数据
     */
    public void clear() {
        editor.clear();
        editor.commit();
    }

    /**
     * 删除SP里指定key对应的数据项
     * @param key
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 获取SP数据里指定key对应的value。如果key不存在，则返回默认值defValue。
     * @param key
     * @param defValue
     * @return
     */
    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    /**
     * 判断SP是否包含特定key的数据
     * @param key
     * @return
     */
    public boolean contains(String key){
        return preferences.contains(key);
    }

}