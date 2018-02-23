package com.bghd.express.ui.order;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bghd.express.R;
import com.bghd.express.adapter.GlideImageLoader;
import com.bghd.express.adapter.MyImgAdapter;
import com.bghd.express.entiy.SaveOrderEntity;
import com.bghd.express.entiy.ShowImgEntity;
import com.bghd.express.entiy.TellEntity;
import com.bghd.express.model.ImageFactoryModel;
import com.bghd.express.model.SaveOrderModel;
import com.bghd.express.model.ShowImgListModel;
import com.bghd.express.ui.ImageLookActivity;
import com.bghd.express.ui.mine.tell.AddTellActivity;
import com.bghd.express.ui.mine.tell.TellListActivity;
import com.bghd.express.utils.base.BaseActivity;
import com.bghd.express.utils.base.DeletableEditText;
import com.bghd.express.utils.tools.StringUtils;
import com.bghd.express.utils.tools.ToastUtil;
import com.bghd.express.utils.tools.ToolUtil;
import com.bghd.express.utils.zxing.activity.CaptureActivity;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by lixu on 2018/2/5.
 */

public class PlaceOrderActivity extends BaseActivity {
    public static final String SAVE_WAY = "order_save";//订单编号获取方式
    public static final String SAVE_AUTO = "order_auto";//自动获取
    public static final String SAVE_SCAN = "order_scan";//扫描条形码
    private String saveWay;


    private List<String> imageList = new ArrayList<>();
    private List<String> strList = new ArrayList<>();
    private TextView tvAcceptAdress;
    private TextView tvSendAdress;
    private Banner banner;

    private Button tvAccpetPerson;
    private Button tvSendPerson;

    //接收 选择的地址id
    //private ArrayList accpetAdressIdList = new ArrayList();
    private String mAccpetAdressId;


    //发送 选择的地址id
    //private ArrayList sendAdressIdList = new ArrayList();
    private String mSendAdressId;


    //收件人
    private TellEntity.DateBean tellAccpet;
    //寄件人
    private TellEntity.DateBean sendAccpet;


    private DeletableEditText etOrderPrice;//订单价格
    private DeletableEditText etWeight;//物品重量
    /**
     * 收件人
     */
    private DeletableEditText etAName;//姓名
    private DeletableEditText etAPHone;//电话
    private DeletableEditText etAAdressInfo;//详细地址

    /**
     * 寄件人
     */
    private DeletableEditText etSName;//姓名
    private DeletableEditText etSPHone;//电话
    private DeletableEditText etSAdressInfo;//详细地址

    private DeletableEditText etOrderCode;


    private Button llSave;

    private LinearLayout llScan;
    private LinearLayout llAutoOrderCode;

    private SaveOrderModel saveOrderModel;
    private ShowImgListModel showImgListModel;


    /**
     * 多张图片相关
     */
    private int IMAGE_MAX = 2;
    private final int MULTI_IMG = 130;
    private ArrayList<String> mImageList = new ArrayList<>();
    private MyImgAdapter imageAdapter;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private LinearLayout llPic;
    private ImageFactoryModel imageFactoryModel;
    private boolean isSSelected = false;//寄件人

    private final int MULTI_IMGA = 133;
    private ArrayList<String> mImageListA = new ArrayList<>();
    private MyImgAdapter imageAdapterA;
    private RecyclerView recyclerViewA;
    private GridLayoutManager gridLayoutManagerA;
    private LinearLayout llPicA;
    private ImageFactoryModel imageFactoryModelA;
    private boolean isASelected = false;//收件人




    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_place_order);
    }

    @Override
    public void initViews() {

        tvAcceptAdress = findViewById(R.id.tv_accept_adress);
        tvSendAdress = findViewById(R.id.tv_send_adress);
        tvAccpetPerson = findViewById(R.id.bt_accpet_person);
        tvSendPerson = findViewById(R.id.bt_send_person);
        llScan = findViewById(R.id.ll_scan);
        llAutoOrderCode = findViewById(R.id.ll_auto_order);
        recyclerView = findViewById(R.id.recycler_img);
        recyclerViewA = findViewById(R.id.recycler_a_img);
        llPic = findViewById(R.id.ll_pic);
        llPicA = findViewById(R.id.ll_a_pic);
        etOrderCode = findViewById(R.id.et_order_code);

        llSave = findViewById(R.id.ll_meter_save);

        etAName = findViewById(R.id.et_a_name);
        etAPHone = findViewById(R.id.et_a_phone);
        etAAdressInfo = findViewById(R.id.et_a_adressinfo);

        etSName = findViewById(R.id.et_s_name);
        etSPHone = findViewById(R.id.et_s_phone);
        etSAdressInfo = findViewById(R.id.et_s_adressinfo);
        etOrderPrice = findViewById(R.id.et_order_price);
        etWeight = findViewById(R.id.et_weight);

        banner = findViewById(R.id.banner);


        tvAcceptAdress.setOnClickListener(this);
        tvAccpetPerson.setOnClickListener(this);
        tvSendPerson.setOnClickListener(this);
        tvSendAdress.setOnClickListener(this);
        llScan.setOnClickListener(this);
        llSave.setOnClickListener(this);

        saveOrderModel = ViewModelProviders.of(PlaceOrderActivity.this).get(SaveOrderModel.class);
        saveOrderModel.getCurrentData(PlaceOrderActivity.this).observe(this, new Observer<SaveOrderEntity>() {
            @Override
            public void onChanged(@Nullable SaveOrderEntity dateBeans) {
                showSuccessDialog(PlaceOrderActivity.this, "添加成功");
            }
        });
        saveOrderModel.setOnErroCallback(new SaveOrderModel.OnErroListener() {
            @Override
            public void onErro() {
            }
        });




        showImgListModel = ViewModelProviders.of(PlaceOrderActivity.this).get(ShowImgListModel.class);
        showImgListModel.getCurrentData(PlaceOrderActivity.this).observe(this, new Observer<List<ShowImgEntity.DataBean>>() {
            @Override
            public void onChanged(@Nullable List<ShowImgEntity.DataBean> dataBeans) {
                imageList.clear();
                strList.clear();
                Log.d("qqqqqq","图片个数"+dataBeans.size());
                for(ShowImgEntity.DataBean img: dataBeans){
                    imageList.add(img.getImg());
                    strList.add(img.getDescription());
                }
                initBanner();
            }
        });
        showImgListModel.getImgList(mRequestClient);
        showImgListModel.setOnErroCallback(new ShowImgListModel.OnErroListener() {
            @Override
            public void onErro() {
                initBanner();
            }
        });


        imageAdapter = new MyImgAdapter(PlaceOrderActivity.this, mImageList);
        gridLayoutManager = new GridLayoutManager(PlaceOrderActivity.this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener(new MyImgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent picIntent = new Intent(PlaceOrderActivity.this, ImageLookActivity.class);
                picIntent.putExtra(ImageLookActivity.IMAGE_INTENT, mImageList.get(position));
                startActivity(picIntent);
            }

            @Override
            public void onAddClick(View view, int position) {
                if (ToolUtil.isEmpty(mImageList) || mImageList.size() < IMAGE_MAX) {
                    startCameras();
                } else {
                    ToastUtil.showToast(PlaceOrderActivity.this, "图片张数上限,请删除部分照片后重试", ToastUtil.TOAST_TYPE_WARNING);
                }
            }

            @Override
            public void onDeleteClick(View view, int position) {
                showSelectDialog(1,position);
            }
        });




        imageFactoryModel = new ImageFactoryModel(PlaceOrderActivity.this);
        imageFactoryModel.setOnChangeCallback(new ImageFactoryModel.OnErroListener() {
            @Override
            public void onSuccess(List<String> imgList,List<String> imgList2, int type) {
                Log.d("qqqqq","imgList=="+imgList.get(0));
                if(type == 1){
                    if(!ToolUtil.isEmpty(imgList2)){
                        saveOrderModel.saveOrder(mRequestClient
                                , mSendAdressId
                                , mAccpetAdressId
                                , etSName.getText().toString().trim()
                                , etSPHone.getText().toString().trim()
                                , etSAdressInfo.getText().toString().trim()
                                , etAName.getText().toString().trim()
                                , etAPHone.getText().toString().trim()
                                , etAAdressInfo.getText().toString().trim()
                                , etOrderPrice.getText().toString().trim()
                                , etWeight.getText().toString().trim()
                                , etOrderCode.getText().toString().trim()
                                ,""
                                ,imgList2.get(0)
                        );
                    }
                }else if(type == 0){
                    if(!ToolUtil.isEmpty(imgList)){
                        saveOrderModel.saveOrder(mRequestClient
                                , mSendAdressId
                                , mAccpetAdressId
                                , etSName.getText().toString().trim()
                                , etSPHone.getText().toString().trim()
                                , etSAdressInfo.getText().toString().trim()
                                , etAName.getText().toString().trim()
                                , etAPHone.getText().toString().trim()
                                , etAAdressInfo.getText().toString().trim()
                                , etOrderPrice.getText().toString().trim()
                                , etWeight.getText().toString().trim()
                                , etOrderCode.getText().toString().trim()
                                ,imgList.get(0)
                                ,""
                        );
                    }
                }else{
                    if(!ToolUtil.isEmpty(imgList) && !ToolUtil.isEmpty(imgList2)){
                        saveOrderModel.saveOrder(mRequestClient
                                , mSendAdressId
                                , mAccpetAdressId
                                , etSName.getText().toString().trim()
                                , etSPHone.getText().toString().trim()
                                , etSAdressInfo.getText().toString().trim()
                                , etAName.getText().toString().trim()
                                , etAPHone.getText().toString().trim()
                                , etAAdressInfo.getText().toString().trim()
                                , etOrderPrice.getText().toString().trim()
                                , etWeight.getText().toString().trim()
                                , etOrderCode.getText().toString().trim()
                                ,imgList.get(0)
                                ,imgList2.get(0)
                                );
                    }
                }

            }



            @Override
            public void onErro(String erroMsg) {
                ToastUtil.showToast(PlaceOrderActivity.this,erroMsg,ToastUtil.TOAST_TYPE_ERRO);
            }
        });


        imageAdapterA = new MyImgAdapter(PlaceOrderActivity.this, mImageListA);
        gridLayoutManagerA = new GridLayoutManager(PlaceOrderActivity.this, 3);
        recyclerViewA.setLayoutManager(gridLayoutManagerA);
        recyclerViewA.setItemAnimator(new DefaultItemAnimator());
        recyclerViewA.setHasFixedSize(true);
        recyclerViewA.setAdapter(imageAdapterA);
        imageAdapterA.setOnItemClickListener(new MyImgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent picIntent = new Intent(PlaceOrderActivity.this, ImageLookActivity.class);
                picIntent.putExtra(ImageLookActivity.IMAGE_INTENT, mImageListA.get(position));
                startActivity(picIntent);
            }

            @Override
            public void onAddClick(View view, int position) {
                if (ToolUtil.isEmpty(mImageListA) || mImageListA.size() < IMAGE_MAX) {
                    startCamerasA();
                } else {
                    ToastUtil.showToast(PlaceOrderActivity.this, "图片张数上限,请删除部分照片后重试", ToastUtil.TOAST_TYPE_WARNING);
                }
            }

            @Override
            public void onDeleteClick(View view, int position) {
                showSelectDialog(0,position);
            }
        });


        imageFactoryModelA = new ImageFactoryModel(PlaceOrderActivity.this);
        imageFactoryModelA.setOnChangeCallback(new ImageFactoryModel.OnErroListener() {
            @Override
            public void onSuccess(List<String> imgList,List<String> imgList2, int type) {
                if(type == 0){
                    if(!ToolUtil.isEmpty(imgList2)){
                        saveOrderModel.saveOrder(mRequestClient
                                , mSendAdressId
                                , mAccpetAdressId
                                , etSName.getText().toString().trim()
                                , etSPHone.getText().toString().trim()
                                , etSAdressInfo.getText().toString().trim()
                                , etAName.getText().toString().trim()
                                , etAPHone.getText().toString().trim()
                                , etAAdressInfo.getText().toString().trim()
                                , etOrderPrice.getText().toString().trim()
                                , etWeight.getText().toString().trim()
                                , etOrderCode.getText().toString().trim()
                                ,""
                                ,imgList2.get(0)
                        );
                    }
                }else if(type == 1){
                    if(!ToolUtil.isEmpty(imgList)){
                        saveOrderModel.saveOrder(mRequestClient
                                , mSendAdressId
                                , mAccpetAdressId
                                , etSName.getText().toString().trim()
                                , etSPHone.getText().toString().trim()
                                , etSAdressInfo.getText().toString().trim()
                                , etAName.getText().toString().trim()
                                , etAPHone.getText().toString().trim()
                                , etAAdressInfo.getText().toString().trim()
                                , etOrderPrice.getText().toString().trim()
                                , etWeight.getText().toString().trim()
                                , etOrderCode.getText().toString().trim()
                                ,imgList.get(0)
                                ,""
                        );
                    }
                }else{
                    if(!ToolUtil.isEmpty(imgList) && !ToolUtil.isEmpty(imgList2)){
                        saveOrderModel.saveOrder(mRequestClient
                                , mSendAdressId
                                , mAccpetAdressId
                                , etSName.getText().toString().trim()
                                , etSPHone.getText().toString().trim()
                                , etSAdressInfo.getText().toString().trim()
                                , etAName.getText().toString().trim()
                                , etAPHone.getText().toString().trim()
                                , etAAdressInfo.getText().toString().trim()
                                , etOrderPrice.getText().toString().trim()
                                , etWeight.getText().toString().trim()
                                , etOrderCode.getText().toString().trim()
                                ,imgList.get(0)
                                ,imgList2.get(0)
                        );
                    }
                }
            }

            @Override
            public void onErro(String erroMsg) {
                ToastUtil.showToast(PlaceOrderActivity.this,erroMsg,ToastUtil.TOAST_TYPE_ERRO);
            }
        });
    }

    @Override
    public void initData() {
        Intent gIntent = getIntent();
        saveWay = gIntent.getExtras().getString(SAVE_WAY);
        if (saveWay.equals(SAVE_AUTO)) {
            llAutoOrderCode.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.tv_accept_adress:
                Intent aIntent = new Intent(PlaceOrderActivity.this, AdressListActivity.class);
                aIntent.putExtra(AdressListActivity.ADRESSID, "0");
                startActivityForResult(aIntent, 0);
                break;
            case R.id.tv_send_adress:
                Intent sIntent = new Intent(PlaceOrderActivity.this, AdressListActivity.class);
                sIntent.putExtra(AdressListActivity.ADRESSID, "0");
                startActivityForResult(sIntent, 1);
                break;
            case R.id.bt_accpet_person:
                Intent paIntent = new Intent(PlaceOrderActivity.this, TellListActivity.class);
                paIntent.putExtra(TellListActivity.TELL_TYPE, TellListActivity.TELL_TYPE_ACCPET);
                paIntent.putExtra(TellListActivity.TELL_STATUS, TellListActivity.TELL_STATUS_SELECT);
                startActivityForResult(paIntent, 2);
                break;
            case R.id.bt_send_person:
                Intent psIntent = new Intent(PlaceOrderActivity.this, TellListActivity.class);
                psIntent.putExtra(TellListActivity.TELL_TYPE, TellListActivity.TELL_TYPE_SEND);
                psIntent.putExtra(TellListActivity.TELL_STATUS, TellListActivity.TELL_STATUS_SELECT);
                startActivityForResult(psIntent, 3);
                break;
            //扫描订单编号
            case R.id.ll_scan:
                Intent scanIntent = new Intent();
                scanIntent.setClass(PlaceOrderActivity.this, CaptureActivity.class);
                startActivityForResult(scanIntent, 4);
                break;
            //提交订单
            case R.id.ll_meter_save:
                if (canSave()) {
                    if(!isASelected && !isASelected) {
                        imageFactoryModel.compressImg(mImageList,mImageListA);
                    }else if(!isASelected && isSSelected){
                        imageFactoryModelA.compressImg(null,mImageListA);
                    }else if(isASelected && !isSSelected){
                        imageFactoryModel.compressImg(mImageList,null);
                    }else{
                        saveOrderModel.saveOrder(mRequestClient
                                , mSendAdressId
                                , mAccpetAdressId
                                , etSName.getText().toString().trim()
                                , etSPHone.getText().toString().trim()
                                , etSAdressInfo.getText().toString().trim()
                                , etAName.getText().toString().trim()
                                , etAPHone.getText().toString().trim()
                                , etAAdressInfo.getText().toString().trim()
                                , etOrderPrice.getText().toString().trim()
                                , etWeight.getText().toString().trim()
                                , etOrderCode.getText().toString().trim()
                                ,""
                                ,""
                        );
                    }
                }
                break;

        }
    }



    private void initBanner(){

        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(imageList);
        //设置banner动画效果
        // banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(strList);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(5500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }


    private boolean canSave() {
        if (saveWay.equals(SAVE_SCAN)) {
            if (isEmptyByEditText(etOrderCode)) {
                ToastUtil.showToast(PlaceOrderActivity.this, "请扫描订单编号", ToastUtil.TOAST_TYPE_WARNING);
                return false;
            }
        }
        if (isEmptyByEditText(etOrderPrice)) {
            ToastUtil.showToast(PlaceOrderActivity.this, "请填写订单价格", ToastUtil.TOAST_TYPE_WARNING);
            return false;
        } else if (isEmptyByEditText(etWeight)) {
            ToastUtil.showToast(PlaceOrderActivity.this, "请填写物品重量", ToastUtil.TOAST_TYPE_WARNING);
            return false;
        } else if (isEmptyByEditText(etAName)) {
            ToastUtil.showToast(PlaceOrderActivity.this, "请填写收件人姓名", ToastUtil.TOAST_TYPE_WARNING);
            return false;
        } else if (isEmptyByEditText(etAPHone)) {
            ToastUtil.showToast(PlaceOrderActivity.this, "请填写收件人电话", ToastUtil.TOAST_TYPE_WARNING);
            return false;
        } else if (StringUtils.isEmpty(tvAcceptAdress.getText().toString())) {
            ToastUtil.showToast(PlaceOrderActivity.this, "请选择收件人地址", ToastUtil.TOAST_TYPE_WARNING);
            return false;
        } else if (isEmptyByEditText(etAAdressInfo)) {
            ToastUtil.showToast(PlaceOrderActivity.this, "请填写收件人详细地址", ToastUtil.TOAST_TYPE_WARNING);
            return false;
        } else if (isEmptyByEditText(etSName)) {
            ToastUtil.showToast(PlaceOrderActivity.this, "请填写寄件人姓名", ToastUtil.TOAST_TYPE_WARNING);
            return false;
        } else if (isEmptyByEditText(etSPHone)) {
            ToastUtil.showToast(PlaceOrderActivity.this, "请填写寄件人电话", ToastUtil.TOAST_TYPE_WARNING);
            return false;
        } else if (StringUtils.isEmpty(tvSendAdress.getText().toString())) {
            ToastUtil.showToast(PlaceOrderActivity.this, "请选择寄件人地址", ToastUtil.TOAST_TYPE_WARNING);
            return false;
        } else if (isEmptyByEditText(etSAdressInfo)) {
            ToastUtil.showToast(PlaceOrderActivity.this, "请填写寄件人详细地址", ToastUtil.TOAST_TYPE_WARNING);
            return false;
        }else if(!isASelected && ToolUtil.isEmpty(mImageListA)){
            ToastUtil.showToast(PlaceOrderActivity.this, "请选择收件人身份证照片", ToastUtil.TOAST_TYPE_WARNING);
            return false;
        }else if(!isSSelected && ToolUtil.isEmpty(mImageList)){
            ToastUtil.showToast(PlaceOrderActivity.this, "请选择寄件人身份证照片", ToastUtil.TOAST_TYPE_WARNING);
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
            //收件
            if (requestCode == 0) {
                if (resultCode == AdressListActivity.FINISH_CODE && data != null) {
                    ArrayList<String> nameList = data.getStringArrayListExtra(AdressListActivity.ADRESSNAMELIST);
                    mAccpetAdressId = data.getExtras().getString(AdressListActivity.ADRESSIDLIST);

                    String adressTest = null;
                    for (String str : nameList) {
                        if (StringUtils.isEmpty(adressTest)) {
                            adressTest = str;
                        } else {
                            adressTest = adressTest + str;
                        }
                    }
                    tvAcceptAdress.setText(adressTest);
                }
                //寄件
            } else if (requestCode == 1) {
                if (resultCode == AdressListActivity.FINISH_CODE && data != null) {
                    ArrayList<String> nameList = data.getStringArrayListExtra(AdressListActivity.ADRESSNAMELIST);
                    mSendAdressId = data.getExtras().getString(AdressListActivity.ADRESSIDLIST);
                    String adressTest = null;
                    for (String str : nameList) {
                        if (StringUtils.isEmpty(adressTest)) {
                            adressTest = str;
                        } else {
                            adressTest = adressTest + str;
                        }
                    }
                    tvSendAdress.setText(adressTest);

                }
                //收件人
            } else if (requestCode == 2) {
                isASelected = true;
                llPicA.setVisibility(View.GONE);
                if (resultCode == TellListActivity.FINISH_CODE) {

                    tellAccpet = (TellEntity.DateBean) data.getSerializableExtra(TellListActivity.FINISH_TELL);
                    if (!StringUtils.isEmpty(tellAccpet.getTruename())) {
                        etAName.setText(tellAccpet.getTruename());
                    }

                    if (!StringUtils.isEmpty(tellAccpet.getMobile())) {
                        etAPHone.setText(tellAccpet.getMobile());
                    }
                    if (!StringUtils.isEmpty(tellAccpet.getAddress())) {
                        etAAdressInfo.setText(tellAccpet.getAddress());
                    }

                    mAccpetAdressId = tellAccpet.getAddress_id();

                    if (!StringUtils.isEmpty(tellAccpet.getProvince()) && !StringUtils.isEmpty(tellAccpet.getCity()) && !StringUtils.isEmpty(tellAccpet.getDistrict())) {
                        tvAcceptAdress.setText(tellAccpet.getProvince()
                                + tellAccpet.getCity()
                                + tellAccpet.getDistrict());
                    }
                }
                //寄件人
            } else if (requestCode == 3) {
                isSSelected = true;
                llPic.setVisibility(View.GONE);
                if (resultCode == TellListActivity.FINISH_CODE) {
                    sendAccpet = (TellEntity.DateBean) data.getSerializableExtra(TellListActivity.FINISH_TELL);
                    if (!StringUtils.isEmpty(sendAccpet.getTruename())) {
                        etSName.setText(sendAccpet.getTruename());
                    }
                    if (!StringUtils.isEmpty(sendAccpet.getMobile())) {
                        etSPHone.setText(sendAccpet.getMobile());
                    }
                    if (!StringUtils.isEmpty(sendAccpet.getAddress())) {
                        etSAdressInfo.setText(sendAccpet.getAddress());
                    }
                    if (!StringUtils.isEmpty(sendAccpet.getAddress_id())) {
                        mSendAdressId = sendAccpet.getAddress_id();
                    }

                    if (!StringUtils.isEmpty(sendAccpet.getProvince()) && !StringUtils.isEmpty(sendAccpet.getCity()) && !StringUtils.isEmpty(sendAccpet.getDistrict())) {
                        tvSendAdress.setText(sendAccpet.getProvince()
                                + sendAccpet.getCity()
                                + sendAccpet.getDistrict());
                    }

                }
            }

            //二维码、条形码
            if (resultCode == 13) {
                if (data != null) {
                    //条形码 asset
                    if (data.getExtras().getInt(CaptureActivity.SCAN_TYPE) == 1) {
                        String mResult = data.getExtras().getString(CaptureActivity.SCAN_RESULT);
                        if (!StringUtils.isEmpty(mResult)) {
                            Log.d("qqqqq", "条形码==" + mResult);
                            etOrderCode.setText(mResult);
                            ToastUtil.showToast(PlaceOrderActivity.this, "扫描成功", ToastUtil.TOAST_TYPE_WARNING);

                        } else {
                            ToastUtil.showToast(PlaceOrderActivity.this, "识别失败,请重新扫描", ToastUtil.TOAST_TYPE_WARNING);
                            return;
                        }
                        //二维码  site
                    } else {
                        ToastUtil.showToast(PlaceOrderActivity.this, "请扫描条形码哦", ToastUtil.TOAST_TYPE_WARNING);

                        /*String mResult = data.getExtras().getString(CaptureActivity.SCAN_RESULT);
                        if(StringUtils.isEmpty(mResult)) {
                            //LogUtils.d(resultCode+"----"+mResult);
                            Log.d("qqqqq","二维码=="+mResult);
                        }else{
                            ToastUtil.showToast(PlaceOrderActivity.this, "识别失败,请重新扫描",ToastUtil.TOAST_TYPE_WARNING);
                            return;
                        }*/
                    }
                }
            }

            if (resultCode == RESULT_OK && requestCode == MULTI_IMG) {
                ArrayList<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                mImageList.clear();
                mImageList.addAll(path);
                imageAdapter.notifyDataSetChanged();

            }else if(resultCode == RESULT_OK && requestCode == MULTI_IMGA){
                ArrayList<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                mImageListA.clear();
                mImageListA.addAll(path);
                imageAdapterA.notifyDataSetChanged();
            }




        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (banner != null) {
            //banner.stopAutoPlay();
        }
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
                    //EventBus
                    finish();
                }
            });
            pDialogSuccess.show();

        } catch (Exception e) {
        }
    }


    private void showFaildDialog(Context mContext, String title) {
        try {
            pDialogSuccess = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE);
            pDialogSuccess.setTitleText(title);
            pDialogSuccess.setCancelable(true);
            pDialogSuccess.setConfirmText("好的");
            pDialogSuccess.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                }
            });
            pDialogSuccess.show();

        } catch (Exception e) {
        }
    }
    private void startCameras() {
        MultiImageSelector.create()
                .showCamera(true) // 是否显示相机. 默认为显示
                .count(2) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                //.single() // 单选模式
                .multi() // 多选模式, 默认模式;
                .origin(mImageList)
                .start(PlaceOrderActivity.this, MULTI_IMG);
    }
private void startCamerasA() {
        MultiImageSelector.create()
                .showCamera(true) // 是否显示相机. 默认为显示
                .count(2) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                //.single() // 单选模式
                .multi() // 多选模式, 默认模式;
                .origin(mImageListA)
                .start(PlaceOrderActivity.this, MULTI_IMGA);
    }

    /**
     * type 0 : 收件  1：寄件
     * @param type
     * @param pos
     */
    private void showSelectDialog(final int type, final int pos) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("是否删除当前照片?")
                // .setContentText("删除后无法恢复!")
                .setConfirmText("是")
                .setCancelText("取消")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        if(type == 1) {
                            if (!ToolUtil.isEmpty(mImageList)) {
                                mImageList.remove(pos);
                                imageAdapter.notifyDataSetChanged();
                            }
                        }else{
                            if (!ToolUtil.isEmpty(mImageListA)) {
                                mImageListA.remove(pos);
                                imageAdapterA.notifyDataSetChanged();
                            }
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
