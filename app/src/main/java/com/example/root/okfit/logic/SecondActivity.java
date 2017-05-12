package com.example.root.okfit.logic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dianrong.crnetwork.host.BaseUrlBindHelper;
import com.dianrong.crnetwork.host.ServerType;
import com.example.root.okfit.CrRxbus.CrBusEvent;
import com.example.root.okfit.CrRxbus.CrObservable;
import com.example.root.okfit.R;
import com.example.root.okfit.base.CrBaseActivity;

public class SecondActivity extends CrBaseActivity {
    private TextView textView2;
    private static int tab = 0;


    @Override
    protected void init(Bundle savedInstanceState) {

        BaseUrlBindHelper.resetBaseUrl(ServerType.PRODUCT);

        textView2 = (TextView) this.findViewById(R.id.text2);
        textView2.setText("SecondActivity");
        this.findViewById(R.id.text).setVisibility(View.GONE);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this,MainActivity.class));
                if (tab == 0) {
                    CrObservable.getInstance().sendStickyEvent(new CrBusEvent(CrBusEvent.EventId.EVENT_MAINPAGE_SWITCH, MainActivity.DETECTIVE));
                } else if (tab == 1) {
                    CrObservable.getInstance().sendStickyEvent(new CrBusEvent(CrBusEvent.EventId.EVENT_MAINPAGE_SWITCH, MainActivity.CREDIT));

                } else if (tab == 2) {
                    CrObservable.getInstance().sendStickyEvent(new CrBusEvent(CrBusEvent.EventId.EVENT_MAINPAGE_SWITCH, MainActivity.TOOL));
                }
                tab++;
                if (tab == 3) {
                    tab = 0;
                }

            }
        });

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }


}