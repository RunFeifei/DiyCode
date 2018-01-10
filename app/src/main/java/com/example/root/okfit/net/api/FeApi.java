package com.example.root.okfit.net.api;


import com.fei.crnetwork.host.ClassHost;
import com.example.root.okfit.net.ddrr.BreakerItem;
import com.example.root.okfit.net.ddrr.DrList;
import com.example.root.okfit.net.ddrr.DrRoot;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;


@ClassHost(PRODUCT = "https://www.dianrong.com/",DEMO = "https://www-demo.dianrong.com/",DEV = "https://www-demo.dianrong.com/")
public interface FeApi {

    @GET("feapi/errors")
    Observable<Response<DrRoot<DrList<BreakerItem>>>> getBreakers();
}
