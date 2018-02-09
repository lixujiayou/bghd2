package com.bghd.express.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bghd.express.R;
import com.bghd.express.entiy.eventbean.MainEvent;
import com.bghd.express.ui.mine.tell.TellListActivity;
import com.bghd.express.ui.order.PlaceOrderActivity;
import com.bghd.express.utils.base.BaseFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by lixu on 2018/1/23.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener{
    private RelativeLayout rlSet;
    private LinearLayout llAboutWe;
    private LinearLayout llRankList;
    private LinearLayout llResultList;
    private LinearLayout llIdea;


    private LinearLayout llAPerson,llSPerson;

    private TextView mTooBarTitle;


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
        llAPerson.setOnClickListener(this);
        llSPerson.setOnClickListener(this);



        mTooBarTitle = mView.findViewById(R.id.toolbar_title);
        mTooBarTitle.setText("我的");
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
        }
    }
}
