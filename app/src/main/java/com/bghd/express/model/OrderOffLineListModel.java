package com.bghd.express.model;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;


import com.bghd.express.core.Constance;
import com.bghd.express.core.MallRequest;
import com.bghd.express.entiy.OrderListEntity;
import com.bghd.express.utils.base.BaseViewModel;
import com.bghd.express.utils.tools.SPUtil;
import com.bghd.express.utils.tools.ToastUtil;
import com.bghd.express.utils.tools.ToolUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixu on 2018/1/23.
 * @author lixu
 */

public class OrderOffLineListModel extends BaseViewModel {
    public OrderOffLineListModel(Application application) {
        super(application);
    }

    private MutableLiveData<List<OrderListEntity.DataBean>> roundSiteList;
    private Context mContext;


    public MutableLiveData<List<OrderListEntity.DataBean>> getCurrentData(Context context) {
        this.mContext = context;
        if (roundSiteList == null) {
            roundSiteList = new MutableLiveData<>();
        }
        return roundSiteList;
    }

    public void loadMyWorkList(MallRequest mRequest, String type, final int page, int size){
        //DialogUtil.getInstance().showProgressDialog(mContext, "正在查询附近站点...");
        String userName = new SPUtil(mContext,SPUtil.USER).getString(SPUtil.USER_UID,"");
        mRequest.getOrderList(userName,type,page,size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrderListEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                       // EventBus.getDefault().post(new MyEvent(MyEvent.OKK));
                    }

                    @Override
                    public void onNext(OrderListEntity roundSiteEntity) {
                        if(roundSiteList == null) {
                            roundSiteList = new MutableLiveData<>();
                        }

                        if(!ToolUtil.isEmpty(roundSiteEntity.getData())){
                            Log.d("qqqq","数据load"+roundSiteEntity.getData().size());
                            List<OrderListEntity.DataBean> sites = roundSiteEntity.getData();
//                            if(!ToolUtil.isEmpty(roundSiteList.getValue()) && page != 1){
//                                List<OrderListEntity.DataBean> sitesOld = roundSiteList.getValue();
//                                sitesOld.addAll(sites);
//                                roundSiteList.postValue(sitesOld);
//                            }else {
                                roundSiteList.postValue(sites);
//                            }

                        }else{
                            onCallBackListener.onErro(page,0);
                            if(page == 1) {
                                ToastUtil.showToast(mContext, "未查询到相关信息", ToastUtil.TOAST_TYPE_WARNING);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        onCallBackListener.onErro(page,2);
                        String strMsg = Constance.getMsgByException(e);
                        ToastUtil.showToast(mContext,strMsg,ToastUtil.TOAST_TYPE_WARNING);
                      //  EventBus.getDefault().post(new MyEvent(MyEvent.NOO));
                        //DialogUtil.getInstance().dismissProgressDialog();
                    }

                    @Override
                    public void onComplete() {
                       // onCallBackListener.onErro();
                      //  EventBus.getDefault().post(new MyEvent(MyEvent.NOO));
                        //DialogUtil.getInstance().dismissProgressDialog();
                    }
                });
    }




    private OnErroListener onCallBackListener;
    public void setOnErroCallback(OnErroListener onItemClickListener) {
        this.onCallBackListener = onItemClickListener;
    }
    public interface OnErroListener {
        void onErro(int page,int type);
    }



}
