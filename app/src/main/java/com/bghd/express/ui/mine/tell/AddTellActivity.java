package com.bghd.express.ui.mine.tell;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.bghd.express.utils.tools.StringUtils;
import com.bghd.express.utils.tools.ToastUtil;
import com.bghd.express.utils.tools.ToolUtil;
import com.cazaea.sweetalert.SweetAlertDialog;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by lixu on 2018/2/11.
 */

public class AddTellActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {
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


    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_tell);
    }

    @Override
    public void initViews() {
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        tvAdress = findViewById(R.id.tv_adress);
        etAdressInfo = findViewById(R.id.et_adressinfo);
        btSave = findViewById(R.id.ll_tell_save);
        recyclerView = findViewById(R.id.recycler_img);
        llPic = findViewById(R.id.ll_pic);
        btSave.setOnClickListener(this);
        tvAdress.setOnClickListener(this);



        Intent gIntent = getIntent();
        mTellType = gIntent.getExtras().getString(TELL_TYPE, "");
        if (mTellType.equals(TELL_TYPE_SEND)) {
            mToolbar.setTitle("新增寄件人");
            mSaveType = shipuser;
        } else {
            mToolbar.setTitle("新增收件人");
            mSaveType = getuser;
        }
        mToolbar.setNavigationIcon(R.drawable.icon_cancle);


        cTellStatsu = gIntent.getExtras().getString(TELL_STATUS, "");
        if (cTellStatsu.equals(TELL_STATUS_EDIT)) {
            llPic.setVisibility(View.GONE);
            setSupportActionBar(mToolbar);
            mToolbar.setOnMenuItemClickListener(this);
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
            public void onSuccess(List<String> imgList) {
                addTellModel.addTell(mRequestClient
                        , mSaveType
                        , etName.getText().toString()
                        , etPhone.getText().toString()
                        , mAdressId
                        , etAdressInfo.getText().toString());
            }

            @Override
            public void onErro(String erroMsg) {
                ToastUtil.showToast(AddTellActivity.this,erroMsg,ToastUtil.TOAST_TYPE_ERRO);
            }
        });

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
        String adressStr = adressBean.getAddress();
        mAdressId = adressBean.getAddress_id();
        String adressInfo = adressBean.getCountry()
                + adressBean.getProvince()
                + adressBean.getCity()
                + adressBean.getDistrict()
                + adressBean.getAddress();

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
                        changeTellModel.changeTell(mRequestClient
                                , adressBean.getId()
                                , etName.getText().toString()
                                , etPhone.getText().toString()
                                , mAdressId
                                , etAdressInfo.getText().toString());
                    } else {
                        imageFactoryModel.compressImg(mImageList);
                    }
                }
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
        }else if(ToolUtil.isEmpty(mImageList)){
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

            }

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tell_remove, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_remove:
                removeTellDialog();
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


}
