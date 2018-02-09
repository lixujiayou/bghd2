package com.bghd.express.utils.zxing.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.bghd.express.R;
import com.bghd.express.utils.zxing.AbsentMConstants;
import com.bghd.express.utils.zxing.FileUtil;
import com.bghd.express.utils.zxing.SystemUtils;
import com.bghd.express.utils.zxing.encoding.EncodingUtils;


/**
 * FindGenerateCodeAty
 * Created by dm on 16-9-13.
 */
public class MyFindGenerateCodeAty extends AppCompatActivity implements View.OnClickListener, ColorChooserDialog.ColorCallback {
    private static final String LOG_TAG = "FindGenerateCodeAty";
    private static final int REQUEST_CODE_ALBUM_1 = 1;
    private static final int REQUEST_CODE_CROP_2 = 2;
    private static final String STATIONID = "type";

    private ImageButton mBackIbtn;
    private ImageButton mAddSettingsIbtn;
    private EditText mQrCodeInfoEt;
    private ImageView mQrLogoImv;
    private Button mGenereteBtn;
    private ImageView mGenerateQrImv;
    private TextView tvTitle;

    private int mForeColor;     // 二维码前景色：默认黑色
    private int mBackColor;     // 二维码背景色：默认白色
    private String mLogoPathStr;    // 二维码片保存地址
    private boolean isQRcodeGenerated;  // 是否生成
    private boolean isBARcodeGenerated;  // 是否生成

    private String qrSettingsItemStr;
    private int mPalette = 0;       // 调色板使用状态：0，前景色；1，背景色

    private String mStationId;
    private String mType;
    private String mSubId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_layout);

        Intent gIntent = getIntent();
        mType = gIntent.getExtras().getString("type");
        mSubId = gIntent.getExtras().getString("sub_id","");
        mStationId = gIntent.getExtras().getString(STATIONID,"");

        initQrBaseValues();
        initView();
        if(mType.equals("yi")){
            tvTitle.setText("BAR Code");
            generateBARcode();
        }else {
            tvTitle.setText("QR Code");
            generateQRcode();

        }
    }

    /**
     * 获取保存的自定义二维码logo地址、前景色、背景色
     */
    private void initQrBaseValues() {
        SharedPreferences share = getSharedPreferences(
                AbsentMConstants.EXTRA_ABSENTM_SHARE, Activity.MODE_PRIVATE);
        mLogoPathStr = share.getString(AbsentMConstants.QRCODE_LOGO_PATH, null);
        mForeColor = share.getInt(AbsentMConstants.FORE_COLOR, 0xff000000);
        mBackColor = share.getInt(AbsentMConstants.BACK_COLOR, 0xffffffff);
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.generate_title_center_tv);
        mBackIbtn = (ImageButton) findViewById(R.id.generate_title_left_ibtn);
        mAddSettingsIbtn = (ImageButton) findViewById(R.id.generate_title_right_ibtn);
        mQrCodeInfoEt = (EditText) findViewById(R.id.generate_qr_code_et);
        mQrLogoImv = (ImageView) findViewById(R.id.generate_logo_imv);
        mGenereteBtn = (Button) findViewById(R.id.generate_qr_code_btn);
        mGenerateQrImv = (ImageView) findViewById(R.id.generate_result_imv);

        if (mLogoPathStr != null) {
            mQrLogoImv.setImageBitmap(BitmapFactory.decodeFile(mLogoPathStr));
        }



        mBackIbtn.setOnClickListener(MyFindGenerateCodeAty.this);
        mAddSettingsIbtn.setOnClickListener(MyFindGenerateCodeAty.this);
        mGenereteBtn.setOnClickListener(MyFindGenerateCodeAty.this);
        mGenereteBtn.setClickable(false);

        mQrCodeInfoEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    mGenereteBtn.setClickable(false);
                    mGenereteBtn.setBackgroundResource(R.drawable.item_teal_selector);
                    mGenereteBtn.setTextColor(getResources().getColor(R.color.gray700));
                } else {
                    mGenereteBtn.setClickable(true);
                    mGenereteBtn.setBackgroundResource(R.drawable.item_teal_selector);
                    mGenereteBtn.setTextColor(getResources().getColor(R.color.white));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (isQRcodeGenerated) {
            mGenerateQrImv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final MaterialDialog materialDialog = new MaterialDialog
                            .Builder(MyFindGenerateCodeAty.this)
                            .positiveColorRes(R.color.teal)
                            .positiveText("OK")
                            .negativeColorRes(R.color.teal)
                            .negativeText("CANCEL")
                            .content("SAVE？")
                            .show();

                    View positiveBtn = materialDialog.getActionButton(DialogAction.POSITIVE);
                    positiveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bitmap bitmap = FileUtil.imageView2Bitmap(
                                    MyFindGenerateCodeAty.this, mGenerateQrImv);

                            String savePath = FileUtil.saveBitmapToJpg(
                                    MyFindGenerateCodeAty.this, bitmap);

                            SystemUtils.showShortToast(MyFindGenerateCodeAty.this,
                                    "Save\tto-> " + savePath);
                            materialDialog.dismiss();
                        }
                    });

                    View negativeBtn = materialDialog.getActionButton(DialogAction.NEGATIVE);
                    negativeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            materialDialog.cancel();
                        }
                    });

                    return true;
                }
            });
        }

        if (isBARcodeGenerated) {
            mGenerateQrImv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final MaterialDialog materialDialog = new MaterialDialog
                            .Builder(MyFindGenerateCodeAty.this)
                            .positiveColorRes(R.color.teal)
                            .positiveText("OK")
                            .negativeColorRes(R.color.teal)
                            .negativeText("CANCEL")
                            .content("SAVE？")
                            .show();

                    View positiveBtn = materialDialog.getActionButton(DialogAction.POSITIVE);
                    positiveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bitmap bitmap = FileUtil.imageView2Bitmap(
                                    MyFindGenerateCodeAty.this, mGenerateQrImv);

                            String savePath = FileUtil.saveBitmapToJpg(
                                    MyFindGenerateCodeAty.this, bitmap);

                            SystemUtils.showShortToast(MyFindGenerateCodeAty.this,
                                    "Save\tto-> " + savePath);
                            materialDialog.dismiss();
                        }
                    });

                    View negativeBtn = materialDialog.getActionButton(DialogAction.NEGATIVE);
                    negativeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            materialDialog.cancel();
                        }
                    });

                    return true;
                }
            });
        }
    }

    /**
     * 生成二维码片
     */
    private void generateQRcode() {
        String contentString = "Site ID:"+mStationId+";Site Name:"+mSubId;
        Log.d("qzzzzzzz","生成的二维码=="+contentString);
        Bitmap logoBitmap = null;
        int size = SystemUtils.dip2px(this, 320);
        //根据字符串生成二维码图片并显示在界面上，第2, 3个参数为图片宽高
        Bitmap qrCodeBitmap = EncodingUtils.createQRCode(contentString,
                size, size, logoBitmap, mForeColor, mBackColor);


        mGenerateQrImv.setImageBitmap(qrCodeBitmap);
        isQRcodeGenerated = true;

        mGenerateQrImv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final MaterialDialog materialDialog = new MaterialDialog
                        .Builder(MyFindGenerateCodeAty.this)
                        .positiveColorRes(R.color.teal)
                        .positiveText("OK")
                        .negativeColorRes(R.color.teal)
                        .negativeText("CANCEL")
                        .content("Saved to the local？")
                        .show();

                View positiveBtn = materialDialog.getActionButton(DialogAction.POSITIVE);
                positiveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bitmap bitmap = FileUtil.imageView2Bitmap(
                                MyFindGenerateCodeAty.this, mGenerateQrImv);

                        String savePath = FileUtil.saveBitmapToJpg(
                                MyFindGenerateCodeAty.this, bitmap);

                        SystemUtils.showShortToast(MyFindGenerateCodeAty.this,
                                "save to -> " + savePath);
                        materialDialog.dismiss();
                    }
                });

                View negativeBtn = materialDialog.getActionButton(DialogAction.NEGATIVE);
                negativeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialDialog.cancel();
                    }
                });
                return true;
            }
        });
    }
    //生成条形码
    private void generateBARcode() {
        String contentString = "";
        if(mSubId==null || mSubId.isEmpty()){
            contentString = mStationId;
        }else{
            contentString = mSubId;
        }
        Log.d("qzzzzzzz","生成的条形码=="+contentString);
        Bitmap logoBitmap = null;
        int size = SystemUtils.dip2px(this, 320);
        //根据字符串生成二维码图片并显示在界面上，第2, 3个参数为图片宽高
        Bitmap qrCodeBitmap =  EncodingUtils.creatBarcode(
                getApplicationContext(), contentString, size, size, false);

        /*Bitmap qrCodeBitmap = EncodingUtils.createQRCode(contentString,
                size, size, logoBitmap, mForeColor, mBackColor);*/


        mGenerateQrImv.setImageBitmap(qrCodeBitmap);
        isBARcodeGenerated = true;

        mGenerateQrImv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final MaterialDialog materialDialog = new MaterialDialog
                        .Builder(MyFindGenerateCodeAty.this)
                        .positiveColorRes(R.color.teal)
                        .positiveText("OK")
                        .negativeColorRes(R.color.teal)
                        .negativeText("CANCEL")
                        .content("Saved to the local？")
                        .show();

                View positiveBtn = materialDialog.getActionButton(DialogAction.POSITIVE);
                positiveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bitmap bitmap = FileUtil.imageView2Bitmap(
                                MyFindGenerateCodeAty.this, mGenerateQrImv);

                        String savePath = FileUtil.saveBitmapToJpg(
                                MyFindGenerateCodeAty.this, bitmap);

                        SystemUtils.showShortToast(MyFindGenerateCodeAty.this,
                                "save to -> " + savePath);
                        materialDialog.dismiss();
                    }
                });

                View negativeBtn = materialDialog.getActionButton(DialogAction.NEGATIVE);
                negativeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialDialog.cancel();
                    }
                });
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.generate_title_left_ibtn:
                MyFindGenerateCodeAty.this.finish();
                break;
            case R.id.generate_title_right_ibtn:
                operateQrSettins();
                break;
            case R.id.generate_qr_code_btn:
                generateQRcode();
                break;
        }
    }

    /**
     * 二维码片设置
     */
    private void operateQrSettins() {
        new MaterialDialog.Builder(MyFindGenerateCodeAty.this)
                .title("二维码片设置")
                .items(R.array.qr_settings_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog,
                                            View itemView,
                                            int position,
                                            CharSequence text) {

                        qrSettingsItemStr = (String) text;
                        Log.d(LOG_TAG, "qrSettingsItemStr >>> " + qrSettingsItemStr);

                        dealChooseItemEvent(qrSettingsItemStr);
                    }
                }).show();
    }

    /**
     * 处理设置条目事件
     *
     * @param itemStr
     */
    private void dealChooseItemEvent(String itemStr) {
        switch (itemStr) {
            case "从相册选择Logo":
                Intent intentAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                intentAlbum.addCategory(Intent.CATEGORY_OPENABLE);
                intentAlbum.setType("image/*");
                intentAlbum.putExtra("return-data", true);
                startActivityForResult(intentAlbum, REQUEST_CODE_ALBUM_1);
                break;
            case "保存二维码至手机":
                if (isQRcodeGenerated) {
                    Bitmap bitmap = FileUtil.imageView2Bitmap(
                            MyFindGenerateCodeAty.this, mGenerateQrImv);

                    String savePath = FileUtil.saveBitmapToJpg(
                            MyFindGenerateCodeAty.this, bitmap);

                    SystemUtils.showShortToast(MyFindGenerateCodeAty.this,
                            "保存至 -> " + savePath);
                } else {
                    SystemUtils.showShortToast(MyFindGenerateCodeAty.this,
                            "请先制作二维码片");
                }

                break;
            case "二维码前景色":
                mPalette = 0;
                new ColorChooserDialog.Builder(MyFindGenerateCodeAty.this, R.string.fore_color)
                        .titleSub(R.string.colors)
                        .doneButton(R.string.sure)
                        .cancelButton(R.string.cancel)
                        .backButton(R.string.back)
                        .customButton(R.string.custom_define)
                        .presetsButton(R.string.back)
                        .show(MyFindGenerateCodeAty.this);
                break;
            case "二维码背景色":
                mPalette = 1;
                new ColorChooserDialog.Builder(MyFindGenerateCodeAty.this, R.string.back_color)
                        .titleSub(R.string.colors)
                        .doneButton(R.string.sure)
                        .cancelButton(R.string.cancel)
                        .backButton(R.string.back)
                        .customButton(R.string.custom_define)
                        .presetsButton(R.string.back)
                        .show(MyFindGenerateCodeAty.this);
                break;
            case "恢复默认":
                final MaterialDialog materialDialog = new MaterialDialog.Builder(MyFindGenerateCodeAty.this)
                        .title(R.string.restore)
                        .content(R.string.reset_logo_color)
                        .positiveText(R.string.sure)
                        .positiveColorRes(R.color.teal)
                        .negativeText(R.string.cancel)
                        .negativeColorRes(R.color.teal)
                        .show();

                View positiveBtn = materialDialog.getActionButton(DialogAction.POSITIVE);
                positiveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mLogoPathStr = null;
                        mForeColor = 0xff000000;
                        mBackColor = 0xffffffff;
                        mQrLogoImv.setImageResource(R.mipmap.ic_launcher);

                        if (isQRcodeGenerated) {
                            generateQRcode();
                        }

                        SharedPreferences share = getSharedPreferences(
                                AbsentMConstants.EXTRA_ABSENTM_SHARE, Activity.MODE_PRIVATE);
                        SharedPreferences.Editor edit = share.edit();
                        edit.putString(AbsentMConstants.QRCODE_LOGO_PATH, null);
                        edit.putInt(AbsentMConstants.FORE_COLOR, 0xff000000);
                        edit.putInt(AbsentMConstants.BACK_COLOR, 0xffffffff);
                        edit.apply();

                        materialDialog.dismiss();
                    }
                });

                View negativeBtn = materialDialog.getActionButton(DialogAction.NEGATIVE);
                negativeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialDialog.cancel();
                    }
                });
                break;
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");  // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("aspectX", 1);   // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);  // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputY", 300);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_CODE_CROP_2);
    }

    /**
     * 保存裁剪之后的图片数据,并更改二维码样式
     *
     * @param picdata intent
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        Log.i(LOG_TAG, "extras.toString >> " + extras.toString());

        Bitmap qrLogoBitmap = extras.getParcelable("data");
        mQrLogoImv.setImageBitmap(qrLogoBitmap);

        if (isQRcodeGenerated) {
            generateQRcode();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ALBUM_1:
                    try {
                        // 从uri获取选择相册返回的图片地址
                        Uri uri = data.getData();
                        String path = uri.getEncodedPath();
                        Log.i(LOG_TAG, "path1 >> " + path);

                        if (path != null) {
                            path = uri.getPath();
                            Log.i(LOG_TAG, "path2 >> " + path);
                        }

                        mLogoPathStr = path;
                        startPhotoZoom(data.getData());
                    } catch (NullPointerException e) {
                        Log.i(LOG_TAG, e.getMessage());
                    }

                    break;
                case REQUEST_CODE_CROP_2:   // 取得裁剪后的图片
                    if (data != null) {
                        setPicToView(data);
                        Log.i(LOG_TAG, ">> " + REQUEST_CODE_CROP_2);
                    }

                    break;
            }
        }
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog,
                                 @ColorInt int selectedColor) {

        Log.d(LOG_TAG, "onColorSelection: " + selectedColor);
        switch (mPalette) {
            // 前景色
            case 0:
                mForeColor = selectedColor;
                operateColor(selectedColor, AbsentMConstants.FORE_COLOR);
                break;
            // 背景色
            case 1:
                mBackColor = selectedColor;
                operateColor(selectedColor, AbsentMConstants.BACK_COLOR);
                break;
        }

    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {

    }

    /**
     * 选择颜色，设置前景色和背景色
     *
     * @param selectedColor
     * @param saveColorKey
     */
    private void operateColor(int selectedColor, String saveColorKey) {
        if (isQRcodeGenerated) {
            generateQRcode();
        }

        SharedPreferences share = getSharedPreferences(
                AbsentMConstants.EXTRA_ABSENTM_SHARE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = share.edit();
        edit.putInt(saveColorKey, selectedColor);
        edit.apply();
    }
}
