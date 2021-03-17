package com.wangzhenyu.catwatch.service

import com.wangzhenyu.catwatch.data.Girl
import retrofit2.http.GET
import retrofit2.http.Path

interface GirlService {


    // 获取图片信息
    @GET("Girl/type/Girl/page/1/count/{n}")
    suspend fun searchGirl(@Path("n") n: String): Girl

}