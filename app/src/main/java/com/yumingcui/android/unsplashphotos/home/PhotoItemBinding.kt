package com.yumingcui.android.unsplashphotos.home

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.yumingcui.android.unsplashphotos.R
import com.yumingcui.android.unsplashphotos.model.Photo
import com.yumingcui.android.unsplashphotos.util.DateUtils


val set = ConstraintSet()

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, photo: Photo) {
    val url = photo?.urls?.thumb
    Glide.with(imageView.rootView.context)
        .load(url)
        .thumbnail(0.2f)
        .into(imageView)

    val ratio =String.format("%d:%d", photo.width, photo.height)
    set.clone(imageView.parent as ConstraintLayout)
    set.setDimensionRatio(R.id.image_bg, ratio)
    set.applyTo(imageView.parent as ConstraintLayout)
}


@BindingAdapter("timeStamp")
fun setTimeStamp(view: TextView, publishedAt: String) {
    val publishDate = DateUtils.getDateFromServerTimeStamp(publishedAt)
    if(publishDate == null){
        view.text = ""
    }else {
        view.text = DateUtils.formatDate(publishDate)
    }
}

class PhotoItemModel(
    var photo: Photo? = null,
    var onItemClickListener: PhotoAdapter.OnItemClickListener?) {

    fun onClick(selectedView: View, currentPage:Int, position:Int) {
        onItemClickListener?.onClickPhoto(photo, position, currentPage, selectedView.findViewById<ImageView>(R.id.image_bg))
    }
}
