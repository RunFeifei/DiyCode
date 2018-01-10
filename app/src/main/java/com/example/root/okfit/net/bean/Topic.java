package com.example.root.okfit.net.bean;

import com.fei.crnetwork.dataformat.Entity;

/**
 * Created by PengFeifei on 17-7-25.
 */

public class Topic implements Entity {

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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUpdated_at() {
        return this.updated_at;
    }

    public void setReplied_at(String replied_at) {
        this.replied_at = replied_at;
    }

    public String getReplied_at() {
        return this.replied_at;
    }

    public void setReplies_count(int replies_count) {
        this.replies_count = replies_count;
    }

    public int getReplies_count() {
        return this.replies_count;
    }

    public void setNode_name(String node_name) {
        this.node_name = node_name;
    }

    public String getNode_name() {
        return this.node_name;
    }

    public void setNode_id(int node_id) {
        this.node_id = node_id;
    }

    public int getNode_id() {
        return this.node_id;
    }

    public void setLast_reply_user_id(int last_reply_user_id) {
        this.last_reply_user_id = last_reply_user_id;
    }

    public int getLast_reply_user_id() {
        return this.last_reply_user_id;
    }

    public void setLast_reply_user_login(String last_reply_user_login) {
        this.last_reply_user_login = last_reply_user_login;
    }

    public String getLast_reply_user_login() {
        return this.last_reply_user_login;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean getDeleted() {
        return this.deleted;
    }

    public void setExcellent(boolean excellent) {
        this.excellent = excellent;
    }

    public boolean getExcellent() {
        return this.excellent;
    }

    public void setAbilities(Abilities abilities) {
        this.abilities = abilities;
    }

    public Abilities getAbilities() {
        return this.abilities;
    }

}
