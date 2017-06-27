package com.example.root.okfit.net.bean;

import com.dianrong.crnetwork.dataformat.Entity;

/**
 * Created by PengFeifei on 17-7-25.
 */
public class User implements Entity {
    private int id;

    private String login;

    private String name;

    private String avatar_url;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return this.login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getAvatar_url() {
        return this.avatar_url;
    }

}