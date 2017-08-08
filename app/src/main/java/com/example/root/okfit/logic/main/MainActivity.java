package com.example.root.okfit.logic.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.root.okfit.CrRxbus.BusEvents;
import com.example.root.okfit.CrRxbus.BusSubscriber;
import com.example.root.okfit.R;
import com.example.root.okfit.base.BaseActivity;
import com.example.root.okfit.logic.LoginActivity;
import com.example.root.okfit.util.UserManager;

import butterknife.BindView;

/**
 * Created by PengFeifei on 17-5-11.
 */

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;
    private Fragment[] fragments;
    private int previousPosition;

    @Override
    protected void init(Bundle savedInstanceState) {
        initFragments();
        initBottomBars();
        addDefaultFragment();
//        resgisBusEvent();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_second;
    }

    private void initFragments() {
        fragments = new Fragment[4];
        fragments[0] = new MainTopicFragment();
        fragments[1] = new MainNewsFragment();
        fragments[2] = new MainSiteFragment();
        fragments[3] = new MainMineFragment();
    }

    private void initBottomBars() {
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setForegroundGravity(Gravity.CENTER);
        bottomNavigationBar.addItem(getTopicItem())
                .addItem(getNewsItem())
                .addItem(getSitesItem())
                .addItem(getMineItem())
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

    private BottomNavigationItem getMineItem() {
        BottomNavigationItem item = new BottomNavigationItem(R.drawable.ic_loan_calculator, "mine");
        item.setActiveColor(R.color.transparent);
        item.setInactiveIconResource(R.drawable.ic_loan_calculator);
        return item;
    }


    private void addDefaultFragment() {
        switchFragment(0);
    }

    @Override
    protected int getFragmentId() {
        return R.id.layFrame;
    }

    @Override
    public void onTabSelected(int position) {
        if (position == 3 && !UserManager.isLogined) {
            startActivity(new Intent(this, LoginActivity.class));
            bottomNavigationBar.selectTab(previousPosition);
            return;
        }
        previousPosition = position;
        switchFragment(position);
    }

    @Override
    public void onTabUnselected(int position) {
    }

    @Override
    public void onTabReselected(int position) {
        switchFragment(position);
    }

    private void switchFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            Fragment fragment = fragments[i];
            if (!fragment.isAdded()) {
                transaction.add(R.id.layFrame, fragment);
            }
            if (i == position) {
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected boolean hasToolbar() {
        return false;
    }

    private void resgisBusEvent() {
        BusSubscriber.bind(this).bindEvent(BusEvents.SWITCH_TAB)
                .onNext((event) -> toast(event.getContent())).create();
    }

}
