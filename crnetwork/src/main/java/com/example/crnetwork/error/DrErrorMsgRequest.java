package com.example.crnetwork.error;

import com.example.crnetwork.dataformat.DrList;
import com.example.crnetwork.dataformat.DrRoot;
import com.example.crnetwork.host.ClassHostMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * <p>
 * Created by yangcheng on 16/7/26.
 * 仅仅适用于Lender
 */
@ClassHostMap(PRODUCT = DianRongHosts.PRODUCT, DEMO = DianRongHosts.DEMO, DEV = DianRongHosts.DEV)
public interface DrErrorMsgRequest {
    @GET("feapi/errors")
    Call<DrRoot<DrList<DrErrorItemBean>>> getErrorMsg(@Query("modifyDate") long modifyDate);
}