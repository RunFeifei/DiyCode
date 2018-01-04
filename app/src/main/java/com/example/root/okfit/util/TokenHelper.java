package com.example.root.okfit.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.root.okfit.net.bean.Token;
import com.fei.root.common.ACache;

/**
 * Created by PengFeifei on 17-7-31.
 */

public class TokenHelper {

    ACache cache;

    public TokenHelper(Context context) {
        cache = ACache.get(context);
    }

    public void saveToken(@NonNull Token token) {
        cache.put("token", token);
    }

    public Token getToken() {
        return (Token) cache.getAsObject("token");
    }

    public void clearToken() {
        cache.remove("token");
    }
}
