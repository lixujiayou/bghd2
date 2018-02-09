package com.bghd.express.utils.tools;

import android.content.Context;

import com.bghd.express.R;
import com.cazaea.sweetalert.SweetAlertDialog;


/**
 * Created by lixu on 2017/12/8.
 */

public class DialogUtil {


        private DialogUtil(){
        }
        private static class DialogUtilHolder{
            private final static DialogUtil instance=new DialogUtil();
        }
        public static DialogUtil getInstance(){
            return DialogUtilHolder.instance;
        }

    private SweetAlertDialog pDialog;
    public void showProgressDialog(Context mContext,String title){
        try {
            pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(R.color.colorPrimary);
            pDialog.setTitleText(title);
            pDialog.setCancelable(false);
            pDialog.show();
        }catch (Exception e){}
    }

    public void dismissProgressDialog(){
        if(pDialog != null){
            pDialog.dismiss();
        }
    }

}
