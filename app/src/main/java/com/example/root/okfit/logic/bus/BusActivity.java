package com.example.root.okfit.logic.bus;

import android.os.Bundle;

import com.example.root.okfit.R;
import com.example.root.okfit.base.CrBaseActivity;

public class BusActivity extends CrBaseActivity {

    @Override
    protected void init(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            SubscriberFragment subscriberFragment = SubscriberFragment.newInstance();
            ObservableFragment observableFragment = ObservableFragment.newInstance();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_subscriber, subscriberFragment)//观察者
                    .add(R.id.fl_observable, observableFragment)//被观察者
                    .commitAllowingStateLoss();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bus;
    }

}
