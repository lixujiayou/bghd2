package com.bghd.express.ui.fragment.child;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.bghd.express.R;
import com.bghd.express.adapter.OrderListAdapter;
import com.bghd.express.core.Constance;
import com.bghd.express.entiy.OrderListEntity;
import com.bghd.express.entiy.SaveOrderEntity;
import com.bghd.express.entiy.eventbean.MainEvent;
import com.bghd.express.model.ChangePrintStatusModel;
import com.bghd.express.model.OrderOnLineListModel;
import com.bghd.express.ui.mine.print.Activity_DeviceList;
import com.bghd.express.utils.base.BaseFragment;
import com.bghd.express.utils.bluetooth.PrintUtil;
import com.bghd.express.utils.bluetooth.checkClick;
import com.bghd.express.utils.tools.ToolUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import HPRTAndroidSDKA300.HPRTPrinterHelper;

/**
 * Created by lixu on 2018/1/23.
 * 线上
 */

public class OnLineFragment extends BaseFragment implements View.OnClickListener{

    private RecyclerView recyclerView;
    private OrderListAdapter orderListAdapter;
    private LinearLayoutManager linearLayoutManager;

    private List<OrderListEntity.DataBean> orderList = new ArrayList<>();
    private OrderOnLineListModel orderListModel;

    private SwipeRefreshLayout swipeRefreshLayout;
    private  View emptyView;

    private int currentPage = 1;
    private boolean isFirstLoad = true;
    private ChangePrintStatusModel changePrintStatusModel;

    public OnLineFragment() {
    }

    public static OnLineFragment getInstance() {
        return answerFragmentHolder.instance;
    }

    public static class answerFragmentHolder {
        public static final OnLineFragment instance = new OnLineFragment();
    }



    @Override
    public void initTheme() {
    }

    @Override
    public int initCreatView() {
        return R.layout.fragment_online;
    }

    @Override
    public void initViews() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler);
        swipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swiperefresh);
         emptyView = mInflater.inflate(R.layout.layout_empty_view,null);

        orderListAdapter = new OrderListAdapter(R.layout.layout_order,orderList);
        orderListAdapter.openLoadAnimation();//动画

        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(orderListAdapter);

        changePrintStatusModel = ViewModelProviders.of(this).get(ChangePrintStatusModel.class);
        changePrintStatusModel.getCurrentData(mContext).observe(this, new Observer<SaveOrderEntity>() {
            @Override
            public void onChanged(@Nullable SaveOrderEntity saveOrderEntity) {

            }
        });
        orderListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Log.d("qqqqqqqq","onLoadMoreRequested");

                orderListModel.loadMyWorkList(mRequestClient
                        , Constance.REQUEST_DOWN_ORDER
                        ,currentPage++
                        ,Constance.ORDER_First_NUM);


            }
        },recyclerView);


        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeRefreshLayout.setRefreshing(true);
                orderListModel.loadMyWorkList(mRequestClient, Constance.REQUEST_ON_ORDER,1,Constance.ORDER_First_NUM);
            }
        });



        orderListModel = ViewModelProviders.of(this).get(OrderOnLineListModel.class);
        orderListModel.getCurrentData(mContext).observe(this, new Observer<List<OrderListEntity.DataBean>>() {
            @Override
            public void onChanged(@Nullable List<OrderListEntity.DataBean> dataBeans) {
                swipeRefreshLayout.setRefreshing(false);

                if(!ToolUtil.isEmpty(dataBeans)){
                    Log.d("qqqq","数据返回"+dataBeans.size());
                    if(currentPage != 1) {
                        orderListAdapter.loadMoreComplete();
                    }else{
                        orderList.clear();
                    }
                    orderList.addAll(dataBeans);
                    orderListAdapter.notifyDataSetChanged();
                }else{
                    orderListAdapter.loadMoreEnd();
                }

                if(isFirstLoad) {
                    orderListAdapter.setEmptyView(emptyView);
                    isFirstLoad = false;
                }
            }
        });
        orderListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (!checkClick.isClickEvent()) return;

                if (!HPRTPrinterHelper.IsOpened()) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        //校验是否已具有模糊定位权限
                        if (ContextCompat.checkSelfPermission(mContext,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(OnLineFragment.this.getActivity(),
                                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                    100);
                        } else {
                            //具有权限
                            Intent serverIntent = new Intent(mContext, Activity_DeviceList.class);
                            startActivityForResult(serverIntent, HPRTPrinterHelper.ACTIVITY_CONNECT_BT);
                            return;
                        }
                    } else {
                        //系统不高于6.0直接执行
                        Intent serverIntent = new Intent(mContext, Activity_DeviceList.class);
                        startActivityForResult(serverIntent, HPRTPrinterHelper.ACTIVITY_CONNECT_BT);
                    }
                }else{
                    changePrintStatusModel.addTell(mRequestClient,orderList.get(position).getOrder_no());
                    PrintUtil.printTest3(mContext,orderList.get(position));
                    orderList.get(position).setIs_print("1");
                    orderListAdapter.notifyDataSetChanged();
                }
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                orderListModel.loadMyWorkList(mRequestClient, Constance.REQUEST_ON_ORDER,currentPage,Constance.ORDER_First_NUM);
            }
        });

        orderListModel.setOnErroCallback(new OrderOnLineListModel.OnErroListener() {
            @Override
            public void onErro(int page,int type) {
                swipeRefreshLayout.setRefreshing(false);
                if(isFirstLoad) {
                    orderListAdapter.setEmptyView(emptyView);
                    isFirstLoad = false;
                }
                if(page != 1 && type == 2) {
                    orderListAdapter.loadMoreFail();
                }else if(page != 1 && type == 0){
                    orderListAdapter.loadMoreEnd();
                }
            }
        });
    }

    @Override
    public void initData() {
        //第一页Fragment可以加载  现无法解决  只能暂时这样
        swipeRefreshLayout.setRefreshing(true);
        orderListModel.loadMyWorkList(mRequestClient, Constance.REQUEST_ON_ORDER,1,Constance.ORDER_First_NUM);
    }
    @Override
    protected void lazyLoad() {

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MainEvent event) {

    }

    @Override
    public void onClick(View view) {

    }
}
