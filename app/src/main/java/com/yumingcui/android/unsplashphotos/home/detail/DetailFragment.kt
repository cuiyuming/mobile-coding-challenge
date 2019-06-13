package com.yumingcui.android.unsplashphotos.home.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide

import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yumingcui.android.unsplashphotos.R
import com.yumingcui.android.unsplashphotos.home.HomeActivity
import com.yumingcui.android.unsplashphotos.home.HomeViewModel
import com.yumingcui.android.unsplashphotos.model.Photo
import com.yumingcui.android.unsplashphotos.util.DateUtils
import kotlinx.android.synthetic.main.detail_fragment.*


class DetailFragment : Fragment() {

    companion object {
        private val ARG_PHOTO = "DetailFragmentPhotoArg"

        fun newInstance(): DetailFragment {
            return DetailFragment()
        }
    }

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel = activity?.run {
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
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
            val photo = homeViewModel.getPreviousPhoto()
            if(photo == null){
                Toast.makeText(this.context, "You have reached the first photo", Toast.LENGTH_SHORT).show()
            }else{
                loadPhoto(photo)
            }
        }

        nextButton.setOnClickListener {
            val photo = homeViewModel.getNextPhoto()
            if(photo == null){
                Toast.makeText(this.context, "Loading...", Toast.LENGTH_SHORT).show()
            }else{
                loadPhoto(photo)
            }
        }

        val photo = homeViewModel.getCurrentPhoto()

        loadPhoto(photo)
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

    private fun refreshData() {
        homeViewModel.loadMorePhotos().observe(this, Observer<List<Photo>> { photoList ->
            if (photoList != null) {
                val photo = homeViewModel.getCurrentPhoto()
                loadPhoto(photo)
            }
        })
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
