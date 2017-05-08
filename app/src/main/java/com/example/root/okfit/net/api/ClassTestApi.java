package com.example.root.okfit.net.api;

import com.dianrong.crnetwork.dataformat.DrList;
import com.dianrong.crnetwork.dataformat.DrRoot;
import com.dianrong.crnetwork.host.ClassHostMap;
import com.example.root.okfit.net.bean.BreakerItem;
import com.example.root.okfit.net.bean.ErrorItem;
import com.example.root.okfit.net.host.DianRongHosts;

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
