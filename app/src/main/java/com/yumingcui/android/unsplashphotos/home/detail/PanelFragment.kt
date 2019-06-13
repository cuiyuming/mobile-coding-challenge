package com.yumingcui.android.unsplashphotos.home.detail

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.fragment.app.Fragment
import com.yumingcui.android.unsplashphotos.R
import com.yumingcui.android.unsplashphotos.model.Photo
import com.yumingcui.android.unsplashphotos.util.DateUtils

import kotlinx.android.synthetic.main.pannel_fragment.*


class PanelFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pannel_fragment, container, false)
    }

    fun load(photo: Photo){
        titleTextView.text = photo.altDescription
        descriptionTextView.text = photo.description

        likesTextView.text = photo.likes.toString()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            descriptionTextView.text = Html.fromHtml(photo.description ?: " ", FROM_HTML_MODE_LEGACY)
        } else {
            descriptionTextView.text = Html.fromHtml(photo.description)
        }
        descriptionTextView.movementMethod = LinkMovementMethod.getInstance()

        if(photo.user != null){
            authorTextView.text = """by ${photo.user.firstName} ${photo.user.lastName}"""
        }
        if (photo.updatedAt != null) {
            val publishDate = DateUtils.getDateFromServerTimeStamp(photo.updatedAt)
            if (publishDate != null) {
                publishAtTextView.text = DateUtils.formatDate(publishDate)
            }
        }
    }
}
