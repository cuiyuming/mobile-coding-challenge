package com.yumingcui.android.unsplashphotos.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.yumingcui.android.unsplashphotos.http.service.GetPhotoService
import com.yumingcui.android.unsplashphotos.http.service.RetrofitInstance
import com.yumingcui.android.unsplashphotos.model.NetworkError
import com.yumingcui.android.unsplashphotos.model.NetworkState
import com.yumingcui.android.unsplashphotos.model.Photo
import com.yumingcui.android.unsplashphotos.model.Status
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    var photoCollection = ArrayList<Photo>()
    var photoLiveData: MutableLiveData<List<Photo>>? = null
    var networkState: MutableLiveData<NetworkState>? = null
    var networkError: MutableLiveData<NetworkError>? = null
    private var getPhotoService: GetPhotoService? = null
    var currentPage: Int = 0
    var currentIndex: Int = 0

    init {
        if (photoLiveData == null) {
            photoLiveData = MutableLiveData()
        }
        if (networkState == null) {
            networkState = MutableLiveData()
        }
        loadMorePhotos()
        networkState?.postValue(NetworkState.LOADING)
    }


    fun loadMorePhotos(): MutableLiveData<List<Photo>> {
        if (photoLiveData == null) {
            photoLiveData = MutableLiveData()
        }
        if (networkState == null) {
            networkState = MutableLiveData()
        }
        networkState?.postValue(NetworkState.LOADING)

        currentPage++

        loadPhoto(currentPage)

        return photoLiveData as MutableLiveData<List<Photo>>
    }

    private fun loadPhoto(page: Int) {
        /*Create handle for the RetrofitInstance interface*/
        getPhotoService = RetrofitInstance.retrofitInstance!!.create(GetPhotoService::class.java)

        val call = getPhotoService?.getPhotos(20, page, "popular")

        call?.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                val code = response.code()
                if (code in 200..299) {
                    photoCollection.addAll(response.body())
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

    fun getCurrentPhoto(): Photo? {
        return photoCollection.get(currentIndex)
    }

    fun getNextPhoto(): Photo? {
        currentIndex++

        var result: Photo? = null
        if (currentIndex < photoCollection.size)
            result = photoCollection.get(currentIndex)
        else {
            loadMorePhotos()
            return result
        }
        return result
    }

    fun getPreviousPhoto(): Photo? {
        var result: Photo? = null

        if (currentIndex > 0) {
            currentIndex--
        }

        if (currentIndex < photoCollection.size)
            result = photoCollection.get(currentIndex)
        else {
            return result
        }
        return result
    }

}