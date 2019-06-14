package com.yumingcui.android.unsplashphotos.home

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionInflater
import com.yumingcui.android.unsplashphotos.R
import com.yumingcui.android.unsplashphotos.model.NetworkError
import com.yumingcui.android.unsplashphotos.model.NetworkState
import com.yumingcui.android.unsplashphotos.model.Photo


class HomeActivity : AppCompatActivity() {
    lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        if(savedInstanceState == null) {
            val homeFragment = HomeFragment()
            replaceFragment(homeFragment)
            homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

            homeViewModel.networkState?.observe(this, Observer<NetworkState> { networkState ->
                val fragments = supportFragmentManager.fragments
                for(fragment in fragments){
                    (fragment as? ISubscriber)?.subscribeNetworkState(networkState)
                }
            })

            homeViewModel.networkError?.observe(this, Observer<NetworkError> { networkError ->
                val fragments = supportFragmentManager.fragments
                for(fragment in fragments){
                    (fragment as? ISubscriber)?.subscribeNetworkError(networkError)
                }
            })
        }
    }

    fun loadData(){
        homeViewModel.loadMorePhotos().observe(this, Observer<List<Photo>> { photoList ->
            if (photoList != null) {
                val fragments = supportFragmentManager.fragments
                for(fragment in fragments){
                    (fragment as? ISubscriber)?.subscribeData(photoList)
                }
            }
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_holder, fragment)
            .commitAllowingStateLoss()
    }

    fun showFragmentWithTransition(
        current: Fragment,
        newFragment: Fragment,
        tag: String,
        sharedView: View,
        sharedElementName: String
    ) {
        val fragmentManager = supportFragmentManager
        // check if the fragment is in back stack
        val fragmentPopped = fragmentManager.popBackStackImmediate(tag, 0)
        if (fragmentPopped) {
            // fragment is pop from backStack
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                current.sharedElementReturnTransition =
                    TransitionInflater.from(this).inflateTransition(R.transition.default_transition)
                current.exitTransition =
                    TransitionInflater.from(this).inflateTransition(android.R.transition.no_transition)

                newFragment.sharedElementEnterTransition =
                    TransitionInflater.from(this).inflateTransition(R.transition.default_transition)
                newFragment.enterTransition =
                    TransitionInflater.from(this).inflateTransition(android.R.transition.no_transition)
            }
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_holder, newFragment, tag)
            fragmentTransaction.addToBackStack(tag)
            fragmentTransaction.addSharedElement(sharedView, sharedElementName)
            fragmentTransaction.commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else
            super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    fun getViewModel(): HomeViewModel {
        return homeViewModel
    }
}
