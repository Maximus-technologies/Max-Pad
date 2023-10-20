package com.max.lib.utils.glide

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

object MaxImage {
    fun load(context: Context, imageUrl: String, imageView: ImageView) {
        Glide.with(context)
            .load(imageUrl)
            .into(imageView)
    }

    fun loadWithPlaceholder(
        context: Context,
        imageUrl: String,
        imageView: ImageView,
        placeholderResId: Int
    ) {
        Glide.with(context)
            .load(imageUrl)
            .placeholder(placeholderResId)
            .into(imageView)
    }

    fun loadWithCacheStrategy(
        context: Context,
        imageUrl: String,
        imageView: ImageView,
        cacheStrategy: DiskCacheStrategy
    ) {
        Glide.with(context)
            .load(imageUrl)
            .diskCacheStrategy(cacheStrategy)
            .into(imageView)
    }

    fun loadWithCustomOptions(
        context: Context,
        imageUrl: String,
        imageView: ImageView,
        requestOptions: RequestOptions
    ) {
        Glide.with(context)
            .load(imageUrl)
            .apply(requestOptions)
            .into(imageView)
    }

    fun clearCache(context: Context) {
        Glide.get(context).clearMemory()
        Glide.get(context).clearDiskCache()
    }
}
