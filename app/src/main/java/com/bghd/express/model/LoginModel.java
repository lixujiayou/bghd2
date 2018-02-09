package com.bghd.express.model;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.bghd.express.R;
import com.bghd.express.core.Constance;
import com.bghd.express.core.MallRequest;
import com.bghd.express.entiy.UserEntity;
import com.bghd.express.utils.base.BaseViewModel;
import com.bghd.express.utils.tools.SPUtil;
import com.bghd.express.utils.tools.ToastUtil;
import com.cazaea.sweetalert.SweetAlertDialog;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixu on 2018/1/22.
 */

public class LoginModel extends BaseViewModel {
    private String TAG = "LoginModel";
    private Context mContext;
    private OnUserListener onCallBackListener;
    private String userToken;

    public LoginModel(Application application) {
        super(application);
    }



    public void login(Context context, final MallRequest mRequestClient, final String name, final String pwd){
        this.mContext = context;
       // DialogUtil.getInstance().showProgressDialog(mContext, "正在登陆");
        showProgressDialog(mContext, "正在登陆");
        mRequestClient.login(name,pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<UserEntity>() {
                    @Override
                    public void accept(UserEntity userEntity) throws Exception {
                    }
                })
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserEntity>() {
                    @Override
                    public void accept(UserEntity userEntity) throws Exception {
                        dismissProgressDialog();
                        if(userEntity.getStatus() == Constance.REQUEST_SUCCESS_CODE){
                            saveSomeSth(userEntity,name,pwd);
                            onCallBackListener.onSuccess(userEntity);
                        }else{
                            ToastUtil.showToast(mContext,userEntity.getInfo(),ToastUtil.TOAST_TYPE_ERRO);
                            onCallBackListener.onError(userEntity.getInfo());
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dismissProgressDialog();
                        String erroMst = Constance.getMsgByException(throwable);
                        onCallBackListener.onError(erroMst);
                        ToastUtil.showToast(mContext,erroMst,ToastUtil.TOAST_TYPE_ERRO);
                    }
                });

    }


    private void saveSomeSth(UserEntity userEntity,String name,String pwd){
        SPUtil sha = new SPUtil(mContext, SPUtil.USER);
        sha.putString(SPUtil.USER_NAME,name);
        sha.putString(SPUtil.USER_PWD,pwd);
        sha.putString(SPUtil.USER_UID,userEntity.getData().getUid());
        sha.putString(SPUtil.USER_TOKEN,userToken);

    }


    public void setOnCallback(OnUserListener onItemClickListener) {
        this.onCallBackListener = onItemClickListener;
    }
    public interface OnUserListener {
        void onSuccess(UserEntity userEntity);
        void onError(String erroMsg);
    }


    private SweetAlertDialog pDialog;
    public void showProgressDialog(Context mContext,String title){
        try {
            pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(R.color.colorPrimary);
            pDialog.setTitleText(title);
            pDialog.setCancelable(false);
            pDialog.show();
        }catch (Exception e){}
    }

    public void dismissProgressDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }
}
