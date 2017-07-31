package com.example.root.okfit.net.bean;

import com.dianrong.crnetwork.dataformat.Entity;

public class News implements Entity {

    private int id;                         // 唯一 id
    private String title;                   // 标题
    private String created_at;              // 创建时间
    private String updated_at;              // 更新时间
    private User user;                      // 创建该话题的用户(信息)
    private String node_name;               // 节点名称
    private int node_id;                    // 节点 id
    private int last_reply_user_id;         // 最近一次回复的用户 id
    private String last_reply_user_login;   // 最近一次回复的用户登录名
    private String replied_at;              // 最近一次回复时间
    private int replies_count;              // 回复总数量
    private String address;                 // 具体地址

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

    public User getUser() {
        return user;
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

    public String getReplied_at() {
        return replied_at;
    }

    public int getReplies_count() {
        return replies_count;
    }

    public String getAddress() {
        return address;
    }
}
