package com.bghd.express.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.bghd.express.R;
import com.bghd.express.utils.base.BaseActivity;
import com.bghd.express.utils.tools.BitmapUtils;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;

import static com.bghd.express.utils.tools.SDUtils.assets2SD;

/**
 * Created by lixu on 2018/2/11.
 */

public class DiscernDemo extends BaseActivity {
    private String TAG = "DiscernDemo";

    /**
     * TessBaseAPI初始化用到的第一个参数，是个目录。
     */
    private static final String DATAPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    /**
     * 在DATAPATH中新建这个目录，TessBaseAPI初始化要求必须有这个目录。
     */
    private static final String tessdata = DATAPATH + File.separator + "tessdata";
    /**
     * TessBaseAPI初始化测第二个参数，就是识别库的名字不要后缀名。
     */
    private static String DEFAULT_LANGUAGE = "chi_sim";
    /**
     * assets中的文件名
     */
    private static String DEFAULT_LANGUAGE_NAME = DEFAULT_LANGUAGE + ".traineddata";
    /**
     * 保存到SD卡中的完整文件名
     */
    private static String LANGUAGE_PATH = tessdata + File.separator + DEFAULT_LANGUAGE_NAME;

    /**
     * 权限请求值
     */
    private static final int PERMISSION_REQUEST_CODE = 0;

    private static final int PICK_REQUEST_CODE = 10;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.a_discern_demo);
    }

    @Override
    public void initViews() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }

        //Android6.0之前安装时就能复制，6.0之后要先请求权限，所以6.0以上的这个方法无用。

        assets2SD(getApplicationContext(), LANGUAGE_PATH, DEFAULT_LANGUAGE_NAME);

        findViewById(R.id.bt_shibie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recognition(BitmapFactory.decodeResource(getResources(), R.drawable.iv_test));
            }
        });
    }

    @Override
    public void initData() {

    }

    public boolean checkTraineddataExists() {
        File file = new File(LANGUAGE_PATH);
        return file.exists();
    }

    /**
     * 识别图像
     *
     * @param bitmap
     */
    private void recognition(final Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String text = "";
                if (!checkTraineddataExists()) {
                    Log.i(TAG, "run: " + LANGUAGE_PATH + "不存在，开始复制\r\n");
                    assets2SD(getApplicationContext(), LANGUAGE_PATH, DEFAULT_LANGUAGE_NAME);
                }


                TessBaseAPI tessBaseAPI = new TessBaseAPI();
                tessBaseAPI.init(DATAPATH, DEFAULT_LANGUAGE);
                tessBaseAPI.setImage(bitmap);
                text = tessBaseAPI.getUTF8Text();
                Log.i(TAG, "run: text " + text);
                final String finalText = text;
                final Bitmap finalBitmap = bitmap;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "识别文字==" + finalText);
                    }
                });
                tessBaseAPI.end();
            }
        }).start();
    }

    /**
     * 请求到权限后在这里复制识别库
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionsResult: " + grantResults[0]);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "onRequestPermissionsResult: copy");
                    assets2SD(getApplicationContext(), LANGUAGE_PATH, DEFAULT_LANGUAGE_NAME);
                }
                break;
            default:
                break;
        }
    }

}
