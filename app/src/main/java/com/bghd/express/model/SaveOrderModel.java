package com.bghd.express.model;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.bghd.express.R;
import com.bghd.express.core.Constance;
import com.bghd.express.core.MallRequest;
import com.bghd.express.entiy.SaveOrderEntity;
import com.bghd.express.entiy.TellEntity;
import com.bghd.express.utils.base.BaseViewModel;
import com.bghd.express.utils.tools.SPUtil;
import com.bghd.express.utils.tools.ToastUtil;
import com.bghd.express.utils.tools.ToolUtil;
import com.cazaea.sweetalert.SweetAlertDialog;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixu on 2018/1/23.
 * @author lixu
 */

public class SaveOrderModel extends BaseViewModel {



    public SaveOrderModel(Application application) {
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
     * @param mRequest
     *uid
     *shipuser_address_id   寄件人地址id
     getuser_address_id    收件人地址id
     shipuser_truename   发件人姓名
     shipuser_mobile      发件人手机号
     shipuser_address   发件人详细地址
     getuser_truename  收件人姓名
     getuser_mobile     收件人手机号
     getuser_address    收件人详细地址
     order_price   实付款
     order_weight    重量
     manual    手动录入单号
     */
    public void saveOrder(MallRequest mRequest
            ,String shipuser_address_id
            ,String getuser_address_id
            ,String shipuser_truename
            , String shipuser_mobile
            , String shipuser_address
            , String getuser_truename
            , String getuser_mobile
            , String getuser_address
            , String order_price
            , String order_weight
            , String express_no
            , String shipuser_img
            , String getuser_img
    ) {
        showProgressDialog(mContext, "添加数据中...");
        SPUtil sp = new SPUtil(mContext,SPUtil.USER);
        String uId = sp.getString(SPUtil.USER_UID,"");
        mRequest.saveOrder(uId
                ,shipuser_address_id
                ,getuser_address_id
                ,shipuser_truename
                ,shipuser_mobile
                ,shipuser_address
                ,getuser_truename
                ,getuser_mobile
                ,getuser_address
                ,order_price
                ,order_weight
                ,express_no
                ,shipuser_img
                ,getuser_img
        )
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
