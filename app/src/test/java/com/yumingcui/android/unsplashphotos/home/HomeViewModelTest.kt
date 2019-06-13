package com.yumingcui.android.unsplashphotos.home


import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import com.jraska.livedata.TestObserver
import com.yumingcui.android.unsplashphotos.model.NetworkError
import com.yumingcui.android.unsplashphotos.model.NetworkState
import com.yumingcui.android.unsplashphotos.model.Photo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock

import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HomeViewModelTest {
    val context = ApplicationProvider.getApplicationContext<Context>()

    lateinit var photoLiveData: MutableLiveData<List<Photo>>

    private lateinit var networkStateLiveData: MutableLiveData<NetworkState>
    private lateinit var networkErrorLiveData: MutableLiveData<NetworkError>

    @Mock
    lateinit var homeViewModel: HomeViewModel

    @get:Rule
    val testRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        photoLiveData = homeViewModel.photoLiveData!!
        networkStateLiveData = homeViewModel.networkState!!
        networkErrorLiveData = homeViewModel.networkError!!
    }

    @After
    fun tearDown() {
    }

    @Test
    fun loadPhoto() {

    }
}