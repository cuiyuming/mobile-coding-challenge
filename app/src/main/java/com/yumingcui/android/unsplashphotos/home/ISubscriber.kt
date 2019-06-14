package com.yumingcui.android.unsplashphotos.home

import com.yumingcui.android.unsplashphotos.model.NetworkError
import com.yumingcui.android.unsplashphotos.model.NetworkState
import com.yumingcui.android.unsplashphotos.model.Photo

interface ISubscriber {
    fun subscribeData(list:List<Photo>)
    fun subscribeNetworkState(networkState: NetworkState)
    fun subscribeNetworkError(networkError: NetworkError)
}