package com.example.root.okfit.base;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;

import com.dianrong.android.common.CrashHandler;
import com.dianrong.android.common.utils.Log;
import com.example.root.okfit.BuildConfig;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class CrBaseActivity extends BasePermissionsActivity {
    private Unbinder butterKinfeBinder;

    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View content = getLayoutInflater().inflate(getContentViewId(), null);
        setContentView(content);
        butterKinfeBinder = ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
    protected String[] getPermissions() {
        return new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        };
    }
}
