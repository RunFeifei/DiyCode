package com.example.root.okfit.logic.main;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.dianrong.crnetwork.dataformat.DrList;
import com.dianrong.crnetwork.framework.RequestAgent;
import com.dianrong.crnetwork.framework.error.ErrorHandler;
import com.dianrong.crnetwork.framework.requests.Requests;
import com.dianrong.crnetwork.framework.subscriber.DefaultSubscriber;
import com.dianrong.crnetwork.framework.view.IBaseView;
import com.dianrong.crnetwork.response.RequestException;
import com.example.root.okfit.R;
import com.example.root.okfit.net.bean.ErrorItem;
import com.kotfit.repository.ClassTestRepository;
import com.kotfit.repository.MethodTestRepository;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by PengFeifei on 17-5-11.
 */

public final class MainToolFragment extends MainFragment implements IBaseView {

    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.textContent)
    TextView textContent;

    @Override
    protected void init(Bundle savedInstanceState, View view) {
        text.setText("ToMap");
    }

    @Override
    protected int getContentViewId() {
        return R.layout.frament_main_tool;
    }

    @OnClick(R.id.text)
    protected void toBubble() {
        /*RequestAgent<DrRoot<DrList<ErrorItem>>> requestAgent = new RequestAgent<>(this);
        requestAgent
                .bindObservable(RequestHandler.getService(DiSanfangApi.class).disanfang2())
                .bindErrorHandler(new ErrorHandler() {
                    @Override
                    public boolean onErrorOccurs(RequestException error) {
                        text.setText(error.toString());
                        return false;
                    }
                }).onData(new Action1<DrRoot<DrList<ErrorItem>>>() {
            @Override
            public void call(DrRoot<DrList<ErrorItem>> drListDrRoot) {
                textContent.setText(drListDrRoot.getContent().getList().get(10).getZhCN());
            }
        });*/
       /* RequestAgent<DrRoot<DrList<ErrorItem>>> requestAgent = new RequestAgent<>(this);
        requestAgent
                .bindObservable(RequestHandler.getService(DiSanfangApi.class).disanfang2())
                .onObservable()
                .subscribe(new DefaultSubscriber<DrRoot<DrList<ErrorItem>>>(this, new ErrorHandler() {
                    @Override
                    public boolean onErrorOccurs(RequestException error) {
                        text.setText(error.toString());
                        return false;
                    }
                }) {
                    @Override
                    public void onHandleData(DrRoot<DrList<ErrorItem>> drListDrRoot) {
                        textContent.setText(drListDrRoot.getContent().getList().get(10).getZhCN());
                    }
                });*/

        RequestAgent<DrList<ErrorItem>> requestAgent = new RequestAgent<>(this);
        requestAgent.bindRequests(new Requests<DrList<ErrorItem>>() {
            @Override
            public DrList<ErrorItem> onRequests() {
                if (new MethodTestRepository().getBreakers("ios").getList().size() > 20) {
                    return new ClassTestRepository().getErros();
                }
                return null;
            }
        }).onObservable().subscribe(new DefaultSubscriber<DrList<ErrorItem>>(this, new ErrorHandler() {
            @Override
            public boolean onErrorOccurs(RequestException error) {
                return false;
            }
        }) {
            @Override
            public void onHandleData(DrList<ErrorItem> errorItemDrList) {
                textContent.setText(errorItemDrList.getList().get(10).getZhCN());
            }
        });
    }

    @Override
    public boolean onRequestStart() {
        return false;
    }

    @Override
    public boolean onRequestEnd() {
        return false;
    }

    @Override
    public boolean onRequestError(RequestException exception) {
        text.setText(exception.toString());
        return false;
    }

    @Override
    public boolean onPageVisible() {
        return isVisible() || isResumed();
    }

    @Override
    public FragmentManager onLoading() {
        return getChildFragmentManager();
    }

    @Override
    public boolean onRequestCacell() {
        return false;
    }
}
