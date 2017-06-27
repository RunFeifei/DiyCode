package com.example.root.okfit.net.bean;

import com.dianrong.crnetwork.dataformat.Entity;
import com.example.root.okfit.R;
import com.fei.root.recater.adapter.multi.ItemModule;

/**
 * Created by PengFeifei on 17-7-27.
 */
public class TopicDetail implements Entity,ItemModule {

    private int id;
    private String title;
    private String created_at;
    private String updated_at;
    private String replied_at;
    private int replies_count;
    private String node_name;
    private int node_id;
    private int last_reply_user_id;
    private String last_reply_user_login;
    private User user;
    private boolean deleted;
    private boolean excellent;
    private Abilities abilities;
    private String body;
    private String body_html;
    private int hits;
    private int likes_count;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getReplied_at() {
        return replied_at;
    }

    public int getReplies_count() {
        return replies_count;
    }

    public String getNode_name() {
        return node_name;
    }

    public int getNode_id() {
        return node_id;
    }

    public int getLast_reply_user_id() {
        return last_reply_user_id;
    }

    public String getLast_reply_user_login() {
        return last_reply_user_login;
    }

    public User getUser() {
        return user;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public boolean isExcellent() {
        return excellent;
    }

    public Abilities getAbilities() {
        return abilities;
    }

    public String getBody() {
        return body;
    }

    public String getBody_html() {
        return body_html;
    }

    public int getHits() {
        return hits;
    }

    public int getLikes_count() {
        return likes_count;
    }

    @Override
    public int itemViewLayoutId() {
        return R.layout.item_topic_detail;
    }
}
