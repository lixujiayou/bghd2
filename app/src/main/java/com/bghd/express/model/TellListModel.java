package com.bghd.express.model;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.bghd.express.R;
import com.bghd.express.core.Constance;
import com.bghd.express.core.MallRequest;
import com.bghd.express.entiy.AdressEntity;
import com.bghd.express.entiy.TellEntity;
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

public class TellListModel extends BaseViewModel {



    public TellListModel(Application application) {
        super(application);
    }

    private MutableLiveData<List<TellEntity.DateBean>> roundSiteList;
    private Context mContext;


    public MutableLiveData<List<TellEntity.DateBean>> getCurrentData(Context context) {
        this.mContext = context;
        if (roundSiteList == null) {
            roundSiteList = new MutableLiveData<>();
        }

        return roundSiteList;
    }

    /**
     *
     * @param mRequest
     */
    public void loadTellList(MallRequest mRequest,String page,String size,String type, String truename) {
        showProgressDialog(mContext, "初始化数据...");
        SPUtil sp = new SPUtil(mContext,SPUtil.USER);
        String uId = sp.getString(SPUtil.USER_UID,"");
        mRequest.getTellList(page,size,uId,type,truename)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TellEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(TellEntity roundSiteEntity) {
                        dismissProgressDialog();
                        if (roundSiteList == null) {
                            roundSiteList = new MutableLiveData<>();
                        }
                        if (roundSiteEntity.getStatus() == Constance.REQUEST_SUCCESS_CODE) {
                            if (!ToolUtil.isEmpty(roundSiteEntity.getDate())) {

                                roundSiteList.postValue(roundSiteEntity.getDate());

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






}
