package com.dianrong.crnetwork.relogin;

import com.dianrong.crnetwork.dataformat.EmptyEntity;
import com.dianrong.crnetwork.dataformat.DrRoot;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * <p>
 * Created by yangcheng on 16/9/19.
 */
public interface TokenLoginRequest {
    @POST("api/v2/users/login/token")
    @FormUrlEncoded
    Observable<Result<DrRoot<EmptyEntity>>> tokenLogin(
            @Field("accessToken") String token,
            @Field("userName") String userName,
            @Field("DEVICE_TOKEN") String deviceToken,
            @Field("MSG_CHANNEL") String msgChannel);
}
