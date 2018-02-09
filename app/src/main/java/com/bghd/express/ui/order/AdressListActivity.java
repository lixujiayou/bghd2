package com.bghd.express.ui.order;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bghd.express.R;
import com.bghd.express.adapter.ProvinceAdapter;
import com.bghd.express.entiy.AdressEntity;
import com.bghd.express.model.AdressListModel;
import com.bghd.express.utils.base.BaseActivity;
import com.bghd.express.utils.base.SideBar;
import com.bghd.express.utils.tools.ToastUtil;
import com.bghd.express.utils.tools.ToolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixu on 2018/2/8.
 */

public class AdressListActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener{
    //当前id name
    public static String ADRESSLEVEL = "level";
    public static String ADRESSID = "level";
    public static String ADRESSNAME = "level_name";

    //用户选择的id name
    public static String ADRESSNAMELIST = "level_name_list";
    public static String ADRESSIDLIST = "level_id_list";

    //返回时的tag
    public static String FINISH_NAME = "f_name";
    public static String FINISH_ID = "f_id";
    public static int FINISH_CODE = 113;//正常返回
    public static int FINISH_CODE_CANCLE = 114;//手动返回


    private ProvinceAdapter adapter;
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private List<AdressEntity.DateBean> mAdressList = new ArrayList<>();



    private AdressListModel adressListModel;


    private String cAdressLevel = "0";
    private String cAdressName = "";

    //保存用户选择的地址
    //private ArrayList<String> mSelectIdList = new ArrayList<>();
    private ArrayList<String> mSelectNameList = new ArrayList<>();



    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.a_province);
    }

    @Override
    public void initViews() {
        sideBar= findViewById(R.id.sidrbar);
        dialog= findViewById(R.id.dialog);
        sortListView= findViewById(R.id.lv_pro);
        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position=adapter.getPositionForSection(s.charAt(0));
                if(position!=-1)
                {
                    sortListView.setSelection(position);
                }
            }
        });

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AdressEntity.DateBean adressTest = ((AdressEntity.DateBean) adapter.getItem(i));
                String nameTest;
//                if(ToolUtil.isEmpty(mSelectIdList)){
//                    mSelectIdList = new ArrayList<>();
//                }
                if(ToolUtil.isEmpty(mSelectNameList)){
                    mSelectNameList = new ArrayList<>();
                }

               // mSelectIdList.add(adressTest.getId());

                if(adressTest.getLevel().equals("1")){
                    nameTest = adressTest.getProvince();
                }else if(adressTest.getLevel().equals("2")){
                    nameTest = adressTest.getCity();
                }else{
                    nameTest = adressTest.getDistrict();
                }

                mSelectNameList.add(nameTest);

                if(((AdressEntity.DateBean) adapter.getItem(i)).getAa() != 0) {
                    Intent intent = new Intent();
                    intent.putExtra(ADRESSID, adressTest.getId());
                    intent.putExtra(ADRESSNAME, nameTest);
                    //intent.putStringArrayListExtra(ADRESSIDLIST, mSelectIdList);

                    intent.putExtra(ADRESSIDLIST, adressTest.getId());
                    intent.putStringArrayListExtra(ADRESSNAMELIST, mSelectNameList);
                    intent.setClass(AdressListActivity.this, AdressListActivity.class);
                    startActivityForResult(intent, 0);
                }else{
                    Intent fIntent = new Intent();
                    fIntent.putExtra(FINISH_NAME,nameTest);
                    fIntent.putExtra(FINISH_ID,adressTest.getId());
                    //fIntent.putStringArrayListExtra(ADRESSIDLIST, mSelectIdList);
                    fIntent.putStringArrayListExtra(ADRESSNAMELIST, mSelectNameList);
                    setResult(FINISH_CODE,fIntent);
                    finish();
                }
            }
        });

        adapter = new ProvinceAdapter(AdressListActivity.this,mAdressList);
        sortListView.setAdapter(adapter);

        adressListModel = ViewModelProviders.of(this).get(AdressListModel.class);
        adressListModel.getCurrentData(this).observe(this, new Observer<List<AdressEntity.DateBean>>() {
            @Override
            public void onChanged(@Nullable List<AdressEntity.DateBean> dateBeans) {
                mAdressList.clear();
                Log.d("qqqqq","返回数据"+dateBeans.size());
                if(!ToolUtil.isEmpty(dateBeans)){
                    mAdressList.addAll(dateBeans);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        adressListModel.setOnErroCallback(new AdressListModel.OnErroListener() {
            @Override
            public void onErro() {

            }
        });


        Intent gIntent = getIntent();
        cAdressLevel = gIntent.getExtras().getString(ADRESSID,"0");
        if(cAdressLevel == "0"){
            mToolbar.setTitle("地址选择");
        }else{
            cAdressName = gIntent.getExtras().getString(ADRESSNAME,"");
            //mSelectIdList = gIntent.getStringArrayListExtra(ADRESSIDLIST);
            mSelectNameList = gIntent.getStringArrayListExtra(ADRESSNAMELIST);
            mToolbar.setTitle(cAdressName);
        }
        mToolbar.setNavigationIcon(R.drawable.icon_cancle);
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public void initData() {
        adressListModel.loadAdressList(mRequestClient,String.valueOf(cAdressLevel));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == FINISH_CODE && data != null){
            setResult(FINISH_CODE,data);
            finish();
        }else if(requestCode == FINISH_CODE_CANCLE){
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adress, menu);

        return true;
    }
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_no_select:
                ToastUtil.showToast(AdressListActivity.this,"取消选择地址",ToastUtil.TOAST_TYPE_NOMAL);
                setResult(FINISH_CODE_CANCLE);
                finish();
                break;

        }
        return true;
    }
}
