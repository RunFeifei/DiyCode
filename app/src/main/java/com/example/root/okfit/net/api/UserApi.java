package com.example.root.okfit.net.api;

import com.dianrong.crnetwork.dataformat.AList;
import com.example.root.okfit.net.bean.State;
import com.example.root.okfit.net.bean.Topic;
import com.example.root.okfit.net.bean.TopicReply;
import com.example.root.okfit.net.bean.User;
import com.example.root.okfit.net.bean.UserDetail;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface UserApi {

    /**
     * 获取用户列表
     *
     * @param limit 数量极限，默认值 20，值范围 1..150
     * @return 用户列表
     */
    @GET("users.json")
    Observable<Response<AList<User>>> getUsersList(@Query("limit") Integer limit);


    /**
     * 获取当前登录者的详细资料
     *
     * @return 用户详情
     */
    @GET("users/me.json")
    Observable<Response<UserDetail>> getMe();

    /**
     * 屏蔽用户
     *
     * @param login_name 登录用户名(非昵称)
     * @return 状态
     */
    @Deprecated
    @POST("users/{login}/block.json")
    Observable<Response<State>> blockUser(@Path("login") String login_name);

    /**
     * 取消屏蔽用户
     *
     * @param login_name 登录用户名(非昵称)
     * @return 状态
     */
    @Deprecated
    @POST("users/{login}/unblock.json")
    Observable<Response<State>> unBlockUser(@Path("login") String login_name);

    /**
     * 获取用户屏蔽的用户列表(只能获取自己的)
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @return 被屏蔽的用户列表
     */
    @GET("users/{login}/blocked.json")
    Observable<Response<AList<User>>> getUserBlockedList(@Path("login") String login_name,
                                                         @Query("offset") Integer offset, @Query("limit") Integer limit);

    //--- user follow ------------------------------------------------------------------------------

    /**
     * 关注用户
     *
     * @param login_name 登录用户名(非昵称)
     * @return 状态
     */
    @Deprecated
    @POST("users/{login}/follow.json")
    Observable<Response<State>> followUser(@Path("login") String login_name);

    /**
     * 取消关注用户
     *
     * @param login_name 登录用户名(非昵称)
     * @return 状态
     */
    @Deprecated
    @POST("users/{login}/unfollow.json")
    Observable<Response<State>> unFollowUser(@Path("login") String login_name);

    /**
     * 用户正在关注的人列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @return 用户关注的人列表
     */
    @GET("users/{login}/following.json")
    Observable<Response<AList<User>>> getUserFollowingList(@Path("login") String login_name,
                                                           @Query("offset") Integer offset, @Query("limit") Integer limit);

    /**
     * 关注该用户的人列白哦
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @return 关注该用户的人列表
     */
    @GET("users/{login}/followers.json")
    Observable<Response<AList<User>>> getUserFollowerList(@Path("login") String login_name,
                                                          @Query("offset") Integer offset, @Query("limit") Integer limit);


    //--- user list --------------------------------------------------------------------------------

    /**
     * 用户收藏的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @return 话题列表
     */
    @GET("users/{login}/favorites.json")
    Observable<Response<AList<Topic>>> getUserCollectionTopicList(@Path("login") String login_name,
                                                                  @Query("offset") Integer offset, @Query("limit") Integer limit);


    /**
     * 获取用户创建的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param order      类型 默认 recent，可选["recent", "likes", "replies"]
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @return 话题列表
     */
    @GET("users/{login}/topics.json")
    Observable<Response<AList<Topic>>> getUserCreateTopicList(@Path("login") String login_name, @Query("order") String order,
                                                              @Query("offset") Integer offset, @Query("limit") Integer limit);


    /**
     * 用户回复过的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param order      类型 默认 recent，可选["recent"]
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @return 话题列表
     */
    @GET("users/{login}/replies.json")
    Observable<Response<AList<TopicReply>>> getUserReplyTopicList(@Path("login") String login_name, @Query("order") String order,
                                                                  @Query("offset") Integer offset, @Query("limit") Integer limit);
}
