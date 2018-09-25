package com.example.root.okfit.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.okfit.R;
import com.example.root.okfit.view.loading.LoadingDialog;
import com.fei.crnetwork.framework.view.IBaseView;
import com.fei.crnetwork.framework.view.loading.Loading;
import com.fei.crnetwork.response.RequestException;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends BasePermissionsActivity implements IBaseView {

    private Unbinder butterKinfeBinder;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = getLayoutInflater().inflate(R.layout.activity_root, null);
        getLayoutInflater().inflate(this.getContentLayoutId(), (ViewGroup) rootView.findViewById(R.id.layContent), true);
        setContentView(rootView);
        butterKinfeBinder = ButterKnife.bind(this);
        initToolbar();
        init(savedInstanceState);
        handleIntent(getIntent());
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        butterKinfeBinder.unbind();
    }

    @LayoutRes
    protected abstract int getContentLayoutId();

    protected abstract void init(Bundle savedInstanceState);

    protected void handleIntent(Intent intent) {
    }

    @TargetApi(17)
    public boolean isViewVisible() {
        return !(isDestroyed() || isFinishing());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    /*****************************Fragment*****************************/
    protected int getFragmentId() {
        return -1;
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


    /*****************************Toolbar*****************************/
    private void initToolbar() {
        Toolbar toolbar = setToolBar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            return;
        }
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        boolean hasToolbar = hasToolbar();
        if (!hasToolbar) {
            return;
        }
        toolbar.setVisibility(View.VISIBLE);

        toolbar.setBackgroundColor(setToolBarColor());
        TextView textTitle = (TextView) rootView.findViewById(R.id.textTitle);
        setToolBarTitle(textTitle);
        TextView menu = ((TextView) toolbar.findViewById(R.id.textMenu));
        setToolbarMenu(menu);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(enableHomeArrow());
            actionBar.setDisplayHomeAsUpEnabled(enableHomeArrow());
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    protected boolean hasToolbar() {
        return true;
    }

    protected Toolbar setToolBar() {
        return null;
    }

    protected void setToolBarTitle(TextView titleText) {
    }

    protected void setToolBarTitle(String titleText) {
        ((TextView) rootView.findViewById(R.id.textTitle)).setText(titleText);
    }

    protected void setToolbarMenu(TextView menuText) {
    }

    protected int setToolBarColor() {
        return ContextCompat.getColor(this, R.color.colorPrimary);
    }

    protected boolean enableHomeArrow() {
        return true;
    }

    /*****************************IBaseView*****************************/
    @Override
    public boolean onRequestStart() {
        return false;
    }

    @Override
    public boolean onRequestCacell() {
        return false;
    }

    @Override
    public boolean onRequestEnd() {
        return false;
    }

    @Override
    public boolean onRequestError(RequestException exception) {
        return false;
    }

    @TargetApi(17)
    @Override
    public boolean onPageVisible() {
        return !(isDestroyed() || isFinishing());
    }

    @Override
    public Loading onLoadIng() {
        return new LoadingDialog(getSupportFragmentManager());
    }

    /*****************************Utils*****************************/
    protected void toast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }


}
