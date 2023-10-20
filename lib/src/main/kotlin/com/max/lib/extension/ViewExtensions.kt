package com.max.lib.extension

import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.max.lib.utils.glide.MaxImage

fun ImageView.loadImage(imageUrl: String) {
    MaxImage.load(context, imageUrl, this)
}

fun ImageView.loadImageWithPlaceholder(imageUrl: String, placeholderResId: Int) {
    MaxImage.loadWithPlaceholder(context, imageUrl, this, placeholderResId)
}

fun ImageView.loadImageWithCacheStrategy(imageUrl: String, cacheStrategy: DiskCacheStrategy) {
    MaxImage.loadWithCacheStrategy(context, imageUrl, this, cacheStrategy)
}

fun ImageView.loadImageWithCustomOptions(imageUrl: String, requestOptions: RequestOptions) {
    MaxImage.loadWithCustomOptions(context, imageUrl, this, requestOptions)
}