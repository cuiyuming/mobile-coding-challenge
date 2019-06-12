package com.yumingcui.android.unsplashphotos.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.yumingcui.android.unsplashphotos.R
import com.yumingcui.android.unsplashphotos.http.service.GetPhotoService
import com.yumingcui.android.unsplashphotos.http.service.RetrofitInstance
import com.yumingcui.android.unsplashphotos.model.NetworkError
import com.yumingcui.android.unsplashphotos.model.NetworkState
import com.yumingcui.android.unsplashphotos.model.Photo
import com.yumingcui.android.unsplashphotos.model.Status
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(application: Application) : AndroidViewModel(application)  {
    private var photoLiveData: MutableLiveData<List<Photo>>? = null
    var networkState: MutableLiveData<NetworkState>? = null
    var networkError: MutableLiveData<NetworkError>? = null
    private var getPhotoService: GetPhotoService? = null
    var currentPage: Int = 1
    var totalPages: Int? = null

    fun loadPhoto(page: Int) {
        /*Create handle for the RetrofitInstance interface*/
        getPhotoService = RetrofitInstance.retrofitInstance!!.create(GetPhotoService::class.java)

        val apiKey = getApplication<Application>().applicationContext.getString(R.string.api_key)
        val call = getPhotoService?.getPhotos(apiKey, 20, page, "popular")

        call?.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                val code = response.code()
                if (code in 200..299) {
                    photoLiveData?.postValue(response.body())
                    networkState?.postValue(NetworkState.LOADED)
                } else if (code == 401) {
                    networkState?.postValue(NetworkState.LOADED)
                    networkError?.postValue(NetworkError(Status.forCode(code)))
                } else if (code in 400..499) {
                    networkState?.postValue(NetworkState.LOADED)
                    networkError?.postValue(NetworkError(Status.forCode(code)))
                } else if (code in 500..599) {
                    networkState?.postValue(NetworkState.LOADED)
                    networkError?.postValue(NetworkError(Status.forCode(code)))
                } else {
                    networkState?.postValue(NetworkState.LOADED)
                    // unknown error
                    networkError?.postValue(NetworkError(Status.forCode(0)))
                }
            }
            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                networkState?.postValue(NetworkState.LOADED)
                Log.i("HomeViewModel", "onFailure")
                t.printStackTrace()
            }
        })
    }
}