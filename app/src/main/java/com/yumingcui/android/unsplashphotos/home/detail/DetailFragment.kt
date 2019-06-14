package com.yumingcui.android.unsplashphotos.home.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yumingcui.android.unsplashphotos.R
import com.yumingcui.android.unsplashphotos.home.HomeActivity
import com.yumingcui.android.unsplashphotos.home.ISubscriber
import com.yumingcui.android.unsplashphotos.model.NetworkError
import com.yumingcui.android.unsplashphotos.model.NetworkState
import com.yumingcui.android.unsplashphotos.model.Photo
import com.yumingcui.android.unsplashphotos.util.DateUtils
import kotlinx.android.synthetic.main.detail_fragment.*


class DetailFragment : Fragment(), ISubscriber {
    companion object {
        fun newInstance(id: String): DetailFragment {
            val detailFragment = DetailFragment()
            val args = Bundle()
            args.putString("transitionName", "imageTransition$id")
            detailFragment.arguments = args
            return detailFragment
        }
    }

    override fun subscribeData(list: List<Photo>) {
        val photo = (activity as HomeActivity).homeViewModel.getCurrentPhoto()
        loadPhoto(photo)
    }

    override fun subscribeNetworkState(networkState: NetworkState) {
        when (networkState) {
            NetworkState.LOADING -> {
                Toast.makeText(activity, "Loading", Toast.LENGTH_SHORT).show()
            }
            NetworkState.LOADED -> {

            }
        }
    }

    override fun subscribeNetworkError(networkError: NetworkError) {
        Toast.makeText(activity, "Network error:" + networkError.status.name, Toast.LENGTH_SHORT).show()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.detail_fragment, container, false)
        val bgImageView = rootView.findViewById<PhotoView>(R.id.photoView)
        val bundle = arguments
        val transitionName = bundle?.getString("transitionName")
        bgImageView.transitionName = transitionName
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        configureBackdrop()
    }

    private fun initView() {
        (activity as HomeActivity).setSupportActionBar(toolbar)
        val actionBar = (activity as HomeActivity).supportActionBar
        if (actionBar != null) {
            actionBar.setTitle("")
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setDisplayShowTitleEnabled(true)
            actionBar.setDisplayUseLogoEnabled(false)
            actionBar.setHomeButtonEnabled(true)
        }

        previousButton.setOnClickListener {
            val photo = (activity as HomeActivity).homeViewModel.getPreviousPhoto()
            if (photo == null) {
                Toast.makeText(this.context, "You have reached the first photo", Toast.LENGTH_SHORT).show()
            } else {
                addTransition(photo)
                loadPhoto(photo)
            }
        }

        nextButton.setOnClickListener {
            val photo = (activity as HomeActivity).homeViewModel.getNextPhoto()
            if (photo == null) {
                Toast.makeText(this.context, "Loading...", Toast.LENGTH_SHORT).show()
            } else {
                addTransition(photo)
                loadPhoto(photo)
            }
        }
        val photo = (activity as HomeActivity).homeViewModel.getCurrentPhoto()
        loadPhoto(photo)
    }

    private fun addTransition(photo: Photo) {
        if ((this@DetailFragment.view?.findViewById<PhotoView>(R.id.photoView))?.transitionName.isNullOrEmpty()) {
            (this@DetailFragment.view?.findViewById<PhotoView>(R.id.photoView))?.transitionName =
                "imageTransition" + photo.id
        }
    }

    private fun loadPhoto(photo: Photo?) {
        titleTextView.text = photo?.altDescription

        if (photo?.updatedAt != null) {
            val publishDate = DateUtils.getDateFromServerTimeStamp(photo.updatedAt)
            if (publishDate != null) {
                publishAtTextView.text = DateUtils.formatDate(publishDate)
            }
        }

        val circularProgressDrawable = CircularProgressDrawable(this.context!!)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(this.activity)
            .load(photo?.urls?.full)
            .asBitmap().asIs().dontTransform()
            .placeholder(circularProgressDrawable)
            .fitCenter()
            .into(photoView)

        val fragment = childFragmentManager.findFragmentById(R.id.panelFragment) as PanelFragment

        if (photo != null) {
            fragment.load(photo)
        }
    }


    private var mBottomSheetBehavior: BottomSheetBehavior<View?>? = null

    private fun configureBackdrop() {
        // Get the fragment reference
        val fragment = childFragmentManager.findFragmentById(R.id.panelFragment)

        fragment?.let {
            // Get the BottomSheetBehavior from the fragment view
            BottomSheetBehavior.from(it.view)?.let { bsb ->
                // Set the initial state of the BottomSheetBehavior to HIDDEN
                bsb.state = BottomSheetBehavior.STATE_COLLAPSED

                // Set the trigger that will expand your view
                fab.setOnClickListener {
                    if (bsb.state == BottomSheetBehavior.STATE_COLLAPSED) {
                        bsb.state = BottomSheetBehavior.STATE_EXPANDED
                    } else {
                        bsb.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }

                // Set the reference into class attribute (will be used latter)
                mBottomSheetBehavior = bsb
            }
        }
    }


}
