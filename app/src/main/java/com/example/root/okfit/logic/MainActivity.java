package com.example.root.okfit.logic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.root.okfit.R;
import com.example.root.okfit.base.BaseActivity;
import com.example.root.okfit.logic.main.MainNewsFragment;
import com.example.root.okfit.logic.main.MainSiteFragment;
import com.example.root.okfit.logic.main.MainTopicFragment;

import butterknife.BindView;

/**
 * Created by PengFeifei on 17-5-11.
 */

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;
    private Fragment[] fragments;

    @Override
    protected void init(Bundle savedInstanceState) {
        initFragments();
        initBottomBars();
        addDefaultFragment();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_second;
    }

    private void initFragments() {
        fragments = new Fragment[3];
        fragments[0] = new MainTopicFragment();
        fragments[1] = new MainNewsFragment();
        fragments[2] = new MainSiteFragment();
    }

    private void initBottomBars() {
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setForegroundGravity(Gravity.CENTER);
        bottomNavigationBar.addItem(getTopicItem())
                .addItem(getNewsItem())
                .addItem(getSitesItem())
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
    }

    private BottomNavigationItem getTopicItem() {
        BottomNavigationItem item = new BottomNavigationItem(R.drawable.ic_public_sentiment, "topic");
        item.setActiveColor(R.color.transparent);
        item.setInactiveIconResource(R.drawable.ic_public_sentiment);
        return item;
    }


    private BottomNavigationItem getNewsItem() {
        BottomNavigationItem item = new BottomNavigationItem(R.drawable.ic_blacklist, "news");
        item.setActiveColor(R.color.transparent);
        item.setInactiveIconResource(R.drawable.ic_blacklist);
        return item;
    }

    private BottomNavigationItem getSitesItem() {
        BottomNavigationItem item = new BottomNavigationItem(R.drawable.ic_loan_calculator, "sites");
        item.setActiveColor(R.color.transparent);
        item.setInactiveIconResource(R.drawable.ic_loan_calculator);
        return item;
    }


    private void addDefaultFragment() {
        addFragment(fragments[0]);
    }

    @Override
    protected int getFragmentId() {
        return R.id.layFrame;
    }

    @Override
    public void onTabSelected(int position) {
        switchFragment(position,true);
    }

    @Override
    public void onTabUnselected(int position) {
        switchFragment(position,false);
    }

    @Override
    public void onTabReselected(int position) {

    }

    private Fragment switchFragment(int position, boolean selected) {
        Fragment fragment = fragments[position];
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!fragment.isAdded()) {
            transaction.add(R.id.layFrame, fragment);
        }
        if (selected) {
            transaction.show(fragment);
        } else {
            transaction.hide(fragment);
        }
        transaction.commitAllowingStateLoss();
        return fragment;
    }

    @Override
    protected boolean hasToolbar() {
        return false;
    }
}
