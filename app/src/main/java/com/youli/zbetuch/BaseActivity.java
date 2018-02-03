package com.youli.zbetuch;

import android.app.Activity;
import android.os.Bundle;

import com.youli.zbetuch.ActivityControler;


public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityControler.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityControler.removeActivity(this);
    }
}
