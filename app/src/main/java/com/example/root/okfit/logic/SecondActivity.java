package com.example.root.okfit.logic;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.root.okfit.CrRxbus.CrBusEvent;
import com.example.root.okfit.CrRxbus.CrObservable;
import com.example.root.okfit.R;
import com.example.root.okfit.base.CrBaseActivity;
import com.example.root.okfit.view.slide.SlideViewGroup;

import butterknife.BindView;
import butterknife.OnClick;

public class SecondActivity extends CrBaseActivity implements SlideViewGroup.OnBubbleClick {


    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.view)
    SlideViewGroup view;

    @Override
    protected void init(Bundle savedInstanceState) {
        view.setOnBubbleClick(this);
    }

    @OnClick(R.id.btn)
    protected void pop() {
        view.popUp();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_slide;
    }

    @Override
    public void onBubbleClick(String content, String num) {
        Log.e("onBubbleClick-->", content + "-->" + num);
        CrObservable.getInstance().sendEvent(new CrBusEvent(CrBusEvent.EventId.EVENT_MAINPAGE_SWITCH, MainActivity.TOOL));
    }
}