package com.example.root.okfit.logic;

import android.content.Intent;
import android.os.Bundle;

import com.dianrong.crnetwork.framework.RequestAgent;
import com.dianrong.crnetwork.framework.subscriber.DefaultSubscriber;
import com.dianrong.crnetwork.request.RequestHandler;
import com.example.root.okfit.R;
import com.example.root.okfit.base.BaseActivity;
import com.example.root.okfit.logic.main.MainActivity;
import com.example.root.okfit.net.api.FeApi;
import com.example.root.okfit.net.dianrong.BreakerItem;
import com.example.root.okfit.net.dianrong.DrList;
import com.example.root.okfit.net.dianrong.DrRoot;

import butterknife.OnClick;

public class SplashActivity extends BaseActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @OnClick(R.id.button)
    protected void toMain() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @OnClick(R.id.button1)
    protected void toTest() {
        requestData();
    }

    private void requestData() {
        RequestAgent<DrRoot<DrList<BreakerItem>>> requestAgent = new RequestAgent<>(this);
        requestAgent
                .bindObservable(RequestHandler.getService(FeApi.class).getBreakers())
                .onObservable()
                .subscribe(new DefaultSubscriber<DrRoot<DrList<BreakerItem>>>(this) {
                    @Override
                    public void onHandleData(DrRoot<DrList<BreakerItem>> drListDrRoot) {
                        drListDrRoot.getContent().getList().get(0);
                    }
                })
        ;
    }
}
