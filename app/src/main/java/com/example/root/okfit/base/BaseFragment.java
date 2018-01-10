package com.example.root.okfit.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fei.crnetwork.framework.view.IBaseView;
import com.fei.crnetwork.response.RequestException;
import com.example.root.okfit.R;
import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment extends RxFragment implements IBaseView {

    private View rootView;
    private BaseActivity activity;

    private boolean isHidden;
    private boolean isResume;

    private Unbinder butterKinfeBinder;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BaseActivity) {
            this.activity = (BaseActivity) activity;
            return;
        }
        throw new IllegalStateException("Makesure fragment's activity is BaseActivity!!!");
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_root, container, false);
        inflater.inflate(this.getContentLayoutId(), (ViewGroup) rootView.findViewById(R.id.layContent), true);
        initToolbar();
        return rootView;
    }


    @Override
    public final void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        butterKinfeBinder = ButterKnife.bind(this, view);
        init(savedInstanceState, view);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        this.isHidden = hidden;
        if (isResume && !hidden) {
            onVisible();
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        isResume = true;
        if (!isHidden) {
            onVisible();
        }
        initToolbar();
        super.onResume();
    }

    @Override
    public void onPause() {
        isResume = false;
        super.onPause();
    }

    protected void onVisible() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKinfeBinder.unbind();
    }

    protected abstract int getContentLayoutId();

    protected abstract void init(Bundle savedInstanceState, View view);

    public boolean isPageVisible() {
        return isVisible() || isResumed();
    }

    protected BaseActivity getBaseActivity() {
        if (activity == null) {
            return (BaseActivity) getActivity();
        }
        return activity;
    }

    public Context getContext() {
        Context context = super.getContext();
        if (context == null) {
            return getBaseActivity();
        }
        return context;
    }

    /*****************************Toolbar*****************************/

    private void initToolbar() {
        Toolbar toolbar = setToolBar();
        if (toolbar != null) {
            getBaseActivity().setSupportActionBar(toolbar);
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

        getBaseActivity().setSupportActionBar(toolbar);

        ActionBar actionBar = getBaseActivity().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(enableHomeArrow());
            actionBar.setDisplayHomeAsUpEnabled(enableHomeArrow());
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    protected Toolbar setToolBar() {
        return null;
    }

    protected boolean hasToolbar() {
        return false;
    }

    protected void setToolBarTitle(TextView titleText) {
    }

    protected void setToolbarMenu(TextView menuText) {
    }

    protected int setToolBarColor() {
        return ContextCompat.getColor(getContext(), R.color.colorPrimary);
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

    @Override
    public boolean onPageVisible() {
        return isVisible() || isResumed();
    }

    @Override
    public FragmentManager onRequestIng() {
        return getChildFragmentManager();
    }
    /*****************************Utils*****************************/
    protected void toast(String content) {
        Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
    }

}
