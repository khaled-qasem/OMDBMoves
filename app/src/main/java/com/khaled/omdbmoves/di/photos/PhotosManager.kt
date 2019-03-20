package com.khaled.omdbmoves.di.photos

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation

interface PhotosManager {

    /**
     * Loads a photo from `url` into `imageView` using [Glide].
     *
     *
     * @param url          the URL for the photo to be loaded.
     * @param imageView    the imageView into which the photo will be loaded.
     * @param defaultImage the default image that will be used as a placeholder or when loading of the photo fails.
     * @param circular     if true, [RequestOptions.circleCropTransform] will be applied to `imageView`.
     * @param blur         if true, [BlurTransformation] will be applied to `imageView`.
     */
    fun loadPhoto(
            url: String,
            imageView: ImageView,
            @DrawableRes defaultImage: Int,
            circular: Boolean,
            blur: Boolean
    )
}