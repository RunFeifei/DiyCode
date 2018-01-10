package com.example.root.okfit.net.api;


import com.fei.crnetwork.dataformat.AList;
import com.example.root.okfit.net.bean.News;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface NewsApi {

    @GET("news.json")
    Observable<Response<AList<News>>> getNews(@Query("offset") int offset);

}
