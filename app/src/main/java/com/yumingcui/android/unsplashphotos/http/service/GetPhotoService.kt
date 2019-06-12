package com.yumingcui.android.unsplashphotos.http.service

import com.yumingcui.android.unsplashphotos.model.Photo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetPhotoService {

    @GET("photos")
    fun getPhotos(
        @Query("Authorization") clientId: String,
        @Query("per_page") pageSize: Int,
        @Query("page") page: Int,
        @Query("order_by") orderBy: String
    ): Call<List<Photo>>

}
