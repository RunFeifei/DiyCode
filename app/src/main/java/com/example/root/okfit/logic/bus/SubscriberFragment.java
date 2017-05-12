package com.example.root.okfit.logic.bus;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.root.okfit.CrRxbus.CrBusEvent;
import com.example.root.okfit.CrRxbus.CrSubscriber;
import com.example.root.okfit.R;
import com.example.root.okfit.base.CrBaseFragment;

import rx.Subscription;
import rx.functions.Action1;

/**
 * 观察者(订阅者)
 */
public class SubscriberFragment extends CrBaseFragment {
    private static final String TAG = "RxBus";

    private TextView mTvResult, mTvResultSticky;
    private Button mBtnSubscribeSticky;
    private CheckBox mCheckBox;


    public static SubscriberFragment newInstance() {
        return new SubscriberFragment();
    }


    @Override
    protected void init(Bundle savedInstanceState, View view) {
        initView(view);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_subscriber;
    }

    private void initView(View view) {
        mTvResult = (TextView) view.findViewById(R.id.tv_result);
        mTvResultSticky = (TextView) view.findViewById(R.id.tv_resultSticky);
        mBtnSubscribeSticky = (Button) view.findViewById(R.id.btn_subscribeSticky);
        mCheckBox = (CheckBox) view.findViewById(R.id.checkbox);

        // 订阅普通RxBus事件
        subscribeEvent(false);

        mBtnSubscribeSticky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 订阅Sticky事件
                subscribeEvent(true);
            }
        });
    }

    private void subscribeEvent(boolean isSticky) {
        CrSubscriber.getFragmentSubscriber(this)
                .bindEvent(new CrBusEvent(1, "1"))
                .onNext(new Action1<CrBusEvent>() {
                    @Override
                    public void call(CrBusEvent crBusEvent) {
                        if (isSticky) {
                            mTvResult.setText(crBusEvent.getContent());
                        } else {
                            mTvResultSticky.setText(crBusEvent.getContent());
                        }
                    }
                })
                .onError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.getCause();
                    }
                })
                .create(isSticky);
    }


}
