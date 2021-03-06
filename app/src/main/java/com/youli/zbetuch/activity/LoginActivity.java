package com.youli.zbetuch.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.youli.zbetuch.BaseActivity;
import com.youli.zbetuch.R;
import com.youli.zbetuch.utils.OkHttpUtils;
import com.youli.zbetuch.utils.SharedPreferencesUtils;
import com.youli.zbetuch.utils.ToastUtils;
import com.youli.zbetuch.utils.UpdateManager;

import java.io.IOException;

import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final int RESPONSE_RETURN = 0;
    private static final int RESPONSE_ERROR = 1;
    private static final int COOKIE_NULL = 2;
    private EditText nameEt, passwordEt;
    private String TAG = "LOGINACTIVITY";
    private Button loginBtn;
    private ProgressDialog dialog;
    private AlertDialog.Builder builder;

    private String nameStr, passwordStr;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            switch (msg.what) {
                case RESPONSE_RETURN:
                    if (msg.obj.equals("true")) {
                        Intent intent = new Intent(LoginActivity.this, MainPagerActivity.class);

                        startActivity(intent);
                        //保存用户名到本地
                        SharedPreferencesUtils.putString("userName", nameStr, getApplicationContext());

                        finish();
                    } else {
                        ToastUtils.showToast(getApplicationContext(), "账号或者密码有误,请重新输入");
                        passwordEt.setText("");

                    }
                    break;
                case RESPONSE_ERROR:
                    ToastUtils.showToast(getApplicationContext(), "请检查网络");
                    break;
                case COOKIE_NULL:
                    ToastUtils.showToast(getApplicationContext(), "账号或者密码有误,请重新输入");
                    passwordEt.setText("");
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        checkInternet();
    }

    private void checkInternet() {
        if (!isConnected()) {
            builder = new AlertDialog.Builder(this);
            builder.setCancelable(false)
                    .setTitle("警告")
                    .setMessage("请检查网络设置，无法正确联网")
                    .setPositiveButton("前去联网", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (isConnected()) {
                                initView();
                            } else {
                                Intent intent = new Intent("android.settings.WIFI_SETTINGS");
                                startActivity(intent);
                                checkInternet();
                            }
                        }
                    })
                    .setNegativeButton("稍后再联", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            builder.show();
        } else {
            // 更新apk
            UpdateManager manager = new UpdateManager(LoginActivity.this);
            manager.checkUpdate();
            initView();
        }

    }

    //初始化控件
    private void initView() {
        nameEt = (EditText) findViewById(R.id.name_et);
        passwordEt = (EditText) findViewById(R.id.password_et);
        loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);
        //判断本地是否有账户保存
        String localUserName = SharedPreferencesUtils.getString("userName", getApplicationContext());
        if (localUserName != null) {
            nameEt.setText(localUserName);
        }
    }

    @Override
    public void onClick(View v) {
        nameStr = nameEt.getText().toString().trim();
        passwordStr = passwordEt.getText().toString().trim();
        switch (v.getId()) {
            case R.id.login_btn:
                if (!TextUtils.isEmpty(nameStr) && !TextUtils.isEmpty(passwordStr)) {
                    //开启线程判断账号密码是否正确
                    judgeUserInfo(nameStr, passwordStr);
                } else {
                    Log.e(TAG, "name: " + nameStr);
                    Log.e(TAG, "password: " + passwordStr);
                    ToastUtils.showToast(this, "用户名和密码不能为空");
                    return;
                }
                dialog = new ProgressDialog(this);
                dialog.setTitle("请稍等。。。");
                dialog.setCancelable(false);
                dialog.show();

                break;
            default:
                break;
        }
    }

    /**
     * 判断账号密码是否正确
     * @param nameStr     用户名
     * @param passwordStr 密码
     */
    private void judgeUserInfo(final String nameStr, final String passwordStr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = OkHttpUtils.BaseUrl + "/JSON/Login/Json_Login.aspx?LoginNo=" + nameStr + "&LoginPwd=" + passwordStr;
                    Response response = OkHttpUtils.okHttpGet(url.trim());
                    Message msg = Message.obtain();
                    if (response != null) {
                       //截取 cookies
                        if(response.header("Set-Cookie")==null){
                            Message mage=Message.obtain();
                            mage.what=COOKIE_NULL;
                            mHandler.sendMessage(mage);
                            return;
                        }
                        String cookies = response.header("Set-Cookie").toString();

                        Log.e("2017/7/31","cookies="+cookies);

                        String newCookies = cookies.substring(0, cookies.indexOf(";"));
                        SharedPreferencesUtils.putString("cookies", newCookies, LoginActivity.this);
                        Log.e(TAG, "cookies:"+newCookies );

                        String reponseReturn = response.body().string();
                        Log.e(TAG, "run: " + reponseReturn);
                        msg.what = RESPONSE_RETURN;
                        msg.obj = reponseReturn;
                    } else {
                        msg.what = RESPONSE_ERROR;
                    }
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //判断是否联网
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }
}
