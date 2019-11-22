package com.bghd.express.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bghd.express.R;
import com.bghd.express.entiy.UserEntity;
import com.bghd.express.model.LoginModel;
import com.bghd.express.utils.base.BaseActivity;
import com.bghd.express.utils.tools.SPUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

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
                AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(LoginActivity.this);
                alertdialogbuilder.setMessage(erroMsg);
                alertdialogbuilder.setPositiveButton("知道了", null);
                AlertDialog alertdialog1 = alertdialogbuilder.create();
                alertdialog1.show();

            }
        });
        aboutPermission();
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

    private void aboutPermission(){
        AndPermission.with(LoginActivity.this)
                .requestCode(100)
                .permission(Permission.CAMERA
                        ,Permission.LOCATION
                        ,Permission.STORAGE
                        ,Permission.CALENDAR
                        ,Permission.SENSORS
                        ,Permission.PHONE
                        ,Permission.SMS
                )
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 此对话框可以自定义，调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(LoginActivity.this, rationale).show();
                    }
                })
                .callback(permissionListener)
                .start();
    }


    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。

            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if(requestCode == 100) {
                // TODO ...
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if(requestCode == 100) {
                // 是否有不再提示并拒绝的权限。
                if (AndPermission.hasAlwaysDeniedPermission(LoginActivity.this, deniedPermissions)) {
                    // 第二种：用自定义的提示语。
                    /*AndPermission.defaultSettingDialog(LoginActivity.this, 400)
                            .setTitle("权限申请失败")
                            .setMessage("您拒绝了我们必要的一些权限，无法正常使用抄表APP，请在设置中授权！")
                            .setPositiveButton("好，去设置")
                            .show();*/
                }
            }
        }
    };
}
