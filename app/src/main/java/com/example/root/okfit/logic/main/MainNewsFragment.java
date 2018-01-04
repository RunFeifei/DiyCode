package com.example.root.okfit.logic.main;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dianrong.crnetwork.dataformat.AList;
import com.dianrong.crnetwork.framework.RequestAgent;
import com.dianrong.crnetwork.framework.subscriber.DefaultSubscriber;
import com.dianrong.crnetwork.request.RequestHandler;
import com.example.root.okfit.R;
import com.example.root.okfit.base.BaseWebviewActivity;
import com.example.root.okfit.net.api.NewsApi;
import com.example.root.okfit.net.bean.News;
import com.example.root.okfit.net.bean.User;
import com.example.root.okfit.util.TimerUtils;
import com.fei.root.common.Collections;
import com.fei.root.recater.action.OnLoadMoreData;
import com.fei.root.recater.action.OnRefreshData;
import com.fei.root.recater.adapter.RefloadAdapter;
import com.fei.root.recater.view.DefaultRefreshFooterView;
import com.fei.root.recater.view.DefaultRefreshHeaderView;
import com.fei.root.recater.view.RefloadRecyclerView;
import com.fei.root.recater.viewholder.CommonHolder;

import butterknife.BindView;

/**
 * Created by PengFeifei on 17-5-11.
 */

public final class MainNewsFragment extends MainFragment implements OnLoadMoreData<News>, OnRefreshData<News> {

    @BindView(R.id.recyclerView)
    RefloadRecyclerView recyclerView;

    private RefloadAdapter<News> adapter;
    private static final int OFFSET = 10;
    private int factorOffset = 1;

    @Override
    protected void init(Bundle savedInstanceState, View view) {
        getInitData();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.frament_main_topic;
    }

    private void getInitData() {
        requestData(0, false);
    }

    private void initRecyclerView(AList<News> news) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter = new RefloadAdapter<News>(news, R.layout.item_news) {
            @Override
            protected void convert(CommonHolder holder, News topic, int position) {
                ImageView imageView = holder.getImageView(R.id.avatar);
                Glide.with(MainNewsFragment.this.getContext()).load(topic.getUser().getAvatar_url()).dontAnimate().placeholder(imageView.getDrawable()).into
                        (imageView);
                User user = topic.getUser();
                holder.setText(R.id.author, user.getName());
                holder.setText(R.id.time, TimerUtils.getHowLongAgo(topic.getUpdated_at()));
                holder.setText(R.id.type, topic.getNode_name());
                holder.setText(R.id.title, topic.getTitle());
                holder.setText(R.id.address, topic.getAddress());
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.setRefreshHeader(new DefaultRefreshHeaderView(getContext()));
        adapter.setLoadMoreFooter(new DefaultRefreshFooterView(getContext()));
        adapter.setRefreshDataListener(this);
        adapter.setLoadMoreDataListener(this);
        adapter.setEnablePullLoadMore(true);
        adapter.setEnablePullRefreshing(true);
        adapter.setOnItemClick((newInfo, view, i) -> BaseWebviewActivity.openLink(getContext(), newInfo.getAddress()));
    }


    @Override
    public void onLoadMoreIng() {
        requestData(OFFSET * (factorOffset++), false);
    }

    @Override
    public void onLoadMoreFail() {

    }

    @Override
    public void onLoadMoreSuccess() {

    }

    @Override
    public void onLoadMoreNone() {

    }

    @Override
    public void onRefreshing() {
        requestData(0, true);
    }

    @Override
    public void onRefreshFail() {

    }

    @Override
    public void onRefreshSuccess() {

    }

    private void requestData(int offset, boolean isRefreshing) {
        RequestAgent<AList<News>> requestAgent = new RequestAgent<>(this);
        requestAgent
                .bindObservable(RequestHandler.getService(NewsApi.class).getNews(offset))
                .onObservable()
                .subscribe(new DefaultSubscriber<AList<News>>(this) {
                    @Override
                    public void onHandleData(AList<News> topicses) {
                        if (Collections.isEmpty(topicses)) {
                            onLoadMoreNone();
                            return;
                        }
                        if (adapter == null) {
                            initRecyclerView(topicses);
                            return;
                        }
                        if (isRefreshing) {
                            adapter.clearAll(false);
                            adapter.appendItems(topicses);
                            adapter.onLoadSuccess(true);
                            return;
                        }
                        adapter.appendItems(topicses);
                        adapter.onLoadSuccess(false);
                    }
                })
        ;
    }

    @Override
    public FragmentManager onRequestIng() {
        return null;
    }
}
