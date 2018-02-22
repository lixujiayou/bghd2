package com.bghd.express.ui.mine.print;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.bghd.express.R;
import com.bghd.express.utils.base.BaseActivity;
import com.bghd.express.utils.bluetooth.checkClick;
import com.bghd.express.utils.tools.ToastUtil;

import HPRTAndroidSDKA300.HPRTPrinterHelper;

/**
 * Created by lixu on 2018/2/10.
 * 连接打印机
 */

public class PrintAboutActivity extends BaseActivity{

    private LinearLayout llConnect,llBreak;

    private static HPRTPrinterHelper HPRTPrinter = new HPRTPrinterHelper();



    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_print_about);
    }

    @Override
    public void initViews() {
        llConnect = findViewById(R.id.ll_print_connect);
        llBreak = findViewById(R.id.ll_print_break);
        llConnect.setOnClickListener(this);
        llBreak.setOnClickListener(this);


        mToolbar.setTitle("关于打印机");
        mToolbar.setNavigationIcon(R.drawable.icon_cancle);


    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.ll_print_connect:
                connectBlueT();
                break;
            case R.id.ll_print_break:
                breakConnectBT();
                break;
        }

    }
    private String ConnectType = "";
    private void connectBlueT(){
        if (!checkClick.isClickEvent()) return;
        if (HPRTPrinter != null) {
            try {
                HPRTPrinterHelper.PortClose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (Build.VERSION.SDK_INT >= 23) {
            //校验是否已具有模糊定位权限
            if (ContextCompat.checkSelfPermission(PrintAboutActivity.this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(PrintAboutActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        100);
            } else {
                //具有权限
                Intent serverIntent = new Intent(PrintAboutActivity.this, Activity_DeviceList.class);
                startActivityForResult(serverIntent, HPRTPrinterHelper.ACTIVITY_CONNECT_BT);
                return;
            }
        } else {
            //系统不高于6.0直接执行
            Intent serverIntent = new Intent(PrintAboutActivity.this, Activity_DeviceList.class);
            startActivityForResult(serverIntent, HPRTPrinterHelper.ACTIVITY_CONNECT_BT);
        }
    }



    private void breakConnectBT(){
        if (!checkClick.isClickEvent()) return;

        try {
            if (HPRTPrinter != null) {
                HPRTPrinterHelper.PortClose();
            }
            ToastUtil.showToast(PrintAboutActivity.this,"已成功断开打印机",ToastUtil.TOAST_TYPE_WARNING);
            return;
        } catch (Exception e) {
            Log.e("HPRTSDKSample", (new StringBuilder("Activity_Main --> onClickClose ")).append(e.getMessage()).toString());
        }
    }


    //call back by scan bluetooth printer
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case HPRTPrinterHelper.ACTIVITY_CONNECT_BT:
                int result = data.getExtras().getInt("is_connected");
                Log.d("qqqqq","result=="+result);
                if (result == 0) {
                    ToastUtil.showToast(PrintAboutActivity.this,"连接成功",ToastUtil.TOAST_TYPE_SUCCESS);
                } else {
                    ToastUtil.showToast(PrintAboutActivity.this,"连接失败",ToastUtil.TOAST_TYPE_ERRO);
                }
                break;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            HPRTPrinterHelper.PortClose();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        if(mUsbReceiver != null) {
//            unregisterReceiver(mUsbReceiver);
//        }

    }
}
