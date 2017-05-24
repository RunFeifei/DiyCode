package com.example.root.okfit.net.api;

import com.dianrong.crnetwork.dataformat.DrList;
import com.dianrong.crnetwork.dataformat.DrRoot;
import com.dianrong.crnetwork.host.ClassHostMap;
import com.example.root.okfit.net.bean.ErrorItem;
import com.example.root.okfit.net.host.DianRongHosts;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by PengFeifei on 17-4-17.
 */
@ClassHostMap(PRODUCT = DianRongHosts.PRODUCT, DEMO = DianRongHosts.DEMO, DEV = DianRongHosts.DEV)
public interface DiSanfangApi {


    @GET("feapi/errors")
    Call<ErrorItem> disanfang();

}
