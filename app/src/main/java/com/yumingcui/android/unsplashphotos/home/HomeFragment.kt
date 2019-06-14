package com.yumingcui.android.unsplashphotos.home

import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.yumingcui.android.unsplashphotos.R
import com.yumingcui.android.unsplashphotos.home.detail.DetailFragment
import com.yumingcui.android.unsplashphotos.model.NetworkError
import com.yumingcui.android.unsplashphotos.model.NetworkState
import com.yumingcui.android.unsplashphotos.model.Photo
import com.yumingcui.android.unsplashphotos.util.WrapContentStaggeredGridLayoutManager
import kotlinx.android.synthetic.main.home_fragment.*
import androidx.recyclerview.widget.StaggeredGridLayoutManager as StaggeredGridLayoutManager1


class HomeFragment : Fragment(), ISubscriber {
    override fun subscribeNetworkState(networkState: NetworkState) {
        when (networkState) {
            NetworkState.LOADING -> {
                isLoading = true
                progressBar.visibility = View.VISIBLE
            }
            NetworkState.LOADED -> {
                isLoading = false
                progressBar.visibility = View.GONE
            }

            // do nothing, just to list all possibilities
            null -> {

            }
        }
    }

    override fun subscribeNetworkError(networkError: NetworkError) {
        Toast.makeText(activity, "Network error:" + networkError.status.name, Toast.LENGTH_SHORT).show()
    }

    override fun subscribeData(list: List<Photo>) {
        val homeViewModel = (this.activity as HomeActivity).getViewModel()
        photoAdapter?.updateData(homeViewModel.photoCollection, list.size, homeViewModel.currentPage)
    }

    private var isLoading: Boolean = false
    private var justStarted: Boolean = true
    private var photoAdapter: PhotoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        refreshData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startPostponedEnterTransition()
        initView()
        initScrollListener()
        justStarted = false
    }

    override fun onResume() {
        super.onResume()

        photoRecyclerView.post {
            photoRecyclerView.scrollToPosition((activity as HomeActivity).homeViewModel.currentIndex)
        }
    }

    private fun initView() {
        (activity as HomeActivity).setSupportActionBar(toolbar)
        val actionBar = (activity as HomeActivity).supportActionBar
        if (actionBar != null) {
            actionBar.title = getString(R.string.photo_list)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setDisplayShowTitleEnabled(true)
            actionBar.setDisplayUseLogoEnabled(false)
            actionBar.setHomeButtonEnabled(true)
        }

        val layoutManager = WrapContentStaggeredGridLayoutManager(2, StaggeredGridLayoutManager1.VERTICAL)
        photoRecyclerView.layoutManager = layoutManager
        photoRecyclerView.adapter = photoAdapter
        val decoration = SpacesItemDecoration(4)
        photoRecyclerView.addItemDecoration(decoration)
    }


    private fun initScrollListener() {
        photoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager1
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                var firstVisibleItems: IntArray? = null
                var pastVisibleItems = 0
                firstVisibleItems = layoutManager.findFirstVisibleItemPositions(firstVisibleItems)
                if (firstVisibleItems != null && firstVisibleItems.isNotEmpty()) {
                    pastVisibleItems = firstVisibleItems[0]
                }

                if (!isLoading && !justStarted) {
                    if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                        refreshData()
                        isLoading = true
                        progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun init() {
        photoAdapter = PhotoAdapter(object : PhotoAdapter.OnItemClickListener {
            override fun onClickPhoto(
                photo: Photo?,
                position: Int,
                currentPage: Int,
                sharedView: View
            ) {
                if (photo != null) {
                    (activity as HomeActivity).homeViewModel.currentIndex = position
                    val url = photo.urls?.full
                    if (!TextUtils.isEmpty(url)) {
                        (activity as HomeActivity).showFragmentWithTransition(
                            this@HomeFragment,
                            DetailFragment.newInstance(photo.id),
                            "photoDetail",
                            sharedView,
                            "imageTransition${photo.id}"
                        )
                    }
                }
            }
        })
    }

    private fun refreshData() {
        (activity as HomeActivity).loadData()
    }

    inner class SpacesItemDecoration(private val mSpace: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.left = mSpace
            outRect.right = mSpace
            outRect.bottom = mSpace
            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = mSpace
        }
    }
}
