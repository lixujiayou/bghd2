/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bghd.express.utils.zxing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bghd.express.R;
import com.bghd.express.utils.zxing.AudioPlayer;
import com.bghd.express.utils.zxing.SystemUtils;
import com.bghd.express.utils.zxing.camera.CameraManager;
import com.bghd.express.utils.zxing.decode.DecodeThread;
import com.bghd.express.utils.zxing.decode.DecodeUtils;
import com.bghd.express.utils.zxing.utils.CaptureActivityHandler;
import com.bghd.express.utils.zxing.utils.InactivityTimer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.zxing.Result;
import com.bumptech.glide.request.animation.GlideAnimation;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 *
 * 二维码 条形扫描
 */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener {
    private static final String TAG = "CaptureActivity";
    public static final String SCAN_RESULT = "scan_result";
    public static final String SCAN_TYPE = "scan_type";
    private static final int SCAN_CODE_ALBUM_1 = 1;
    private static final int SCAN_CODE_CROP_2 = 2;

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;

    private SurfaceView scanPreview = null;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;
    private Toolbar mToolbar;
    private Rect mCropRect = null;
    private boolean isHasSurface = false;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

   // private ImageButton mTtitleBackIbtn;
    private Button mGenerateBtn;
    private ImageView mLightImv;
    private Button mAlbumBtn;
    private boolean mIsLightOpen;
    private String qrCodePathStr;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);

        inactivityTimer = new InactivityTimer(this);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
                .RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);

        mToolbar.setTitle("SCAN");
        mToolbar.setNavigationIcon(R.drawable.icon_cancle);
        if(mToolbar != null) {
            mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
            mToolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initButton();
    }

    private void initButton() {
//        mTtitleBackIbtn = (ImageButton) findViewById(R.id.scan_title_left_imv);
        mGenerateBtn = (Button) findViewById(R.id.find_scan_generate_btn);
        mLightImv = (ImageView) findViewById(R.id.find_scan_light_btn);
        mAlbumBtn = (Button) findViewById(R.id.find_scan_album_btn);

//        mTtitleBackIbtn.setOnClickListener(CaptureActivity.this);
        mGenerateBtn.setOnClickListener(CaptureActivity.this);
        mLightImv.setOnClickListener(CaptureActivity.this);
        mAlbumBtn.setOnClickListener(CaptureActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(scanPreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            scanPreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
        offLight();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();

        if (rawResult == null) {
            SystemUtils.showHandlerToast(CaptureActivity.this, "未发现二维码/条形码");
        }

//        beepManager.playBeepSoundAndVibrate();
        AudioPlayer.getInstance(this).playRaw(R.raw.scan, false, false);

        operateResult(rawResult);
    }

    private void operateResult(Result rawResult) {
        Log.d("qzzzzzz","rawResult=="+rawResult.toString());
        String codeType = rawResult.getBarcodeFormat().toString();
        String scanResult = rawResult.getText();
        // 二维码
        if ("QR_CODE".equals(codeType) || "DATA_MATRIX".equals(codeType)) {
            boolean isUrl = SystemUtils.checkWebSite(scanResult);
            // 不是标准网址
            if (!isUrl) {
                // 如果是没有添加协议的网址
                if (SystemUtils.checkWebSitePath(scanResult)) {
                    scanResult = "http://" + scanResult;
                    isUrl = true;
                }
            }

            // 扫描结果为网址
            if (isUrl) {
                try {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    Uri uri = Uri.parse(scanResult);
                    intent.setData(uri);
                    ActivityFinish(intent);
                } catch (Exception e) {
                    Log.e(TAG, "handleDecode: " + e.toString());
                    displayResult(scanResult, 0);
                }
            } else {
                Log.d("qzzzzzz","扫描结果为wenzi==");
                displayResult(scanResult, 0);
            }
            // 条形码
        } else if ("EAN_13".equals(codeType)) {
            displayResult(scanResult, 1);
        } else {
            displayResult(scanResult,1);
            //SystemUtils.showShortToast(CaptureActivity.this, "未发现二维码/条形码");
        }
    }

    private void displayResult(String scanResult, int type) {
        if(scanResult == null){

        }else {
            Log.d("qzzzzz", "返回scanResult== " + scanResult);
            Intent intent = new Intent();
            intent.putExtra(SCAN_RESULT, scanResult);
            intent.putExtra(SCAN_TYPE, type);
            setResult(13, intent);
            finish();
        }
    }

    private void ActivityFinish(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.zoomin, 0);
        finish();
        overridePendingTransition(0, 0);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("Camera error");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());

            Log.d(TAG, "getResources().getDimensionPixelSize(x) = " + getResources().getDimensionPixelSize(x));
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 开关灯
     */
    private void operateLight() {
        if (!mIsLightOpen) {
            cameraManager.openLight();
            mIsLightOpen = true;
            mLightImv.setImageResource(R.drawable.light_pressed);
        } else {
            cameraManager.offLight();
            mIsLightOpen = false;
            mLightImv.setImageResource(R.drawable.light_normal);
        }
    }

    /**
     * 关灯
     */
    private void offLight() {
        if (mIsLightOpen) {
            cameraManager.offLight();
            mIsLightOpen = false;
            mLightImv.setImageResource(R.drawable.light_normal);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.find_scan_generate_btn:
                //startActivity(new Intent(CaptureActivity.this, FindGenerateCodeAty.class));
                break;
            case R.id.find_scan_light_btn:
                operateLight();
                break;
            case R.id.find_scan_album_btn:
                offLight();
                Intent intentAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                intentAlbum.addCategory(Intent.CATEGORY_OPENABLE);
                intentAlbum.setType("image/*");
                intentAlbum.putExtra("return-data", true);
                startActivityForResult(intentAlbum, SCAN_CODE_ALBUM_1);
                overridePendingTransition(R.anim.zoomin, 0);
                break;
        }
    }

    /**
     * 从相册返回扫描结果
     *
     * @param uri 图片地址
     */
    private void operateAlbumScanResult(Uri uri) {
        int myWidth = getResources().getDisplayMetrics().widthPixels;
        int myHeight = getResources().getDisplayMetrics().heightPixels;

        Glide.with(CaptureActivity.this)
                .load(uri)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(myWidth, myHeight) {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        Result resultZxing = new DecodeUtils(DecodeUtils.DECODE_DATA_MODE_ALL)
                                .decodeWithZxing(resource);
                        Log.i(TAG, "resultZxing >> " + resultZxing);

                        if (resultZxing != null) {
                            handleDecode(resultZxing, null);
                        } else {
                            SystemUtils.showHandlerToast(CaptureActivity.this,
                                    "未发现二维码/条形码");
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SCAN_CODE_ALBUM_1:
                    // 从uri获取选择相册返回的图片地址
                    Uri uri = data.getData();
                    Log.i(TAG, "uri >> " + uri);
                    operateAlbumScanResult(uri);
                    break;
            }
        }
    }
}
