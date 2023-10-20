package com.max.lib.utils.adapter

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.max.lib.extension.loadImage
import com.max.lib.extension.loadImageWithPlaceholder

object ImageViewAdapter {

    @JvmStatic
    @BindingAdapter("maxBitmap")
    fun setBitmap(iv: ImageView, bitmap: Bitmap?) {
        iv.setImageBitmap(bitmap)
    }

    @BindingAdapter("imageUrl")
    fun ImageView.setImageUrl(imageUrl: String?) {
        if (!imageUrl.isNullOrBlank()) {
            loadImage(imageUrl)
        }
    }

    @BindingAdapter("imageUrl", "placeholderRes", requireAll = false)
    fun ImageView.setImageUrlWithPlaceholder(imageUrl: String?, placeholderResId: Int?) {
        if (!imageUrl.isNullOrBlank()) {
            if (placeholderResId != null) {
                loadImageWithPlaceholder(imageUrl, placeholderResId)
            } else {
                loadImage(imageUrl)
            }
        }
    }

}