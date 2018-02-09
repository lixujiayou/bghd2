package com.bghd.express.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bghd.express.R;
import com.bghd.express.ui.fragment.OrderFragment;
import com.bghd.express.ui.fragment.MineFragment;
import com.bghd.express.utils.base.BaseFragmentActivity;
import com.bghd.express.utils.tools.ToastUtil;

/**
 * Created by lixu on 2018/2/5.
 */

public class Main extends BaseFragmentActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener{
    LinearLayout ll_answer;
    LinearLayout ll_mine;
    TextView tv_answer;
    TextView tv_mine;
    ImageView iconMAnswer;
    ImageView iconMMine;
    private FragmentManager fragmentManager;
    private OrderFragment orderFragment;
    private MineFragment mineFragment;

 //   private Toolbar mToolbar;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initViews() {
        ll_answer = findViewById(R.id.ll_main_answer);
        ll_mine = findViewById(R.id.ll_main_mine);
        tv_answer = findViewById(R.id.tv_answer);
        tv_mine = findViewById(R.id.tv_mine);
        iconMAnswer = findViewById(R.id.icon_m_answer);
        iconMMine = findViewById(R.id.icon_m_mine);


        ll_answer.setOnClickListener(this);
        ll_mine.setOnClickListener(this);
    }

    @Override
    public void initData() {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);

        if (orderFragment == null) {
            orderFragment = OrderFragment.getInstance();
            transaction.add(R.id.framelayout, orderFragment);
        } else {
            transaction.show(orderFragment);
        }

        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_main_answer:
                changePage(0);
                OnTabSelected(0);
                break;
            case R.id.ll_main_mine:
                changePage(2);
                OnTabSelected(2);
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (orderFragment != null) {
            transaction.hide(orderFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
    }
    private void changePage(int postion) {
        switch (postion) {
            case 0:
                tv_answer.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                tv_mine.setTextColor(ContextCompat.getColor(this, R.color.textcolor_m));
                iconMAnswer.setImageDrawable(ContextCompat.getDrawable(Main.this, R.drawable.icon_answer_p));
                iconMMine.setImageDrawable(ContextCompat.getDrawable(Main.this, R.drawable.icon_mine));
                break;
            case 2:
                tv_answer.setTextColor(ContextCompat.getColor(this, R.color.textcolor_m));
                tv_mine.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                iconMAnswer.setImageDrawable(ContextCompat.getDrawable(Main.this, R.drawable.icon_answer));
                iconMMine.setImageDrawable(ContextCompat.getDrawable(Main.this, R.drawable.icon_mine_p));
                break;
        }
    }
    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.showToast(Main.this, "再按一次退出APP", ToastUtil.TOAST_TYPE_NOMAL);
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    private void OnTabSelected(int index) {


        supportInvalidateOptionsMenu();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (index) {
            case 0:
                    hideFragments(transaction);
                    if (orderFragment == null) {
                        orderFragment = OrderFragment.getInstance();
                        transaction.add(R.id.framelayout, orderFragment);
                    } else {
                        transaction.show(orderFragment);
                    }

                break;
            case 2:
                hideFragments(transaction);
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    transaction.add(R.id.framelayout, mineFragment);
                } else {
                    transaction.show(mineFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

}
