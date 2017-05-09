package com.dianrong.crnetwork;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * <p/> 统一HttpClient
 * Created by yangcheng on 2016/12/28.
 * 在Manifest中注册
 */
public class CrGlideModule implements GlideModule {

    /**
     *改变Bitmap的格式,可以将默认的RGB_565转化为RGB_8888,是
     *builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
     */
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

    }

    /**
     * 用来在Glide单例创建之后但请求发起之前注册组件
     */
    @Override
    public void registerComponents(Context context, Glide glide) {

        OkHttpClient client = CrNetworkFactory.getClient();
        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(client);
        glide.register(GlideUrl.class, InputStream.class, factory);
    }
}
