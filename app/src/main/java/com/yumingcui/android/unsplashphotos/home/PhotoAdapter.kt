package com.yumingcui.android.unsplashphotos.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yumingcui.android.unsplashphotos.BR

import com.yumingcui.android.unsplashphotos.R
import com.yumingcui.android.unsplashphotos.databinding.PhotoItemBinding

import com.yumingcui.android.unsplashphotos.model.Photo


class PhotoAdapter internal constructor(private val onItemClickListener: OnItemClickListener?) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private var photos: List<Photo> = ArrayList()
    private var currentPage: Int? = null

    fun updateData(photos: List<Photo>, numberOfNewPhotos: Int, currentPage: Int) {
        this.currentPage = currentPage
        this.photos = photos
        this.notifyItemRangeInserted(itemCount - numberOfNewPhotos - 1, numberOfNewPhotos)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): PhotoViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val binding = DataBindingUtil.inflate<PhotoItemBinding>(
            layoutInflater,
            R.layout.photo_item,
            viewGroup,
            false
        )
        binding.setVariable(BR.currentPage, this.currentPage)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, i: Int) {
        holder.binding.setVariable(BR.position, i)
        holder.binding.imageBg.transitionName = "imageTransition" + this.photos[i].id
        holder.binding.photoItemModel = PhotoItemModel(this.photos[i], this.onItemClickListener)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    inner class PhotoViewHolder(val binding: PhotoItemBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onClickPhoto(photo: Photo?, position: Int, currentPage: Int, sharedView: View)
    }

}
