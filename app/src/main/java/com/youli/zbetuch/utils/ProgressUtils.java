package com.youli.zbetuch.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by liutao on 2017/7/31.
 */

public class ProgressUtils {

    private static ProgressDialog progressDialog;


    public static void showMyProgressDialog(Context context){

        progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("正在加载中...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void dismissMyProgressDialog(Context context){
        if (progressDialog!= null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog=null;
        }
    }

}
