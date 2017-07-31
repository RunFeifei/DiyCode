package com.example.root.okfit.logic.main;

import android.content.Intent;
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
import com.example.root.okfit.logic.TopicDetailsActivity;
import com.example.root.okfit.net.api.TopicApi;
import com.example.root.okfit.net.bean.Topic;
import com.example.root.okfit.net.bean.User;
import com.example.root.okfit.util.TimerUtils;
import com.fei.root.recater.action.OnLoadMoreData;
import com.fei.root.recater.action.OnRefreshData;
import com.fei.root.recater.adapter.RefloadAdapter;
import com.fei.root.recater.view.DefaultRefreshFooterView;
import com.fei.root.recater.view.DefaultRefreshHeaderView;
import com.fei.root.recater.view.RefloadRecyclerView;
import com.fei.root.recater.viewholder.CommonHolder;
import com.feifei.common.utils.Collections;

import butterknife.BindView;

import static com.example.root.okfit.logic.TopicDetailsActivity.TOPIC;

/**
 * Created by PengFeifei on 17-5-11.
 */

public final class MainTopicFragment extends MainFragment implements OnLoadMoreData<Topic>, OnRefreshData<Topic> {

    @BindView(R.id.recyclerView)
    RefloadRecyclerView recyclerView;

    private RefloadAdapter<Topic> adapter;
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

    private void initRecyclerView(AList<Topic> topicses) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter = new RefloadAdapter<Topic>(topicses, R.layout.item_topics) {
            @Override
            protected void convert(CommonHolder holder, Topic topic, int position) {
                ImageView imageView = holder.getImageView(R.id.avatar);
                Glide.with(MainTopicFragment.this.getContext()).load(topic.getUser().getAvatar_url()).dontAnimate().placeholder(imageView.getDrawable()).into
                        (imageView);
                User user = topic.getUser();
                holder.setText(R.id.author, user.getName());
                holder.setText(R.id.time, TimerUtils.getHowLongAgo(topic.getUpdated_at()));
                holder.setText(R.id.type, topic.getNode_name());
                holder.setText(R.id.replies_count, "评论 " + topic.getReplies_count());
                holder.setText(R.id.title, topic.getTitle());
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.setRefreshHeader(new DefaultRefreshHeaderView(getContext()));
        adapter.setLoadMoreFooter(new DefaultRefreshFooterView(getContext()));
        adapter.setRefreshDataListener(this);
        adapter.setLoadMoreDataListener(this);
        adapter.setEnablePullLoadMore(true);
        adapter.setEnablePullRefreshing(true);
        adapter.setOnItemClick((topic, view, i) -> {
            Intent intent=new Intent(getContext(), TopicDetailsActivity.class);
            intent.putExtra(TOPIC, topic.getId());
            startActivity(intent);
        });
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
        RequestAgent<AList<Topic>> requestAgent = new RequestAgent<>(this);
        requestAgent
                .bindObservable(RequestHandler.getService(TopicApi.class).getTopics(offset))
                .onObservable()
                .subscribe(new DefaultSubscriber<AList<Topic>>(this) {
                    @Override
                    public void onHandleData(AList<Topic> topicses) {
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
