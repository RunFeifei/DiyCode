package com.example.root.okfit.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.dianrong.android.common.CrashHandler;
import com.dianrong.android.common.utils.Log;
import com.dianrong.android.common.viewholder.AutomaticViewHolderUtil;
import com.dianrong.crnetwork.error.ErrorCode;
import com.dianrong.crnetwork.response.RequestException;
import com.example.root.okfit.BuildConfig;
import com.example.root.okfit.uibinder.UiBinder;
import com.example.root.okfit.uibinder.UiBinderAgent;
import com.example.root.okfit.uibinder.UiBinderBatch;
import com.example.root.okfit.uibinder.UiBinderView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class CrBaseActivity extends RxAppCompatActivity implements UiBinderView, CrBaseFragment.AppFragmentCallback {


    private UiBinderAgent binderAgent;
    private Unbinder butterKinfeBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View content = getLayoutInflater().inflate(getContentViewId(), null);
        setContentView(content);
        butterKinfeBinder= ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        AutomaticViewHolderUtil.findAllViews(this, content);
        initVars();
        try {
            init(savedInstanceState);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.logStackTrace(e);
            }
            // TODO: 17-5-10 七牛
            CrashHandler.getInstance().saveCrashInfoToFile(e, false);
        }
        if (null != getIntent()) {
            handleIntent(getIntent());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        butterKinfeBinder.unbind();
    }

    private void initVars() {
        UiBinderAgent agent = UiBinderAgent.newInstance();
        agent.setUiBinderView(this);
        binderAgent = agent;
    }

    @LayoutRes
    protected abstract int getContentViewId();

    protected abstract void init(Bundle savedInstanceState);

    protected int getFragmentId() {
        return -1;
    }

    protected void handleIntent(Intent intent) {
    }

    protected void addFragment(Fragment fragment) {
        int fragmentId = getFragmentId();
        if (fragment == null || fragmentId == -1) {
            return;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(getFragmentId(), fragment, fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onUiBinderStart(int type) {
        if (type == UiBinder.BEHAVIOR_SILENCE) {
            return;
        }
    }

    @Override
    public void onUiBinderError(RequestException exception) {
        Log.e(getClass().getSimpleName(), exception.toString());
        switch (exception.getCode()) {
            case ErrorCode.NETWORK_ERR:
                break;
            case ErrorCode.RESPONSE_NULL_ERR:
                break;
            case ErrorCode.DR_CAST_ERR:
                break;
            case ErrorCode.DR_RELOGIN_ERR:
                break;
            case ErrorCode.UNKNOWN_ERR:
                break;
            default:
                break;
        }

    }

    @Override
    public void onUiBinderEnd(int type) {
        if (type == UiBinder.BEHAVIOR_SILENCE) {
            return;
        }
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
