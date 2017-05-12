package com.example.root.okfit.logic;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.root.okfit.CrRxbus.CrBusEvent;
import com.example.root.okfit.CrRxbus.CrSubscriber;
import com.example.root.okfit.R;
import com.example.root.okfit.base.CrBaseActivity;
import com.example.root.okfit.logic.main.MainCreditFragment;
import com.example.root.okfit.logic.main.MainDetectiveFragment;
import com.example.root.okfit.logic.main.MainToolFragment;

import butterknife.BindView;
import rx.functions.Action1;
import util.Strings;

/**
 * Created by PengFeifei on 17-5-11.
 */

public class MainActivity extends CrBaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String DETECTIVE = MainDetectiveFragment.class.getSimpleName();
    public static final String CREDIT = MainCreditFragment.class.getSimpleName();
    public static final String TOOL = MainToolFragment.class.getSimpleName();

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;

    @Override
    protected void init(Bundle savedInstanceState) {
        initBottomBars();
        addDefaultFragment();
        subscribeEvent(true);
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
        BottomNavigationItem item = new BottomNavigationItem(R.drawable.ic_public_sentiment, "111");
        item.setActiveColor(R.color.transparent);
        item.setInactiveIconResource(R.drawable.ic_public_sentiment);
        return item;
    }


    private BottomNavigationItem getCreditItem() {
        BottomNavigationItem item = new BottomNavigationItem(R.drawable.ic_blacklist, "222");
        item.setActiveColor(R.color.transparent);
        item.setInactiveIconResource(R.drawable.ic_blacklist);
        return item;
    }

    private BottomNavigationItem getToolItem() {
        BottomNavigationItem item = new BottomNavigationItem(R.drawable.ic_loan_calculator, "333");
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


    private void subscribeEvent(boolean isSticky) {
        CrSubscriber.getActivitySubscriber(this)
                .bindEvent(CrBusEvent.EventId.EVENT_MAINPAGE_SWITCH)
                .onNext(new Action1<CrBusEvent>() {
                    @Override
                    public void call(CrBusEvent crBusEvent) {
                        Log.e("TAG-->","-------------------------------");
                        if (crBusEvent == null) {
                            return;
                        }
                        String action = crBusEvent.getContent();
                        if (Strings.isEqual(action, DETECTIVE)) {
                            bottomNavigationBar.selectTab(0);
                            return;
                        }
                        if (Strings.isEqual(action, CREDIT)) {
                            bottomNavigationBar.selectTab(1);
                            return;
                        }
                        if (Strings.isEqual(action, TOOL)) {
                            bottomNavigationBar.selectTab(2);
                        }

                    }
                })
                .onError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, throwable.getCause().toString());
                    }
                })
                .create(isSticky);
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
