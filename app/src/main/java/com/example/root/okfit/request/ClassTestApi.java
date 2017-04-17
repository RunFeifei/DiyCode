package com.example.root.okfit.request;

import com.example.crnetwork.dataformat.DrList;
import com.example.crnetwork.dataformat.DrRoot;
import com.example.crnetwork.host.ClassHostMap;
import com.example.crnetwork.host.MethodHostMap;
import com.example.root.okfit.bean.breakers.BreakerItem;
import com.example.root.okfit.bean.errors.ErrorItem;
import com.example.root.okfit.net.baseurlservices.hosts.DianRongHosts;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by PengFeifei on 17-4-17.
 */
@ClassHostMap(PRODUCT = DianRongHosts.PRODUCT, DEMO = DianRongHosts.DEMO, DEV = DianRongHosts.DEV)
public interface ClassTestApi {

    @GET("feapi/errors")
    Call<DrRoot<DrList<ErrorItem>>> getErros();

    @GET("feapi/breakers")
    Call<DrRoot<DrList<BreakerItem>>> getBreakers(@Query("platform") String platform);
}
