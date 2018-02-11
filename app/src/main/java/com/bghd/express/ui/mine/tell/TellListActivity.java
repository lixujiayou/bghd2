package com.bghd.express.ui.mine.tell;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.bghd.express.R;
import com.bghd.express.adapter.TellListAdapter;
import com.bghd.express.core.Constance;
import com.bghd.express.entiy.SaveOrderEntity;
import com.bghd.express.entiy.TellEntity;
import com.bghd.express.model.RemoveTellModel;
import com.bghd.express.model.TellListModel;
import com.bghd.express.ui.order.AdressListActivity;
import com.bghd.express.ui.order.SearchOrderActivity;
import com.bghd.express.utils.base.BaseActivity;
import com.bghd.express.utils.base.DeletableEditText;
import com.bghd.express.utils.tools.StringUtils;
import com.bghd.express.utils.tools.ToastUtil;
import com.bghd.express.utils.tools.ToolUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixu on 2018/2/8.
 */

public class TellListActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener{

    public static String TELL_TYPE = "type";  //0:寄件  1：收件
    public static String TELL_TYPE_SEND = "0"; //寄件
    public static String TELL_TYPE_ACCPET = "1"; //收件

    public static String TELL_STATUS = "status";
    public static String TELL_STATUS_MINE = "0"; //我的进入
    public static String TELL_STATUS_SELECT = "1";  // 选择进入



    public static String FINISH_TELL = "finish_tell";
    public static int FINISH_CODE = 133;  // 返回



    private String mTellType;
    private String mTellStatus;

    private String shipuser = "shipuser";  // 寄件人
    private String getuser = "getuser";  // 收件人


    private String queryStatus;




    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TellListAdapter tellListAdapter;
    private List<TellEntity.DateBean> tellList = new ArrayList<>();


    private TellListModel tellListModel;
    private RemoveTellModel removeTellModel;

    private DeletableEditText etSearch;
    private int page = 1;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_tell_list);
    }

    @Override
    public void initViews() {
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        recyclerView = findViewById(R.id.recycler);
        etSearch = findViewById(R.id.et_search);




        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String strs = etSearch.getText().toString().trim();
                    if (!StringUtils.isEmpty(strs)) {
                        tellListModel.loadTellList(mRequestClient
                                ,String.valueOf(page)
                                ,String.valueOf(Constance.ORDER_First_NUM)
                                ,queryStatus
                                ,strs);
                    } else {
                        ToastUtil.showToast(TellListActivity.this, "请输入姓名", ToastUtil.TOAST_TYPE_NOMAL);
                    }
                }
                return false;
            }
        });


        Intent gIntent = getIntent();
        mTellType = gIntent.getExtras().getString(TELL_TYPE);
        if(mTellType.equals(TELL_TYPE_SEND)){
            mToolbar.setTitle("我的寄件人");
            queryStatus = shipuser;
        }else{
            mToolbar.setTitle("我的收件人");
            queryStatus = getuser;
        }
        Log.d("qqqq","mTellType=="+mTellType);
        mToolbar.setNavigationIcon(R.drawable.icon_cancle);
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(this);

        mTellStatus = gIntent.getExtras().getString(TELL_STATUS);
        if(mTellType.equals(TELL_STATUS_MINE)){

        }else{

        }
        tellListAdapter = new TellListAdapter(TellListActivity.this,R.layout.item_tell_list,tellList,mTellType);

        tellListAdapter.openLoadAnimation();//动画

        linearLayoutManager  = new LinearLayoutManager(TellListActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);





        //滑动删除和拖拽init
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(tellListAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);



        // 开启滑动删除
        tellListAdapter.enableSwipeItem();
        tellListAdapter.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
                //滑动完毕后调用
                //Log.d("qqqqq","clearView"+pos);
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                //删除后调用
                removeTellModel.removeTell(mRequestClient,tellList.get(pos).getId(),true);
                Log.d("qqqqq","onItemSwiped"+pos);
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

            }
        });


        //滑动最后一个Item的时候回调onLoadMoreRequested方法
        tellListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Log.d("qqqqq","onLoadMoreRequested到底部开始加载");
            }
        },recyclerView);
        // 没有数据的时候默认显示该布局
        //tellListAdapter.setEmptyView(getView());
        tellListAdapter.disableLoadMoreIfNotFullPage();




        tellListModel = ViewModelProviders.of(TellListActivity.this).get(TellListModel.class);
        tellListModel.getCurrentData(TellListActivity.this).observe(this, new Observer<List<TellEntity.DateBean>>() {
            @Override
            public void onChanged(@Nullable List<TellEntity.DateBean> dateBeans) {
                swipeRefreshLayout.setRefreshing(false);
                tellList.clear();
                if(!ToolUtil.isEmpty(dateBeans)){
                    tellList.addAll(dateBeans);
                    tellListAdapter.notifyDataSetChanged();
                }
            }
        });

        tellListModel.setOnErroCallback(new TellListModel.OnErroListener() {
            @Override
            public void onErro() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        recyclerView.setAdapter(tellListAdapter);
        tellListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(mTellStatus.equals(TELL_STATUS_MINE)){
                    Log.d("qqqqq","TELL_TYPE"+mTellType);
                    Log.d("qqqqq","TELL_STATUS"+AddTellActivity.TELL_STATUS_EDIT);
                    Intent eIntent = new Intent(TellListActivity.this,AddTellActivity.class);
                    eIntent.putExtra(AddTellActivity.TELL_TYPE,mTellType);
                    eIntent.putExtra(AddTellActivity.TELL_STATUS,AddTellActivity.TELL_STATUS_EDIT);
                    eIntent.putExtra(AddTellActivity.ADRESS_INFO,tellList.get(position));
                    startActivityForResult(eIntent,1);
                }else{
                    Intent fIntent = new Intent();
                    fIntent.putExtra(FINISH_TELL,tellList.get(position));
                    setResult(FINISH_CODE,fIntent);
                    finish();
                }
            }
        });
        removeTellModel = ViewModelProviders.of(TellListActivity.this).get(RemoveTellModel.class);
        removeTellModel.getCurrentData(TellListActivity.this).observe(this, new Observer<SaveOrderEntity>() {
            @Override
            public void onChanged(@Nullable SaveOrderEntity saveOrderEntity) {
            }
        });



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tellListModel.loadTellList(mRequestClient
                        ,String.valueOf(page)
                        , String.valueOf(Constance.ORDER_First_NUM)
                        ,queryStatus
                        ,etSearch.getText().toString().trim());
            }
        });

        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void initData() {
        tellListModel.loadTellList(mRequestClient
                ,String.valueOf(page)
                , String.valueOf(Constance.ORDER_First_NUM)
                ,queryStatus
                ,etSearch.getText().toString().trim());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tell, menu);
        return true;
    }
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_add:
                Intent eIntent = new Intent(TellListActivity.this,AddTellActivity.class);
                eIntent.putExtra(AddTellActivity.TELL_TYPE,mTellType);
                eIntent.putExtra(AddTellActivity.TELL_STATUS,AddTellActivity.TELL_STATUS_ADD);
                startActivityForResult(eIntent,1);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == AddTellActivity.FINISH_RESH_CODE){
            swipeRefreshLayout.setRefreshing(true);
            page = 1;
            tellListModel.loadTellList(mRequestClient
                    ,String.valueOf(page)
                    , String.valueOf(Constance.ORDER_First_NUM)
                    ,queryStatus
                    ,etSearch.getText().toString().trim());
        }
    }
}
