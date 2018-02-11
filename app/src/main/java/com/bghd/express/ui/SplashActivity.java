package com.bghd.express.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import com.bghd.express.R;

import java.lang.reflect.Field;

/**
 * Created by lixu on 2018/1/26.
 */

public class SplashActivity extends AppCompatActivity {
    private boolean inited = false;
    private CountDownTimer mTimer;
    private TextView tv_actionbar_line;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_actionbar_line = findViewById(R.id.activity_login_tv_xs);
        startTimer();
        initChenJin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(inited){
            startTimer();
        }
    }
    /**
     *
     */
    private void startTimer() {
        mTimer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long l) {

            }
            @Override
            public void onFinish() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                stopTimer();
                finish();
            }
        }.start();
    }


    private void stopTimer() {

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


    private void initChenJin(){
        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            tv_actionbar_line.setVisibility(View.VISIBLE);
            // tv_actionbar_line.getBackground().setAlpha(0);
            int statusHeight= getStatusBarHeight();
            android.view.ViewGroup.LayoutParams lp =tv_actionbar_line.getLayoutParams();
            lp.height =statusHeight;
        }
    }
    /**
     * 获取状态栏的高度
     * @return
     */
    private int getStatusBarHeight(){
        try
        {
            Class<?> c=Class.forName("com.android.internal.R$dimen");
            Object obj=c.newInstance();
            Field field=c.getField("status_bar_height");
            int x=Integer.parseInt(field.get(obj).toString());
            return  getResources().getDimensionPixelSize(x);
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}
