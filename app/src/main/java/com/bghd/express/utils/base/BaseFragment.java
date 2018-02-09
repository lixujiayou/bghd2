package com.bghd.express.utils.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bghd.express.core.MallRequest;
import com.bghd.express.utils.converter.ServiceGenerator;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Administrator on 2016/12/8 0008.
 */

public abstract class BaseFragment extends Fragment {

    public View mView;
    public Context mContext;
    private int layoutID;
    public LayoutInflater mInflater;
    public Bundle mSavedInstanceState;
    public MallRequest mRequestClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mSavedInstanceState = savedInstanceState;
        mInflater = inflater;
        layoutID = initCreatView();
        mView = inflater.inflate(layoutID, container, false);
        mContext = getActivity();
        EventBus.getDefault().register(this);
        mRequestClient = ServiceGenerator.createService(MallRequest.class);
        initViews();
        isPrepared = true;
        initData();
        return mView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * Fragment当前状态是否可见
     */

    protected boolean isVisible;
    protected boolean isFirst = true;
    protected boolean isPrepared = false;


    @Override
    public void
    setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            if(isFirst && isPrepared) {
                onVisible();
            }
        } else {
            isVisible = false;
            onInvisible();
        }

    }


    /**
     * 可见
     */

    protected void onVisible() {
        isFirst = false;
        lazyLoad();

    }


    /**
     * 不可见
     */

    protected void onInvisible() {


    }


    /**
     * 延迟加载
     * <p>
     * <p>
     * 子类必须重写此方法
     */

    protected abstract void lazyLoad();


    public abstract void initTheme();

    public abstract int initCreatView();

    public abstract void initViews();

    public abstract void initData();

}
