package com.bghd.express.model;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.bghd.express.R;
import com.bghd.express.core.Constance;
import com.bghd.express.core.MallRequest;
import com.bghd.express.entiy.SaveOrderEntity;
import com.bghd.express.utils.base.BaseViewModel;
import com.bghd.express.utils.tools.SPUtil;
import com.bghd.express.utils.tools.ToastUtil;
import com.cazaea.sweetalert.SweetAlertDialog;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixu on 2018/1/23.
 * @author lixu
 * 日结
 */

public class RiJieModel extends BaseViewModel {



    public RiJieModel(Application application) {
        super(application);
    }

    private MutableLiveData<SaveOrderEntity> roundSiteList;
    private Context mContext;


    public MutableLiveData<SaveOrderEntity> getCurrentData(Context context) {
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
    public void rijieStart(MallRequest mRequest) {
        showProgressDialog(mContext, "正在发起日结...");
        SPUtil sp = new SPUtil(mContext,SPUtil.USER);
        String uId = sp.getString(SPUtil.USER_UID,"");
        mRequest.rijie(uId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SaveOrderEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(SaveOrderEntity roundSiteEntity) {
                        dismissProgressDialog();
                        if (roundSiteList == null) {
                            roundSiteList = new MutableLiveData<>();
                        }
                        if (roundSiteEntity.getStatus() == Constance.REQUEST_SUCCESS_CODE) {

                            roundSiteList.postValue(roundSiteEntity);

                        } else {
                            if(onCallBackListener != null) {
                                onCallBackListener.onErro();
                            }
                            ToastUtil.showToast(mContext, roundSiteEntity.getInfo(), ToastUtil.TOAST_TYPE_ERRO);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        if(onCallBackListener != null) {
                            onCallBackListener.onErro();
                        }
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
