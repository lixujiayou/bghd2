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
 * 修改打印状态
 */

public class ChangePrintStatusModel extends BaseViewModel {



    public ChangePrintStatusModel(Application application) {
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
     * @param orderNo  （shipuser|getuser）  订单号
     */
    public void addTell(MallRequest mRequest,String orderNo) {

        SPUtil sp = new SPUtil(mContext,SPUtil.USER);
        String uId = sp.getString(SPUtil.USER_UID,"");
        mRequest.changePrintStatus(uId,orderNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SaveOrderEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(SaveOrderEntity roundSiteEntity) {

                    }

                    @Override
                    public void onError(Throwable e) {

                        String strMsg = Constance.getMsgByException(e);
                        ToastUtil.showToast(mContext, strMsg, ToastUtil.TOAST_TYPE_ERRO);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }







}
