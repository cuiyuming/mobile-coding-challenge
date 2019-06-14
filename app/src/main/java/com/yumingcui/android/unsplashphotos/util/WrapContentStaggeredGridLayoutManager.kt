package com.yumingcui.android.unsplashphotos.util


import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class WrapContentStaggeredGridLayoutManager(spanCount: Int, orientation: Int) :
    StaggeredGridLayoutManager(spanCount, orientation) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }

    }
}