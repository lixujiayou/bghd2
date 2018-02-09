package com.bghd.express.core;

import android.content.Context;
import android.os.Environment;

import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lixu on 2017/5/12.
 */

public class Constance {

    public static final int REQUEST_SUCCESS_CODE = 1;
    public static final int REQUEST_OTHER_CODE = 2;
    public static final int REQUEST_ERRO_CODE = 1;

    public static final String REQUEST_DOWN_ORDER = "down";
    public static final String REQUEST_ON_ORDER = "up";

    //订单第一次加载条数
    public static final int  ORDER_First_NUM = 20;


    // 文件分隔符
    public static final String FILE_SEPARATOR = "/";

    // 外存sdcard存放路径
    public static final String FILE_PATH = Environment.getExternalStorageDirectory()
            + FILE_SEPARATOR
            +"bibleAsk" + FILE_SEPARATOR;
    public static final String FILENAME = "autoapk.apk";
    public static final String FILE_NAME = FILE_PATH + FILENAME;


    public static final List<String> chooseList = Arrays.asList("传输内线数据关联性指标","传输内线字段必填性指标","传输内线业务逻辑合理性指标");


    /**
     * 获取状态栏的高度
     * @return
     */
    public static int getStatusBarHeight(Context mContext){
        try
        {
            Class<?> c= Class.forName("com.android.internal.R$dimen");
            Object obj=c.newInstance();
            Field field=c.getField("status_bar_height");
            int x= Integer.parseInt(field.get(obj).toString());
            return  mContext.getResources().getDimensionPixelSize(x);
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

     public static String getMsgByCode(int Code){
         if(Code == 500){
             return "服务器异常";
         }else if(Code == 400){
             return "请求错误";
         }else if(Code == 401){
             return "登录过期，请重新登录";
         }else if(Code == 404){
             return "操作异常，请联系网络管理员";
         }
         return String.valueOf(Code);
     }

      public static String getMsgByException(Throwable t){
        if(t instanceof SocketTimeoutException){
            return "连接超时，请检查网络";
        }else if(t instanceof ConnectException){
            return "网络连接错误";
        }else if(t instanceof SocketException){
            return "请求被关闭";
        }else if(t.getMessage().toString().contains("Canceled")){
            return "请求被关闭";
        }
        return t.getMessage().toString();
    }
}
