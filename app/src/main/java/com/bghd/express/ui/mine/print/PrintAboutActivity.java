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



    private PendingIntent mPermissionIntent = null;
    private static HPRTPrinterHelper HPRTPrinter ;
    private static final String ACTION_USB_PERMISSION = "com.HPRTSDKSample";
    private UsbManager mUsbManager = null;
    private UsbDevice device = null;
    private String PrinterName = "HM-A300";//打印机型号
    private BluetoothAdapter mBluetoothAdapter;
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

        HPRTPrinter = new HPRTPrinterHelper(PrintAboutActivity.this, PrinterName);

        mPermissionIntent = PendingIntent.getBroadcast(PrintAboutActivity.this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        PrintAboutActivity.this.registerReceiver(mUsbReceiver, filter);
        EnableBluetooth();
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
                        ConnectType = "Bluetooth";
                        Intent serverIntent = new Intent(PrintAboutActivity.this, BlueToothListActivity.class);
                        startActivityForResult(serverIntent, HPRTPrinterHelper.ACTIVITY_CONNECT_BT);
                        return;
                    }
                } else {
                    //系统不高于6.0直接执行
                    ConnectType = "Bluetooth";
                    Intent serverIntent = new Intent(PrintAboutActivity.this, BlueToothListActivity.class);
                    startActivityForResult(serverIntent, HPRTPrinterHelper.ACTIVITY_CONNECT_BT);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
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
    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                //Toast.makeText(thisCon, "now:"+System.currentTimeMillis(), Toast.LENGTH_LONG).show();
                //HPRTPrinterHelper.WriteLog("1.txt", "fds");
                if (ACTION_USB_PERMISSION.equals(action)) {
                    synchronized (this) {
                        device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if (HPRTPrinterHelper.PortOpen(device) != 0) {
                                HPRTPrinter = null;
                                ToastUtil.showToast(PrintAboutActivity.this,"连接失败",ToastUtil.TOAST_TYPE_ERRO);
                                return;
                            } else
                                ToastUtil.showToast(PrintAboutActivity.this,"连接成功",ToastUtil.TOAST_TYPE_SUCCESS);

                        } else {
                            return;
                        }
                    }
                }
                if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                    device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device != null) {
                        HPRTPrinterHelper.PortClose();
                    }
                }
            } catch (Exception e) {
                Log.e("HPRTSDKSample", (new StringBuilder("Activity_Main --> mUsbReceiver ")).append(e.getMessage()).toString());
            }
        }
    };

    //call back by scan bluetooth printer
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        try {
            String strIsConnected;
            switch (resultCode) {
                case HPRTPrinterHelper.ACTIVITY_CONNECT_BT:
                    int result = data.getExtras().getInt("is_connected");
                    if (result == 0) {
                        ToastUtil.showToast(PrintAboutActivity.this,"连接成功",ToastUtil.TOAST_TYPE_SUCCESS);
                    } else {
                        ToastUtil.showToast(PrintAboutActivity.this,"连接失败",ToastUtil.TOAST_TYPE_ERRO);
                    }
                    break;
                case HPRTPrinterHelper.ACTIVITY_CONNECT_WIFI:
                    String strIPAddress = "";
                    String strPort = "";
                    strIsConnected = data.getExtras().getString("is_connected");
                    if (strIsConnected.equals("NO")) {
                        ToastUtil.showToast(PrintAboutActivity.this,"扫描失败",ToastUtil.TOAST_TYPE_SUCCESS);
                        return;
                    } else {
                        strIPAddress = data.getExtras().getString("IPAddress");
                        strPort = data.getExtras().getString("Port");
                        if (strIPAddress == null || !strIPAddress.contains("."))
                            return;
                        HPRTPrinter = new HPRTPrinterHelper(PrintAboutActivity.this, PrinterName);
                        if (HPRTPrinterHelper.PortOpen("WiFi," + strIPAddress + "," + strPort) != 0)
                            ToastUtil.showToast(PrintAboutActivity.this,"连接失败",ToastUtil.TOAST_TYPE_ERRO);
                        else
                            ToastUtil.showToast(PrintAboutActivity.this,"连接成功",ToastUtil.TOAST_TYPE_SUCCESS);
                        return;
                    }
                case HPRTPrinterHelper.ACTIVITY_PRNFILE:
                    String strPRNFile = data.getExtras().getString("FilePath");
                    HPRTPrinterHelper.PrintBinaryFile(strPRNFile);

                    return;
            }
        } catch (Exception e) {
            Log.e("HPRTSDKSample", (new StringBuilder("Activity_Main --> onActivityResult ")).append(e.getMessage()).toString());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //EnableBluetooth
    private boolean EnableBluetooth() {
        boolean bRet = false;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isEnabled())
                return true;
            mBluetoothAdapter.enable();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!mBluetoothAdapter.isEnabled()) {
                bRet = true;
                Log.d("PRTLIB", "BTO_EnableBluetooth --> Open OK");
            }
        } else {
            Log.d("HPRTSDKSample", (new StringBuilder("Activity_Main --> EnableBluetooth ").append("Bluetooth Adapter is null.")).toString());
        }
        return bRet;
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
        if(mUsbReceiver != null) {
            unregisterReceiver(mUsbReceiver);
        }

    }
}
