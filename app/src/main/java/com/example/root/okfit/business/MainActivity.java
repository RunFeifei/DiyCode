package com.example.root.okfit.business;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.crnetwork.dataformat.DrList;
import com.example.crnetwork.host.BaseUrlBindHelper;
import com.example.crnetwork.host.ServerType;
import com.example.root.okfit.R;
import com.example.root.okfit.base.BaseActivity;
import com.example.root.okfit.net.bean.BreakerItem;
import com.example.root.okfit.uibinder.Hold;
import com.example.root.okfit.uibinder.UiBinder;
import com.example.root.okfit.uibinder.Work;
import com.okfit.repository.MethodTestRepository;


public class MainActivity extends BaseActivity {
    private TextView textView;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseUrlBindHelper.resetBaseUrl(ServerType.PRODUCT);

        textView = (TextView) this.findViewById(R.id.text);
        textView2 = (TextView) this.findViewById(R.id.text2);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void loadData() {
        UiBinder<DrList<BreakerItem>> uiBinder = getUiBinder();
        uiBinder.workInBackground(new Work<DrList<BreakerItem>>() {
            @Override
            public DrList<BreakerItem> onWork() {
                return new MethodTestRepository().getBreakers("ios");
            }
        }).holdDataInUi(new Hold<DrList<BreakerItem>>() {
            @Override
            public void onResultHold(DrList<BreakerItem> data) {
                textView2.setText(data.getList().get(1).getName());
            }
        }).apply();
    }

}
