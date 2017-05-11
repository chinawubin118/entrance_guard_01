package com.ruitu.entrance_guard.model.api;

import com.google.gson.JsonObject;
import com.ruitu.entrance_guard.model.PageModel;
import com.ruitu.entrance_guard.model.bean.NoticeBean;
import com.ruitu.entrance_guard.model.bean.User2;
import com.ruitu.entrance_guard.model.bean.WeatherBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wubin on 2017/4/20.
 */

public interface ApiService {
    /**
     * 测试登录2
     */
    @FormUrlEncoded
    @POST("login2?method=login2")
    Observable<JsonObject> login2(@Field("username") String username, @Field("password") String password);

    /**
     * 测试获取用户列表
     *
     * @param currentPage 当前页码
     */
    @FormUrlEncoded
    @POST("login2?method=getUserList")
    Observable<PageModel<User2>> getUserList(@Field("currentPage") int currentPage);


    /***********************************上面的没用***********************************/

    /**
     * 获取天气信息(调用的是心知天气的接口)
     */
    @GET("weather/now.json?key=tyc5m2krtykod7bw&location=jinan&language=zh-Hans&unit=c")
    Observable<WeatherBean> getWeatherInfo(@Query("zzz") int zzz);

    /**
     * 获取公告:程序启动的时候调用一次
     */
    @GET("notice/getOneNotice")
    Observable<NoticeBean> getNotice();
}