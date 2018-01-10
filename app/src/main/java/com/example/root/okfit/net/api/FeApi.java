package com.example.root.okfit.net.api;


import com.fei.crnetwork.host.ClassHost;
import com.example.root.okfit.net.dianrong.BreakerItem;
import com.example.root.okfit.net.dianrong.DrList;
import com.example.root.okfit.net.dianrong.DrRoot;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;


@ClassHost(PRODUCT = "https://www.dianrong.com/",DEMO = "https://www-demo.dianrong.com/",DEV = "https://www-demo.dianrong.com/")
public interface FeApi {

    @GET("feapi/errors")
    Observable<Response<DrRoot<DrList<BreakerItem>>>> getBreakers();
}
