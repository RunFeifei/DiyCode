package com.example.root.okfit.net.api;


import com.dianrong.crnetwork.dataformat.AList;
import com.example.root.okfit.net.bean.Topic;
import com.example.root.okfit.net.bean.TopicDetail;
import com.example.root.okfit.net.bean.TopicReply;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface TopicApi {

    @GET("topics.json")
    Observable<Response<AList<Topic>>> getTopics(@Query("offset") int offset);

    @GET("topics/{id}.json")
    Observable<Response<TopicDetail>> getTopicDetail(@Path("id") int id);

    @GET("topics/{id}/replies.json")
    Observable<Response<AList<TopicReply>>> getTopicRepliesList(@Path("id") int id);
}
