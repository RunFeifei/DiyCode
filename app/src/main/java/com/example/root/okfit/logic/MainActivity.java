package com.example.root.okfit.logic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dianrong.crnetwork.dataformat.DrList;
import com.dianrong.crnetwork.dataformat.DrRoot;
import com.dianrong.crnetwork.host.BaseUrlBindHelper;
import com.dianrong.crnetwork.host.ServerType;
import com.dianrong.crnetwork.response.DrResponse;
import com.dianrong.crnetwork.response.ResponseCallback;
import com.example.root.okfit.R;
import com.example.root.okfit.base.BaseActivity;
import com.example.root.okfit.net.bean.BreakerItem;
import com.example.root.okfit.net.bean.ErrorItem;
import com.example.root.okfit.uibinder.Hold;
import com.example.root.okfit.uibinder.UiBinder;
import com.example.root.okfit.uibinder.Work;
import com.okfit.repository.ClassTestRepository;
import com.okfit.repository.MethodTestRepository;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;


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
                startActivity(new Intent(MainActivity.this, BubbleActivity.class));
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMethodAsyncBreakers();
                getClassAsyncErrors();
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


    private void getMethodBreakers() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DrList<BreakerItem> drList = new MethodTestRepository().getBreakers("android");
                log("getMethodBreakers", drList.getList().get(0).getName());
            }
        }).start();
    }

    private void getMethodAsyncBreakers() {
        new MethodTestRepository().getBreakers("ios", new ResponseCallback<DrRoot<DrList<BreakerItem>>>() {
            @Override
            public void onResponse(Call<DrRoot<DrList<BreakerItem>>> call, Response<DrRoot<DrList<BreakerItem>>> response) {
                super.onResponse(call, response);
                ArrayList<BreakerItem> listData = new DrResponse<DrRoot<DrList<BreakerItem>>>().getListData(response, call);
                log("getMethodAsyncBreakers", listData.get(1).getName());
            }
        });
    }


    private void getClassErrors() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DrList<ErrorItem> drList = new ClassTestRepository().getErros();
                log("getClassErrors", drList.getList().get(0).getZhCN());
            }
        }).start();
    }

    private void getClassAsyncErrors() {
        new ClassTestRepository().getErros(new ResponseCallback<DrRoot<DrList<ErrorItem>>>() {
            @Override
            public void onResponse(Call<DrRoot<DrList<ErrorItem>>> call, Response<DrRoot<DrList<ErrorItem>>> response) {
                super.onResponse(call, response);
                ArrayList<ErrorItem> listData = new DrResponse<DrRoot<DrList<ErrorItem>>>().getListData(response, call);
                log("getClassAsyncErrors", listData.get(1).getZhCN());

            }
        });
    }


}
