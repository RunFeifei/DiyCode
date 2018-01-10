package com.example.root.okfit.net.bean;

import com.fei.crnetwork.dataformat.Entity;

public class Token implements Entity {

    private String access_token;     // 用户令牌(获取相关数据使用)
    private String token_type;       // 令牌类型
    private int expires_in;          // 过期时间
    private String refresh_token;    // 刷新令牌(获取新的令牌)
    private int created_at;          // 创建时间

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public int getCreated_at() {
        return created_at;
    }

    @Override
    public String toString() {
        return "Token{" +
                "access_token='" + access_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in=" + expires_in +
                ", refresh_token='" + refresh_token + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
