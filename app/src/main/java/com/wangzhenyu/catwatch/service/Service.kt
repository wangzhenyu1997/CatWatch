package com.wangzhenyu.catwatch.service

import com.wangzhenyu.catwatch.data.Pixabay
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Service {


    //获取返回的Pixabay图片
    @GET("api/?key=12472743-874dc01dadd26dc44e0801d61&image_type=photo")
    suspend fun searchFromPixabay(
        @Query("q") question: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): Pixabay
}