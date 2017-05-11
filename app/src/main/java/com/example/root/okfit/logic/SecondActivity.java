package com.example.root.okfit.logic;

import android.os.Bundle;
import android.view.Gravity;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.root.okfit.R;
import com.example.root.okfit.base.CrBaseActivity;
import com.example.root.okfit.logic.main.MainCreditFragment;
import com.example.root.okfit.logic.main.MainDetectiveFragment;
import com.example.root.okfit.logic.main.MainToolFragment;

import butterknife.BindView;

/**
 * Created by PengFeifei on 17-5-11.
 */

public class SecondActivity extends CrBaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;


    @Override
    protected void init(Bundle savedInstanceState) {
        initBottomBars();
        addDefaultFragment();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_second;
    }


    private void initBottomBars() {
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
       /* bottomNavigationBar.setActiveColor(android.R.color.transparent);
        bottomNavigationBar.setInActiveColor(android.R.color.transparent);
        bottomNavigationBar.setBarBackgroundColor(android.R.color.transparent);*/
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setForegroundGravity(Gravity.CENTER);


        bottomNavigationBar.addItem(getDetectiveItem())
                .addItem(getCreditItem())
                .addItem(getToolItem())
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
    }

    private BottomNavigationItem getDetectiveItem() {
        BottomNavigationItem item = new BottomNavigationItem(R.drawable.ic_public_sentiment,"111");
        item.setActiveColor(R.color.transparent);
        item.setInactiveIconResource(R.drawable.ic_public_sentiment);
        return item;
    }


    private BottomNavigationItem getCreditItem() {
        BottomNavigationItem item = new BottomNavigationItem(R.drawable.ic_blacklist,"222");
        item.setActiveColor(R.color.transparent);
        item.setInactiveIconResource(R.drawable.ic_blacklist);
        return item;
    }

    private BottomNavigationItem getToolItem() {
        BottomNavigationItem item = new BottomNavigationItem(R.drawable.ic_loan_calculator,"333");
        item.setActiveColor(R.color.transparent);
        item.setInactiveIconResource(R.drawable.ic_loan_calculator);
        return item;
    }


    private void addDefaultFragment() {
        addFragment(new MainDetectiveFragment());
    }

    @Override
    protected int getFragmentId() {
        return R.id.layFrame;
    }

    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case 0: {
                addFragment(new MainDetectiveFragment());
                break;
            }
            case 1: {
                addFragment(new MainCreditFragment());
                break;
            }
            case 2: {
                addFragment(new MainToolFragment());
                break;
            }
        }

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }


}
