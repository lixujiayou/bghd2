package com.bghd.express.ui.mine.print;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.bghd.express.R;
import com.bghd.express.utils.base.BaseActivity;

/**
 * Created by lixu on 2018/2/10.
 * 连接打印机
 */

public class PrintAboutActivity extends BaseActivity{

    private LinearLayout llConnect,llBreak;


    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_print_about);
    }

    @Override
    public void initViews() {
        llConnect = findViewById(R.id.ll_print_connect);
        llBreak = findViewById(R.id.ll_print_break);
        llConnect.setOnClickListener(this);
        llBreak.setOnClickListener(this);


        mToolbar.setTitle("关于打印机");
        mToolbar.setNavigationIcon(R.drawable.icon_cancle);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.ll_print_connect:
                break;
            case R.id.ll_print_break:
                break;
        }

    }
}
