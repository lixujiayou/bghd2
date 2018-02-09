package com.bghd.express.model;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.bghd.express.R;
import com.bghd.express.core.Constance;
import com.bghd.express.core.MallRequest;
import com.bghd.express.entiy.AdressEntity;
import com.bghd.express.entiy.OrderListEntity;
import com.bghd.express.utils.base.BaseViewModel;
import com.bghd.express.utils.pinyin.CharacterParser;
import com.bghd.express.utils.pinyin.PinyinComparator;
import com.bghd.express.utils.tools.SPUtil;
import com.bghd.express.utils.tools.ToastUtil;
import com.bghd.express.utils.tools.ToolUtil;
import com.cazaea.sweetalert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixu on 2018/1/23.
 * @author lixu
 */

public class AdressListModel extends BaseViewModel {



    public AdressListModel(Application application) {
        super(application);
    }

    private MutableLiveData<List<AdressEntity.DateBean>> roundSiteList;
    private Context mContext;


    public MutableLiveData<List<AdressEntity.DateBean>> getCurrentData(Context context) {
        this.mContext = context;
        if (roundSiteList == null) {
            roundSiteList = new MutableLiveData<>();
            //实例化汉字转拼音类
            characterParser=CharacterParser.getInstance();
            pinyinComparator=new PinyinComparator();
        }

        return roundSiteList;
    }

    /**
     *
     * @param mRequest
     * @param id
     */
    public void loadAdressList(MallRequest mRequest, String id) {
        showProgressDialog(mContext, "初始化数据...");
        mRequest.getAdressList(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AdressEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(AdressEntity roundSiteEntity) {
                        dismissProgressDialog();
                        if (roundSiteList == null) {
                            roundSiteList = new MutableLiveData<>();
                        }
                        if (roundSiteEntity.getStatus() == Constance.REQUEST_SUCCESS_CODE) {
                            if (!ToolUtil.isEmpty(roundSiteEntity.getDate())) {
                                List sites = roundSiteEntity.getDate();
                                Log.d("qqqqq","查询数据"+sites.size());
                                List myAdressList = filledData(sites);
                                //根据a-z进行排序源数据
                                Collections.sort(myAdressList,pinyinComparator);
                                roundSiteList.postValue(myAdressList);

                            } else {
                                onCallBackListener.onErro();
                                ToastUtil.showToast(mContext, "未初始化到数据", ToastUtil.TOAST_TYPE_WARNING);
                            }
                        } else {
                            onCallBackListener.onErro();
                            ToastUtil.showToast(mContext, roundSiteEntity.getInfo(), ToastUtil.TOAST_TYPE_ERRO);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        onCallBackListener.onErro();
                        String strMsg = Constance.getMsgByException(e);
                        ToastUtil.showToast(mContext, strMsg, ToastUtil.TOAST_TYPE_ERRO);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    private OnErroListener onCallBackListener;

    public void setOnErroCallback(OnErroListener onItemClickListener) {
        this.onCallBackListener = onItemClickListener;
    }

    public interface OnErroListener {
        void onErro();
    }


    private SweetAlertDialog pDialog;

    public void showProgressDialog(Context mContext, String title) {
        try {
            pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(R.color.colorPrimary);
            pDialog.setTitleText(title);
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





    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    /**
     * 为ListView填充数据
     * @param
     * @return
     */
    private List<AdressEntity.DateBean> filledData(List<AdressEntity.DateBean> list){
        List<AdressEntity.DateBean> mSortList = new ArrayList<>();
        for(int i=0; i<list.size(); i++){
            AdressEntity.DateBean province = list.get(i);

//            province.setProvince(list.get(i).getProvince());
//            province.setId(list.get(i).getId());

        //    province = list.get(i);



            //汉字转换成拼音
            String pinyin;
            String typeTest = list.get(i).getLevel();
            if(typeTest.equals("1")){
                pinyin = characterParser.getSelling(list.get(i).getProvince());
            }else if(typeTest.equals("2")){
                pinyin = characterParser.getSelling(list.get(i).getCity());
            }else{
                pinyin = characterParser.getSelling(list.get(i).getDistrict());
            }



            String sortString = pinyin.substring(0, 1).toUpperCase();//获取拼音首字母
            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                province.setSortLetters(sortString.toUpperCase());
            }else{
                province.setSortLetters("#");
            }

            mSortList.add(province);
        }
        return mSortList;
    }
}
