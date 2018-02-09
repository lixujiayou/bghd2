package com.bghd.express.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bghd.express.R;
import com.bghd.express.entiy.UserEntity;
import com.bghd.express.model.LoginModel;
import com.bghd.express.utils.base.BaseActivity;
import com.bghd.express.utils.tools.SPUtil;

/**
 * Created by lixu on 2018/2/6.
 */

public class LoginActivity extends BaseActivity{

    private Button btLogin;
    private LoginModel loginModel;
    private EditText etName,etPwd;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initViews() {
        btLogin = findViewById(R.id.bt_login);
        etName = findViewById(R.id.et_name);
        etPwd = findViewById(R.id.et_pwd);
        btLogin.setOnClickListener(this);


        SPUtil sha = new SPUtil(LoginActivity.this, SPUtil.USER);
        etName.setText(sha.getString(SPUtil.USER_NAME,""));
        etPwd.setText(sha.getString(SPUtil.USER_PWD,""));



        loginModel = ViewModelProviders.of(this).get(LoginModel.class);
        loginModel.setOnCallback(new LoginModel.OnUserListener() {
            @Override
            public void onSuccess(UserEntity userEntity) {
                Intent gIntent = new Intent(LoginActivity.this,Main.class);
                startActivity(gIntent);
                finish();
            }

            @Override
            public void onError(String erroMsg) {

            }
        });
    }

    @Override
    public void initData() {

    }




    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.bt_login:
                String name = etName.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                loginModel.login(LoginActivity.this,mRequestClient,name,pwd);
                break;
        }
    }
}
