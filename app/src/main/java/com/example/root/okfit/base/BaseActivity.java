package com.example.root.okfit.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.crnetwork.error.ErrorCode;
import com.example.crnetwork.response.RequestException;
import com.example.root.okfit.uibinder.UiBinder;
import com.example.root.okfit.uibinder.UiBinderAgent;
import com.example.root.okfit.uibinder.UiBinderBatch;
import com.example.root.okfit.uibinder.UiBinderView;

/**
 * Created by PengFeifei on 17-4-27.
 */

public class BaseActivity extends AppCompatActivity implements UiBinderView{

    private UiBinderAgent binderAgent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiBinderAgent agent = UiBinderAgent.newInstance();
        agent.setUiBinderView(this);
        binderAgent = agent;
    }

    protected void log(String tag, String log) {
        Log.e("TAG-->" + tag, log);
    }

    @Override
    public void onUiBinderStart(int type) {
        log("","onUiBinderStart");
    }

    @Override
    public void onUiBinderError(RequestException exception) {
        log("","onUiBinderError");
    }

    @Override
    public void onUiBinderEnd(int type) {
        log("","onUiBinderEnd");
    }

    public UiBinderAgent getUiBinderAgent() {
        return binderAgent;
    }

    protected <T> UiBinder<T> getUiBinder() {
        return this.binderAgent.bornUiBinder();
    }

    protected UiBinderBatch getUiBinderBatch() {
        return this.binderAgent.bornUiBinderBatch();
    }


}
