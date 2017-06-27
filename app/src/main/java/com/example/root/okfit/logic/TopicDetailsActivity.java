package com.example.root.okfit.logic;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dianrong.crnetwork.dataformat.AList;
import com.dianrong.crnetwork.framework.RequestAgent;
import com.dianrong.crnetwork.framework.subscriber.DefaultSubscriber;
import com.dianrong.crnetwork.request.RequestHandler;
import com.example.root.okfit.R;
import com.example.root.okfit.base.BaseActivity;
import com.example.root.okfit.net.api.TopicApi;
import com.example.root.okfit.net.bean.TopicDetail;
import com.example.root.okfit.net.bean.TopicReply;
import com.example.root.okfit.util.CommonUtils;
import com.example.root.okfit.view.CustomWebView;
import com.fei.root.recater.adapter.multi.ItemModule;
import com.fei.root.recater.adapter.multi.MultiAdapter;
import com.fei.root.recater.viewholder.CommonHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TopicDetailsActivity extends BaseActivity {

    public static final String TOPIC = "topic";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private MultiAdapter<ItemModule> adapter;
    private List<ItemModule> listDatas;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_topic_details;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        listDatas = new ArrayList<>();
    }

    @Override
    protected void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        int id = intent.getIntExtra(TOPIC, -1);
        if (id < 0) {
            finish();
            return;
        }
        requestData(id);
    }

    private void requestData(int id) {
        RequestAgent<TopicDetail> requestAgent = new RequestAgent<>(this);
        requestAgent
                .bindObservable(RequestHandler.getService(TopicApi.class).getTopicDetail(id))
                .onObservable()
                .subscribe(new DefaultSubscriber<TopicDetail>(this) {
                    @Override
                    public void onHandleData(TopicDetail topicDetail) {
                        listDatas.add(topicDetail);
                        initRecyclerView(listDatas);
                        requestReplyData(id);
                    }
                })
        ;
    }

    private void requestReplyData(int id) {
        RequestAgent<AList<TopicReply>> requestAgent = new RequestAgent<>(this);
        requestAgent
                .bindObservable(RequestHandler.getService(TopicApi.class).getTopicRepliesList(id))
                .onObservable()
                .subscribe(new DefaultSubscriber<AList<TopicReply>>(this) {
                    @Override
                    public void onHandleData(AList<TopicReply> topicReplys) {
                        //                        listDatas.addAll(new ItemWrapper<>(topicReplys,R.layout.item_topic_detail_replys));
                        listDatas.addAll(topicReplys);
                        adapter.notifyDataSetChanged();
                    }
                })
        ;
    }

    private void initRecyclerView(List<ItemModule> datas) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter = new MultiAdapter<ItemModule>(datas) {

            @Override
            protected void convert(CommonHolder holder, ItemModule module, int position) {
                if (module instanceof TopicDetail) {
                    TopicDetail item = (TopicDetail) module;
                    setToolBarTitle(item.getTitle());
                    CustomWebView customWebView = (CustomWebView) holder.itemView.findViewById(R.id.web_view);
                    customWebView.loadData(item.getBody_html());

                    ImageView imageView = holder.getImageView(R.id.avatar);
                    Glide.with(TopicDetailsActivity.this)
                            .load(item.getUser().getAvatar_url()).dontAnimate().placeholder(imageView.getDrawable()).into(imageView);
                    holder.setText(R.id.author, item.getUser().getName());
                    holder.setText(R.id.type, item.getNode_name());
                    holder.setText(R.id.reply_count, String.format(getString(R.string.topic_detail_reply_count), item.getReplies_count()));
                    Typeface typeface = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
                    TextView likeText = holder.getTextView(R.id.like);
                    likeText.setTypeface(typeface);
                    likeText.setText(getString(R.string.topic_detail_like));

                    TextView favoriteText = holder.getTextView(R.id.favorite);
                    favoriteText.setTypeface(typeface);
                    favoriteText.setText(getString(R.string.topic_detail_favorite));
                    holder.setText(R.id.like_count, item.getLikes_count() + "");
                    return;
                }
                if (module instanceof TopicReply) {
                    TopicReply item = (TopicReply) module;
                    ImageView imageView = holder.getImageView(R.id.avatar);
                    Glide.with(TopicDetailsActivity.this)
                            .load(item.getUser().getAvatar_url()).dontAnimate().placeholder(imageView.getDrawable()).into(imageView);
                    holder.setText(R.id.author, item.getUser().getName());
                    holder.setText(R.id.position_and_time, String.format(getString(R.string.topic_detail_position_and_time),
                            holder.getAdapterPosition(), CommonUtils.getHowLongAgo(item.getUpdated_at())));
                    Typeface typeface = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
                    holder.getTextView(R.id.reply).setTypeface(typeface);
                    holder.setText(R.id.reply, getString(R.string.topic_detail_reply));
                    holder.setText(R.id.content, Html.fromHtml(item.getBody_html()));

                }
            }
        };
        recyclerView.setAdapter(adapter);
    }

}
