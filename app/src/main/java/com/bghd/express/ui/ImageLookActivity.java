package com.bghd.express.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bghd.express.R;
import com.bumptech.glide.Glide;

import java.lang.reflect.Field;

/**
 * Created by lixu on 2018/1/29.
 */

public class ImageLookActivity extends Activity{
    public static final String IMAGE_INTENT = "image_intent";

    private ImageView ivCancle;
    private ImageView iv;
    private TextView tv_actionbar_line;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);


        tv_actionbar_line = findViewById(R.id.activity_login_tv_xs);
        iv = findViewById(R.id.img_img);
        ivCancle =findViewById(R.id.iv_cancle);


        Intent gIntent = getIntent();
        String url = gIntent.getExtras().getString(IMAGE_INTENT,"");

        Glide.with(ImageLookActivity.this)
                .load(url)
                .into(iv);


        ivCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initChenJin();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
