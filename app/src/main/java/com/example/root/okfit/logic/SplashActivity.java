package com.example.root.okfit.logic;

import android.content.Intent;
import android.os.Bundle;

import com.example.root.okfit.R;
import com.example.root.okfit.base.BaseActivity;
import com.example.root.okfit.logic.main.MainActivity;

import butterknife.OnClick;

public class SplashActivity extends BaseActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @OnClick(R.id.button)
    protected void toMain() {
        startActivity(new Intent(this,MainActivity.class));
    }
}
