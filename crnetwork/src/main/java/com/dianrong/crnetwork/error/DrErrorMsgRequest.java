package com.dianrong.crnetwork.error;

import com.dianrong.crnetwork.dataformat.DrRoot;
import com.dianrong.crnetwork.host.ClassHostMap;
import com.dianrong.crnetwork.dataformat.DrList;
import com.dianrong.crnetwork.host.dianrong.annotation.DrClassHostMap;
import com.dianrong.crnetwork.host.dianrong.hosts.DianRongHosts;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * <p>
 * Created by yangcheng on 16/7/26.
 * 仅仅适用于Lender
 */
@DrClassHostMap
public interface DrErrorMsgRequest {
    @GET("feapi/errors")
    Call<DrRoot<DrList<DrErrorItemBean>>> getErrorMsg(@Query("modifyDate") long modifyDate);
}