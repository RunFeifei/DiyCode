/*
 * Copyright 2017 GcsSloop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2017-03-08 01:01:18
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.example.root.okfit.net.bean;

import com.fei.crnetwork.dataformat.Entity;
import com.example.root.okfit.R;
import com.fei.root.recater.adapter.multi.ItemModule;

/**
 * 用户详细信息
 */
public class UserDetail implements Entity ,ItemModule {

    private int id;                     // ID
    private String login;               // 用户名
    private String name;                // 昵称
    private String avatar_url;          // 头像链接
    private String location;            // 城市
    private String company;             // 公司
    private String twitter;             // twitter
    private String website;             // 网站地址
    private String bio;                 // 个人介绍
    private String tagline;             // 签名
    private String github;              // github
    private String created_at;          // 创建时间
    private String email;               // email
    private int topics_count;           // 话题数量
    private int replies_count;          // 回复数量
    private int following_count;        // 正在 follow 的人数
    private int followers_count;        // follow 他的人数
    private int favorites_count;        // 收藏的数量
    private String level;               // 等级
    private String level_name;          // 等级名称

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public String getLocation() {
        return location;
    }

    public String getCompany() {
        return company;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getWebsite() {
        return website;
    }

    public String getBio() {
        return bio;
    }

    public String getTagline() {
        return tagline;
    }

    public String getGithub() {
        return github;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getEmail() {
        return email;
    }

    public int getTopics_count() {
        return topics_count;
    }

    public int getReplies_count() {
        return replies_count;
    }

    public int getFollowing_count() {
        return following_count;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public int getFavorites_count() {
        return favorites_count;
    }

    public String getLevel() {
        return level;
    }

    public String getLevel_name() {
        return level_name;
    }

    @Override
    public int itemViewLayoutId() {
        return R.layout.item_my_head_view;
    }
}
