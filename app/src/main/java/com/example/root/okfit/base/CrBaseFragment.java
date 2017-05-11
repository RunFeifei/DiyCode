package com.example.root.okfit.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dianrong.android.common.CrashHandler;
import com.dianrong.android.common.utils.Log;
import com.dianrong.crnetwork.response.RequestException;
import com.example.root.okfit.BuildConfig;
import com.example.root.okfit.R;
import com.example.root.okfit.uibinder.UiBinder;
import com.example.root.okfit.uibinder.UiBinderAgent;
import com.example.root.okfit.uibinder.UiBinderBatch;
import com.example.root.okfit.uibinder.UiBinderView;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class CrBaseFragment extends Fragment implements UiBinderView {

    private UiBinderAgent activityBinderAgent;
    private UiBinderAgent fragmentBinderAgent;
    private Unbinder butterKinfeBinder;


    private View root;
    private CrBaseActivity activity;

    private boolean hidden = false;
    private boolean resume = false;


    protected View getRoot() {
        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppFragmentCallback) {
            AppFragmentCallback callback = (AppFragmentCallback) context;
            this.activityBinderAgent = callback.getUiBinderAgent();
        }
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
            init(savedInstanceState);
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

    protected abstract void init(Bundle savedInstanceState);

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


    @Override
    public void onDetach() {
        super.onDetach();
        this.activityBinderAgent = null;
    }

    @Override
    public void onUiBinderStart(int type) {
        /*Not implement*/
    }

    @Override
    public void onUiBinderError(RequestException error) {
        /*Not implement*/
    }

    @Override
    public void onUiBinderEnd(int type) {
        /*Not implement*/
    }

    protected UiBinderAgent getFragmentUiBinderAgent() {
        UiBinderAgent helper = this.fragmentBinderAgent;
        if (helper == null) {
            helper = UiBinderAgent.newInstance();
            helper.setUiBinderView(this);
            this.fragmentBinderAgent = helper;
        }
        return helper;
    }

    protected UiBinderAgent getActivityUiBinderAgent() {
        return this.activityBinderAgent;
    }

    protected <T> UiBinder<T> getActivityUiBinder() {
        return this.activityBinderAgent.bornUiBinder();
    }

    protected UiBinderBatch getActivityUiBinderBatch() {
        return this.activityBinderAgent.bornUiBinderBatch();
    }

    protected <T> UiBinder<T> getFragmentUiBinder() {
        return getFragmentUiBinderAgent().bornUiBinder();
    }

    protected UiBinderBatch getFragmentUiBinderBatch() {
        return getFragmentUiBinderAgent().bornUiBinderBatch();
    }

    public interface AppFragmentCallback {
        UiBinderAgent getUiBinderAgent();
    }


    protected CrBaseActivity getRealActivity() {
        if (activity == null) {
            return (CrBaseActivity) getActivity();
        }
        return activity;
    }


}
