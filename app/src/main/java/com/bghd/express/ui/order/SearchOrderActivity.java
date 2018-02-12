package com.bghd.express.ui.order;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bghd.express.R;
import com.bghd.express.adapter.OrderListAdapter;
import com.bghd.express.adapter.SearchHistoryAdapter;
import com.bghd.express.core.Constance;
import com.bghd.express.entiy.OrderListEntity;
import com.bghd.express.model.OrderOnLineListModel;
import com.bghd.express.room_.AppDatabase;
import com.bghd.express.room_.User;
import com.bghd.express.utils.base.BaseActivity;
import com.bghd.express.utils.tools.StringUtils;
import com.bghd.express.utils.tools.ToastUtil;
import com.bghd.express.utils.tools.ToolUtil;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixu on 2018/2/5.
 */

public class SearchOrderActivity extends BaseActivity {
    private String DELETE = "delete";
    private String QUERY = "query";


    private ImageView ivCancle;
    private EditText etSearch;
    private AppDatabase db;

    /**
     * 搜索历史记录相关
     */
    private SearchHistoryAdapter searchHistoryAdapter;
    private List<User> tagList = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerViewHistory;
    private ImageView ivCleanHistory;

    private OrderOnLineListModel orderOnLineListModel;
    private int currentPage = 1;
    private RecyclerView recyclerView;
    private OrderListAdapter orderListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<OrderListEntity.DataBean> orderList = new ArrayList<>();
    private LinearLayout llHistory;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_order);
    }

    @Override
    public void initViews() {
        recyclerViewHistory = findViewById(R.id.recycler_history);
        ivCancle = findViewById(R.id.iv_cancle);
        etSearch = findViewById(R.id.et_search);
        ivCleanHistory = findViewById(R.id.iv_clean_history);
        recyclerView = findViewById(R.id.recycler);
        llHistory = findViewById(R.id.ll_history);


        ivCancle.setOnClickListener(this);
        ivCleanHistory.setOnClickListener(this);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String strs = etSearch.getText().toString().trim();
                    if (!StringUtils.isEmpty(strs)) {
                        if(llHistory.getVisibility() != View.GONE){
                            llHistory.setVisibility(View.GONE);
                        }
                        addAHistoty(strs);
                        showProgressDialog(SearchOrderActivity.this,"正在查询...");
                        orderOnLineListModel.loadMyWorkList(mRequestClient,Constance.REQUEST_ON_ORDER,currentPage, Constance.ORDER_First_NUM);
                    } else {
                        ToastUtil.showToast(SearchOrderActivity.this, "请输入订单号", ToastUtil.TOAST_TYPE_NOMAL);
                    }
                }
                return false;
            }
        });



        orderOnLineListModel = ViewModelProviders.of(SearchOrderActivity.this).get(OrderOnLineListModel.class);
        orderOnLineListModel.getCurrentData(SearchOrderActivity.this).observe(this, new Observer<List<OrderListEntity.DataBean>>() {
            @Override
            public void onChanged(@Nullable List<OrderListEntity.DataBean> dataBeans) {
                dismissProgressDialog();
                orderList.clear();
                orderList.addAll(dataBeans);
                searchHistoryAdapter.notifyDataSetChanged();
            }
        });
        orderOnLineListModel.setOnErroCallback(new OrderOnLineListModel.OnErroListener() {
            @Override
            public void onErro(int page, int type) {
                dismissProgressDialog();
            }
        });


        Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();


        orderListAdapter = new OrderListAdapter(R.layout.layout_order,orderList);
        orderListAdapter.openLoadAnimation();//动画
        linearLayoutManager = new LinearLayoutManager(SearchOrderActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(orderListAdapter);

    }

    @Override
    public void initData() {
        searchHistoryAdapter = new SearchHistoryAdapter(R.layout.item_search_tag, tagList);
        gridLayoutManager = new GridLayoutManager(SearchOrderActivity.this, 2);
        recyclerViewHistory.setLayoutManager(gridLayoutManager);
        recyclerViewHistory.setItemAnimator(new DefaultItemAnimator());
        recyclerViewHistory.setAdapter(searchHistoryAdapter);
        searchHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                etSearch.setText(tagList.get(position).getFirstName());
                //去搜索
            }
        });

        queryOrDeleteAllHistoty(QUERY);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_cancle:
                finish();
                break;
            case R.id.iv_clean_history:
                queryOrDeleteAllHistoty(DELETE);
                break;
        }
    }

    /**
     * 添加历史记录
     */
    private void addAHistoty(final String searchStr) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                db.userDao().insertAll(new User(searchStr));
                emitter.onNext(searchStr);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String s) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("qqqqq", "addhistory==erro==" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    /**
     * 查询历史记录
     */
    private void queryOrDeleteAllHistoty(final String type) {
        Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> emitter) throws Exception {

                List<User> usersTest = db.userDao().getAll();
                if (type.equals(DELETE)) {
                    db.userDao().delete(usersTest);
                }
                emitter.onNext(usersTest);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<List<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<User> s) {
                        tagList.clear();
                        if (!ToolUtil.isEmpty(s)) {
                            tagList.addAll(s);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("qqqqq", "查询history erro" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        searchHistoryAdapter.notifyDataSetChanged();
                    }
                });
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
