package com.example.root.okfit.net.bean;

import com.fei.crnetwork.dataformat.Entity;
import com.example.root.okfit.R;
import com.fei.root.recater.adapter.multi.ItemModule;

public class TopicReply implements Entity, ItemModule {

    private int id;                 // 回复 的 id
    private String body_html;       // 回复内容详情(HTML)
    private String created_at;      // 创建时间
    private String updated_at;      // 更新时间
    private boolean deleted;        // 是否已经删除
    private int topic_id;           // topic 的 id
    private User user;              // 创建该回复的用户信息
    private int likes_count;        // 喜欢的人数
    private Abilities abilities;    // 当前用户所拥有的权限


    public int getId() {
        return id;
    }

    public String getBody_html() {
        return body_html;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public User getUser() {
        return user;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public Abilities getAbilities() {
        return abilities;
    }

    @Override
    public int itemViewLayoutId() {
        return R.layout.item_topic_detail_replys;
    }
}
