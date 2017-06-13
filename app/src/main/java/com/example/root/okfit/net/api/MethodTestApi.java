package com.example.root.okfit.net.api;

import com.dianrong.crnetwork.dataformat.DrList;
import com.dianrong.crnetwork.dataformat.DrRoot;
import com.dianrong.crnetwork.host.MethodHostMap;
import com.example.root.okfit.net.bean.BreakerItem;
import com.example.root.okfit.net.bean.ErrorItem;
import com.example.root.okfit.net.host.DianRongHosts;

import annotation.MethodHostSurpported;
import annotation.EscapeProcessorMap;
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
    @EscapeProcessorMap(Escape = false)
    Call<DrRoot<DrList<BreakerItem>>> getBreakers(@Query("platform") String platform);

}
