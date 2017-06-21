package com.example.root.okfit.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dianrong.android.common.CrashHandler;
import com.dianrong.android.common.utils.Log;
import com.example.root.okfit.BuildConfig;
import com.example.root.okfit.R;
import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class CrBaseFragment extends RxFragment {

    private Unbinder butterKinfeBinder;


    private View root;
    private CrBaseActivity activity;

    private boolean hidden = false;
    private boolean resume = false;


    protected View getRoot() {
        return root;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (CrBaseActivity) activity;
    }


    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (root != null) {
            return root;
        }
        root = inflater.inflate(getRootLayout(), container, false);
        try {
            View view = inflater.inflate(this.getContentViewId(), (ViewGroup) root.findViewById(R.id.rootContainer), true);
            butterKinfeBinder = ButterKnife.bind(this, view);
        } catch (Exception e) {
            // nothing
        }
        return root;
    }

    @Override
    public final void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        butterKinfeBinder = ButterKnife.bind(this, view);
        try {
            init(savedInstanceState, view);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.logStackTrace(e);
            }
            CrashHandler.getInstance().saveCrashInfoToFile(e, false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKinfeBinder.unbind();
    }

    @LayoutRes
    protected int getRootLayout() {
        return R.layout.base_fragment;
    }

    protected abstract int getContentViewId();

    protected abstract void init(Bundle savedInstanceState, View view);

    @Override
    public void onHiddenChanged(boolean hidden) {
        this.hidden = hidden;
        if (resume && !hidden) {
            onVisible();
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onPause() {
        resume = false;
        super.onPause();
    }

    @Override
    public void onResume() {
        resume = true;
        if (!hidden) {
            onVisible();
        }
        super.onResume();
    }

    /**
     * fragment由不可见变为可见时被调用，首次调用时（Fragment刚加载），根View是没有WindowToken的。
     */
    protected void onVisible() {
    }


    protected CrBaseActivity getRealActivity() {
        if (activity == null) {
            return (CrBaseActivity) getActivity();
        }
        return activity;
    }


}
