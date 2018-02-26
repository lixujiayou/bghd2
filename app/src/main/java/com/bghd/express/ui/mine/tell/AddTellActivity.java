package com.bghd.express.ui.mine.tell;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bghd.express.R;
import com.bghd.express.adapter.MyImgAdapter;
import com.bghd.express.entiy.SaveOrderEntity;
import com.bghd.express.entiy.TellEntity;
import com.bghd.express.model.AddTellModel;
import com.bghd.express.model.ChangeTellModel;
import com.bghd.express.model.ImageFactoryModel;
import com.bghd.express.model.RemoveTellModel;
import com.bghd.express.ui.ImageLookActivity;
import com.bghd.express.ui.order.AdressListActivity;
import com.bghd.express.utils.base.BaseActivity;
import com.bghd.express.utils.base.DeletableEditText;
import com.bghd.express.utils.tools.ImgUtils;
import com.bghd.express.utils.tools.StringUtils;
import com.bghd.express.utils.tools.ToastUtil;
import com.bghd.express.utils.tools.ToolUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.oginotihiro.cropview.CropView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import static com.bghd.express.utils.tools.SDUtils.assets2SD;

/**
 * Created by lixu on 2018/2/11.
 */

public class AddTellActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {
    private String TAG = "AddTellActivity";
    public static final int FINISH_RESH_CODE = 199;//返回刷新


    public static String TELL_TYPE = "type";  //0:寄件  1：收件
    public static String TELL_TYPE_SEND = "0"; //寄件
    public static String TELL_TYPE_ACCPET = "1"; //收件

    public static String TELL_STATUS = "status";  //0:编辑  1：新增
    public static String TELL_STATUS_EDIT = "0";  //0:编辑
    public static String TELL_STATUS_ADD = "1";  //1：新增
    private String cTellStatsu;

    public static String ADRESS_INFO = "adress_info";  //需要编辑的地址信息
    private TellEntity.DateBean adressBean;


    private String shipuser = "shipuser";  // 寄件人
    private String getuser = "getuser";  // 收件人

    private String mTellType;
    private String mSaveType;


    //选择的地址id
    private String mAdressId;


    private DeletableEditText etName;
    private DeletableEditText etPhone;
    private TextView tvAdress;
    private DeletableEditText etAdressInfo;
    private Button btSave;


    private AddTellModel addTellModel;
    private ChangeTellModel changeTellModel;
    private RemoveTellModel removeTellModel;


    /**
     * 多张图片相关
     */
    private int IMAGE_MAX = 2;
    private final int MULTI_IMG = 130;
    private final int ACT_GALLERY = 132;
    private final int ACT_CAMERA = 131;
    private ArrayList<String> mImageList = new ArrayList<>();
    private MyImgAdapter imageAdapter;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private LinearLayout llPic;
    private ImageFactoryModel imageFactoryModel;


    /**
     * 识别相关
     */
    private final int MULTI_IMG_DISCERN = 133;
    private DeletableEditText etDiscern;
    private LinearLayout llDiscern;
    private CropView cropView;
    private ScrollView scrollView;
    private Button btDiscern;
    private LinearLayout llEtDiscern;
    private LinearLayout llDiscernTop;
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
        setContentView(R.layout.activity_add_tell);
    }

    @Override
    public void initViews() {
        etDiscern = findViewById(R.id.et_discern);
        llDiscern = findViewById(R.id.ll_discern);
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        tvAdress = findViewById(R.id.tv_adress);
        etAdressInfo = findViewById(R.id.et_adressinfo);
        btSave = findViewById(R.id.ll_tell_save);
        recyclerView = findViewById(R.id.recycler_img);
        llPic = findViewById(R.id.ll_pic);
        cropView = findViewById(R.id.iv_cropview);
        scrollView = findViewById(R.id.scrollView);
        btDiscern = findViewById(R.id.bt_discern);
        llEtDiscern = findViewById(R.id.ll_et_discern);
        llDiscernTop = findViewById(R.id.ll_discern_top);
        btSave.setOnClickListener(this);
        tvAdress.setOnClickListener(this);
        btDiscern.setOnClickListener(this);
        llDiscernTop.setOnClickListener(this);


        Intent gIntent = getIntent();
        mTellType = gIntent.getExtras().getString(TELL_TYPE, "");
        if (mTellType.equals(TELL_TYPE_SEND)) {
            mToolbar.setTitle("新增寄件人");
            mSaveType = shipuser;
        } else {
            llPic.setVisibility(View.GONE);
            mToolbar.setTitle("新增收件人");
            mSaveType = getuser;
        }
        mToolbar.setNavigationIcon(R.drawable.icon_cancle);


        cTellStatsu = gIntent.getExtras().getString(TELL_STATUS, "");
        if (cTellStatsu.equals(TELL_STATUS_EDIT)) {
            llPic.setVisibility(View.GONE);

            adressBean = (TellEntity.DateBean) gIntent.getSerializableExtra(ADRESS_INFO);
            initAdressData();

            if (mTellType.equals(TELL_TYPE_SEND)) {
                mToolbar.setTitle("编辑寄件人");
                mSaveType = shipuser;
            } else {
                mToolbar.setTitle("编辑收件人");
                mSaveType = getuser;
            }
        }
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(this);



        //一个可以双指缩放移动的控件，解决滑动冲突
        cropView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //允许ScrollView截断点击事件，ScrollView可滑动
                    scrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    //不允许ScrollView截断点击事件，点击事件由子View处理
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });



        imageAdapter = new MyImgAdapter(AddTellActivity.this, mImageList);
        gridLayoutManager = new GridLayoutManager(AddTellActivity.this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener(new MyImgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent picIntent = new Intent(AddTellActivity.this, ImageLookActivity.class);
                picIntent.putExtra(ImageLookActivity.IMAGE_INTENT, mImageList.get(position));
                startActivity(picIntent);
            }

            @Override
            public void onAddClick(View view, int position) {
                if (ToolUtil.isEmpty(mImageList) || mImageList.size() < IMAGE_MAX) {
                    startCameras();
                } else {
                    ToastUtil.showToast(AddTellActivity.this, "图片张数上限,请删除部分照片后重试", ToastUtil.TOAST_TYPE_WARNING);
                }
            }

            @Override
            public void onDeleteClick(View view, int position) {
                showSelectDialog(position);
            }
        });


        imageFactoryModel = new ImageFactoryModel(AddTellActivity.this);
        imageFactoryModel.setOnChangeCallback(new ImageFactoryModel.OnErroListener() {
            @Override
            public void onSuccess(List<String> imgList, List<String> imgList2, int type) {
                addTellModel.addTell(mRequestClient
                        , mSaveType
                        , etName.getText().toString()
                        , etPhone.getText().toString()
                        , mAdressId
                        , etAdressInfo.getText().toString()
                        , imgList.get(0)
                );
            }


            @Override
            public void onErro(String erroMsg) {
                ToastUtil.showToast(AddTellActivity.this, erroMsg, ToastUtil.TOAST_TYPE_ERRO);
            }
        });


        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }

        //Android6.0之前安装时就能复制，6.0之后要先请求权限，所以6.0以上的这个方法无用。
//new Thread(new Runnable() {
//    @Override
//    public void run() {
//        assets2SD(getApplicationContext(), LANGUAGE_PATH, DEFAULT_LANGUAGE_NAME);
//    }
//}).start();
    }

    @Override
    public void initData() {
        if (cTellStatsu.equals(TELL_STATUS_EDIT)) {
            changeTellModel = ViewModelProviders.of(AddTellActivity.this).get(ChangeTellModel.class);
            changeTellModel.getCurrentData(AddTellActivity.this).observe(this, new Observer<SaveOrderEntity>() {
                @Override
                public void onChanged(@Nullable SaveOrderEntity saveOrderEntity) {
                    showSuccessDialog(AddTellActivity.this, "保存成功");
                }
            });
        } else {
            addTellModel = ViewModelProviders.of(AddTellActivity.this).get(AddTellModel.class);
            addTellModel.getCurrentData(AddTellActivity.this).observe(this, new Observer<SaveOrderEntity>() {
                @Override
                public void onChanged(@Nullable SaveOrderEntity saveOrderEntity) {
                    showSuccessDialog(AddTellActivity.this, "保存成功");
                }
            });
        }
        removeTellModel = ViewModelProviders.of(AddTellActivity.this).get(RemoveTellModel.class);
        removeTellModel.getCurrentData(AddTellActivity.this).observe(this, new Observer<SaveOrderEntity>() {
            @Override
            public void onChanged(@Nullable SaveOrderEntity saveOrderEntity) {
                showSuccessDialog(AddTellActivity.this, "删除成功");
            }
        });
    }

    private void initAdressData() {
        if (adressBean == null) {
            return;
        }
        String nameStr = adressBean.getTruename();
        String phoneStr = adressBean.getMobile();
        String adressInfo = adressBean.getAddress();
        mAdressId = adressBean.getAddress_id();
        String adressStr = adressBean.getCountry()
                + adressBean.getProvince()
                + adressBean.getCity()
                + adressBean.getDistrict();

        if (!StringUtils.isEmpty(nameStr)) {
            etName.setText(nameStr);
        }
        if (!StringUtils.isEmpty(phoneStr)) {
            etPhone.setText(phoneStr);
        }
        if (!StringUtils.isEmpty(adressStr)) {
            tvAdress.setText(adressStr);
        }
        if (!StringUtils.isEmpty(adressInfo)) {
            etAdressInfo.setText(adressInfo);
        }
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_adress:
                Intent aIntent = new Intent(AddTellActivity.this, AdressListActivity.class);
                aIntent.putExtra(AdressListActivity.ADRESSID, "0");
                startActivityForResult(aIntent, 0);
                break;
            case R.id.ll_tell_save:
                if (canSave()) {
                    if (cTellStatsu.equals(TELL_STATUS_EDIT)) {
                        //编辑不用传照片
                        changeTellModel.changeTell(mRequestClient
                                , adressBean.getId()
                                , etName.getText().toString()
                                , etPhone.getText().toString()
                                , mAdressId
                                , etAdressInfo.getText().toString());
                    }else if(mTellType.equals(TELL_TYPE_SEND)){
                        //新增发件人需要传照片
                        imageFactoryModel.compressImg(mImageList,null);
                    } else {
                        //新增收件人不用传照片
                        addTellModel.addTell(mRequestClient
                                , mSaveType
                                , etName.getText().toString()
                                , etPhone.getText().toString()
                                , mAdressId
                                , etAdressInfo.getText().toString()
                                , ""
                        );
                    }
                }
                break;
            case R.id.bt_discern:
                Bitmap bt = cropView.getOutput();
                recognition(bt);
                break;
            case R.id.ll_discern_top:
//                llEtDiscern.setVisibility(View.GONE);
//                if(llDiscern.getVisibility() == View.GONE){
//                    llDiscern.setVisibility(View.VISIBLE);
//                }else{
//                    llDiscern.setVisibility(View.GONE);
//                }
                break;
        }
    }


    private boolean canSave() {
        if (isEmptyByEditText(etName)) {
            ToastUtil.showToast(AddTellActivity.this, "请填写'姓名'", ToastUtil.TOAST_TYPE_WARNING);
            return false;
        } else if (isEmptyByEditText(etPhone)) {
            ToastUtil.showToast(AddTellActivity.this, "请填写'电话'", ToastUtil.TOAST_TYPE_WARNING);
            return false;
        } else if (StringUtils.isEmpty(tvAdress.getText().toString())) {
            ToastUtil.showToast(AddTellActivity.this, "请选择'地址'", ToastUtil.TOAST_TYPE_WARNING);
            return false;
        } else if (isEmptyByEditText(etAdressInfo)) {
            ToastUtil.showToast(AddTellActivity.this, "请填写'详细地址'", ToastUtil.TOAST_TYPE_WARNING);
            return false;
        } else if (ToolUtil.isEmpty(mImageList) && !cTellStatsu.equals(TELL_STATUS_EDIT) && mTellType.equals(TELL_TYPE_SEND)) {
            ToastUtil.showToast(AddTellActivity.this, "请填写上传身份证照片", ToastUtil.TOAST_TYPE_WARNING);
            return false;
        }
        return true;
    }

    private boolean isEmptyByEditText(DeletableEditText et) {
        String etStr = et.getText().toString().trim();
        if (StringUtils.isEmpty(etStr)) {
            et.setShakeAnimation();
            return true;
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 0) {
                if (resultCode == AdressListActivity.FINISH_CODE && data != null) {
                    ArrayList<String> nameList = data.getStringArrayListExtra(AdressListActivity.ADRESSNAMELIST);
                    mAdressId = data.getExtras().getString(AdressListActivity.ADRESSIDLIST);

                    String adressTest = null;
                    for (String str : nameList) {
                        if (StringUtils.isEmpty(adressTest)) {
                            adressTest = str;
                        } else {
                            adressTest = adressTest + str;
                        }
                    }
                    tvAdress.setText(adressTest);
                }
                //寄件
            } else if (requestCode == 1) {
                if (resultCode == AdressListActivity.FINISH_CODE && data != null) {
                    ArrayList<String> nameList = data.getStringArrayListExtra(AdressListActivity.ADRESSNAMELIST);
                    mAdressId = data.getExtras().getString(AdressListActivity.ADRESSIDLIST);
                    String adressTest = null;
                    for (String str : nameList) {
                        if (StringUtils.isEmpty(adressTest)) {
                            adressTest = str;
                        } else {
                            adressTest = adressTest + str;
                        }
                    }
                    tvAdress.setText(adressTest);
                }
            }
            if (resultCode == RESULT_OK && requestCode == MULTI_IMG) {
                ArrayList<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                mImageList.clear();
                mImageList.addAll(path);
                imageAdapter.notifyDataSetChanged();

            } else if (resultCode == RESULT_OK && requestCode == MULTI_IMG_DISCERN) {

                final ArrayList<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (ToolUtil.isEmpty(path)) {
                    return;
                }
                //recognition(BitmapFactory.decodeResource(getResources(), R.drawable.iv_test));
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
                Bitmap dBitmap = BitmapFactory.decodeFile(path.get(0), null);
                //Uri uri= Uri.parse(path.get(0));
                //Uri uri= Uri.parse("file:///content://com.miui.gallery.open/raw/%2Fstorage%2Femulated%2F0%2FDCIM%2FIMG_-2038920741.jpg");
                //Log.d("qqqqq",uri.toString());
                //cropView.of(uri).withAspect(2, 1).initialize(AddTellActivity.this);

             //   llDiscern.setVisibility(View.VISIBLE);
                recognition(dBitmap);


            }

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_tell_remove, menu);
        if (!cTellStatsu.equals(TELL_STATUS_EDIT)) {
            menu.findItem(R.id.action_remove).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_remove:
                removeTellDialog();
                break;
            case R.id.action_discern:
                startCamerasForDiscern();
                //recognition(BitmapFactory.decodeResource(getResources(), R.drawable.iv_test));
                break;

        }
        return true;
    }

    private SweetAlertDialog pDialogSuccess;

    private void showSuccessDialog(Context mContext, String title) {
        try {
            pDialogSuccess = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
            pDialogSuccess.setTitleText(title);
            pDialogSuccess.setCancelable(true);
            pDialogSuccess.setConfirmText("确定");
            pDialogSuccess.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    setResult(FINISH_RESH_CODE);
                    finish();
                }
            });
            pDialogSuccess.show();

        } catch (Exception e) {
        }
    }


    private void removeTellDialog() {
        new SweetAlertDialog(AddTellActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("提示")
                .setContentText("确定要删除本条信息吗？")
                .setConfirmText("确定")
                .setCancelText("取消")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        removeTellModel.removeTell(mRequestClient, adressBean.getId(), true);
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();

                    }
                })
                .show();
    }

    private void startCameras() {
        MultiImageSelector.create()
                .showCamera(true) // 是否显示相机. 默认为显示
                .count(2) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                //.single() // 单选模式
                .multi() // 多选模式, 默认模式;
                .origin(mImageList)
                .start(AddTellActivity.this, MULTI_IMG);
    }

    private void startCamerasForDiscern() {
        MultiImageSelector.create()
                .showCamera(true) // 是否显示相机. 默认为显示
                //.count(2) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                .single() // 单选模式
                //.multi() // 多选模式, 默认模式;
                //.origin(mImageList)
                .start(AddTellActivity.this, MULTI_IMG_DISCERN);
    }

    private void showSelectDialog(final int pos) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("是否删除当前照片?")
                // .setContentText("删除后无法恢复!")
                .setConfirmText("是")
                .setCancelText("取消")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        if (!ToolUtil.isEmpty(mImageList)) {
                            mImageList.remove(pos);
                            imageAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    public boolean checkTraineddataExists() {
        File file = new File(LANGUAGE_PATH);
        return file.exists();
    }

    /**
     * 识别图像
     *
     */
    private void recognition(final Bitmap resource) {
        showProgressDialog(AddTellActivity.this, "正在识别...");
//        final Bitmap bmp =  ImgUtils.getSmallBitmap(strBitmap);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//                    String text = "";
//                    if (!checkTraineddataExists()) {
//                        Log.i(TAG, "run: " + LANGUAGE_PATH + "不存在，开始复制\r\n");
//                        assets2SD(getApplicationContext(), LANGUAGE_PATH, DEFAULT_LANGUAGE_NAME);
//                    }
//
//                    TessBaseAPI tessBaseAPI = new TessBaseAPI();
//                    tessBaseAPI.init(DATAPATH, DEFAULT_LANGUAGE);
//                    tessBaseAPI.setImage(bmp);
//                    text = tessBaseAPI.getUTF8Text();
//                    Log.i(TAG, "run: text " + text);
//                    final String finalText = text;
//                    final Bitmap finalBitmap = bmp;
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            dismissProgressDialog();
//                            llDiscern.setVisibility(View.VISIBLE);
//                            etDiscern.setText(finalText);
//                            Log.d(TAG, "识别文字==" + finalText);
//                            if(StringUtils.isEmpty(finalText)){
//                                ToastUtil.showToast(AddTellActivity.this,"识别失败,请选择清晰度较高的图片",ToastUtil.TOAST_TYPE_WARNING);
//                            }
//                        }
//                    });
//                    tessBaseAPI.end();
//
//
//                } catch (Exception e) {
//                    Log.d("qqqqq", "识别异常" + e.getMessage());
////                    ToastUtil.showToast(AddTellActivity.this,"识别异常"+e.getMessage(),ToastUtil.TOAST_TYPE_ERRO);
//                    throw e;
//                }
//            }
//
//
//        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String text = "";
                    if (!checkTraineddataExists()) {
                        Log.i(TAG, "run: " + LANGUAGE_PATH + "不存在，开始复制\r\n");
                        assets2SD(getApplicationContext(), LANGUAGE_PATH, DEFAULT_LANGUAGE_NAME);
                    }

                    TessBaseAPI tessBaseAPI = new TessBaseAPI();
                    tessBaseAPI.init(DATAPATH, DEFAULT_LANGUAGE);
                    tessBaseAPI.setImage(resource);
                    text = tessBaseAPI.getUTF8Text();
                    Log.i(TAG, "run: text " + text);
                    final String finalText = text;
                    final Bitmap finalBitmap = resource;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissProgressDialog();
                            llDiscern.setVisibility(View.VISIBLE);
                            //llEtDiscern.setVisibility(View.VISIBLE);
                            etDiscern.setText(finalText);
                            Log.d(TAG, "识别文字==" + finalText);
                        }
                    });
                    tessBaseAPI.end();


                } catch (Exception e) {
                    Log.d("qqqqq", "识别异常" + e.getMessage());
//                    ToastUtil.showToast(AddTellActivity.this,"识别异常"+e.getMessage(),ToastUtil.TOAST_TYPE_ERRO);
                    throw e;
                }
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
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    assets2SD(getApplicationContext(), LANGUAGE_PATH, DEFAULT_LANGUAGE_NAME);
                }
                break;
            default:
                break;
        }
    }

    private SweetAlertDialog pDialog;

    public void showProgressDialog(Context mContext, String title) {
        try {
            pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(R.color.colorPrimary);
            pDialog.setTitleText(title);
            pDialog.setContentText("图片越大,识别时间越长");
            pDialog.setCancelable(false);
            pDialog.show();
        } catch (Exception e) {
        }
    }

    public void dismissProgressDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

}
