package com.example.root.okfit.request;

import com.example.crnetwork.dataformat.DrList;
import com.example.crnetwork.dataformat.DrRoot;
import com.example.crnetwork.host.MethodHostMap;
import com.example.root.okfit.bean.breakers.BreakerItem;
import com.example.root.okfit.bean.errors.ErrorItem;
import com.example.root.okfit.net.baseurlservices.hosts.DianRongHosts;

import annotation.MethodHostSurpported;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by PengFeifei on 17-4-17.
 */
public interface MethodTestApi {

    @MethodHostMap(PRODUCT = DianRongHosts.PRODUCT, DEMO = DianRongHosts.DEMO, DEV = DianRongHosts.DEV)
    @GET("feapi/errors")
    @MethodHostSurpported(Surpported = true)
    Call<DrRoot<DrList<ErrorItem>>> getErros();


    @MethodHostMap(PRODUCT = DianRongHosts.PRODUCT, DEMO = DianRongHosts.DEMO, DEV = DianRongHosts.DEV)
    @GET("feapi/breakers")
    @MethodHostSurpported(Surpported = true)
    Call<DrRoot<DrList<BreakerItem>>> getBreakers(@Query("platform") String platform);

}
