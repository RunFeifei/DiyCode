package com.example.root.okfit.net.api;

import com.fei.crnetwork.dataformat.AList;
import com.example.root.okfit.net.bean.Sites;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

public interface SitesApi {

    @GET("sites.json")
    Observable<Response<AList<Sites>>> getSites();
}
