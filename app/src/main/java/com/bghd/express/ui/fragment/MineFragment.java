package com.bghd.express.ui.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bghd.express.R;
import com.bghd.express.entiy.SaveOrderEntity;
import com.bghd.express.entiy.eventbean.MainEvent;
import com.bghd.express.model.RiJieModel;
import com.bghd.express.ui.LoginActivity;
import com.bghd.express.ui.fragment.child.OffLineFragment;
import com.bghd.express.ui.mine.print.PrintAboutActivity;
import com.bghd.express.ui.mine.tell.TellListActivity;
import com.bghd.express.ui.order.PlaceOrderActivity;
import com.bghd.express.utils.base.BaseFragment;
import com.bghd.express.utils.tools.SPUtil;
import com.bghd.express.utils.tools.ToastUtil;
import com.cazaea.sweetalert.SweetAlertDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by lixu on 2018/1/23.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener{


    private LinearLayout llAPerson,llSPerson,llPrintAbout,llOver;
    private Button btLoginOut;

    private TextView mTooBarTitle;

    private RiJieModel riJieModel;


    public MineFragment() {
    }

    public static MineFragment getInstance() {
        return answerFragmentHolder.instance;
    }

    public static class answerFragmentHolder {
        public static final MineFragment instance = new MineFragment();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void initTheme() {
    }

    @Override
    public int initCreatView() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initViews() {
        llAPerson = mView.findViewById(R.id.ll_accpet_person);
        llSPerson = mView.findViewById(R.id.ll_send_person);
        llPrintAbout = mView.findViewById(R.id.ll_print_about);
        btLoginOut = mView.findViewById(R.id.bt_login_out);
        llOver = mView.findViewById(R.id.ll_over);
        llAPerson.setOnClickListener(this);
        llSPerson.setOnClickListener(this);
        llPrintAbout.setOnClickListener(this);
        btLoginOut.setOnClickListener(this);
        llOver.setOnClickListener(this);

        SPUtil sp = new SPUtil(mContext,SPUtil.USER);
        if(sp.getInt(SPUtil.USER_RIJIE,0) != 1){
            llOver.setVisibility(View.GONE);
        }


        mTooBarTitle = mView.findViewById(R.id.toolbar_title);
        mTooBarTitle.setText("我的");


        riJieModel = ViewModelProviders.of(this).get(RiJieModel.class);
        riJieModel.getCurrentData(mContext).observe(this, new Observer<SaveOrderEntity>() {
            @Override
            public void onChanged(@Nullable SaveOrderEntity saveOrderEntity) {
                ToastUtil.showToast(mContext,"日结成功",ToastUtil.TOAST_TYPE_SUCCESS);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MainEvent event) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_accpet_person:
                Intent paIntent = new Intent(mContext, TellListActivity.class);
                paIntent.putExtra(TellListActivity.TELL_TYPE, TellListActivity.TELL_TYPE_ACCPET);
                paIntent.putExtra(TellListActivity.TELL_STATUS, TellListActivity.TELL_STATUS_MINE);
                startActivity(paIntent);
                break;
            case R.id.ll_send_person:
                Intent psIntent = new Intent(mContext, TellListActivity.class);
                psIntent.putExtra(TellListActivity.TELL_TYPE, TellListActivity.TELL_TYPE_SEND);
                psIntent.putExtra(TellListActivity.TELL_STATUS, TellListActivity.TELL_STATUS_MINE);
                startActivity(psIntent);
                break;
            case R.id.ll_print_about:
                Intent pIntent = new Intent(mContext, PrintAboutActivity.class);
                startActivity(pIntent);
                break;
            case R.id.bt_login_out:
                isLoginOut();
                break;
                //日结
            case R.id.ll_over:
                rijie();
                break;
        }
    }

    private void isLoginOut(){
        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("即将退出当前登录账号")
               // .setContentText("即将退出当前登录账号")
                .setConfirmText("继续")
                .setCancelText("取消")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Intent lIntent = new Intent(mContext, LoginActivity.class);
                        startActivity(lIntent);
                        getActivity().finish();


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



    private void rijie(){
        riJieModel.rijieStart(mRequestClient);
    }



}
