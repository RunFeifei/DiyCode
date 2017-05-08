package com.dianrong.crnetwork.relogin;

import android.text.TextUtils;

import com.dianrong.android.common.utils.ContextUtils;
import com.dianrong.android.common.utils.Log;
import com.dianrong.android.user.UserStatus;
import com.dianrong.crnetwork.NetworkFactory;
import com.dianrong.crnetwork.dataformat.DrRoot;
import com.dianrong.crnetwork.dataformat.EmptyEntity;
import com.dianrong.crnetwork.dataformat.Entity;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;

/**
 * 用于创建一般的Api,内嵌自动登录(tokenLogin)后重发请求的逻辑
 * <p>
 * Created by yangcheng on 16/9/13.
 */
public class LoginServiceCreator {

    private static AutoLoginServiceFactory factory;

    public static AutoLoginServiceFactory getAutoLoginServiceFactory() {
        if (factory == null) {
            factory = (AutoLoginServiceFactory<EmptyEntity>) () -> {
                Log.e("API", "token login ...");
                String token = "";
                String aid = "";
                if (UserStatus.isLoggedIn()) {
                    if (!TextUtils.isEmpty(UserStatus.getToken())) {
                        token = UserStatus.getToken();
                    }
                    if (UserStatus.getUser() != null && !TextUtils.isEmpty(UserStatus.getUser().getAid())) {
                        aid = UserStatus.getUser().getAid();
                    }
                }
                return NetworkFactory.createService(TokenLoginRequest.class)
                        .tokenLogin(token, aid, ContextUtils.getDeviceToken(), ContextUtils.getChannelName());
            };
        }
        return factory;
    }

    /**
     * 为不同的应用创建自动登录服务的工厂接口
     */
    public interface AutoLoginServiceFactory<T extends Entity> {
        /**
         * 用于为不同的应用创建登录服务
         *
         * @return
         */
        Observable<Result<DrRoot<T>>> create();
    }
}
