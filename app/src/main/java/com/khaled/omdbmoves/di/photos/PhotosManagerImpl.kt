package com.khaled.omdbmoves.di.photos

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.khaled.omdbmoves.BuildConfig
import jp.wasabeef.glide.transformations.BlurTransformation
import java.util.*
import javax.inject.Inject

class PhotosManagerImpl @Inject constructor(
    private val context: Context
) : PhotosManager {
    override fun loadPhoto(
        url: String,
        imageView: ImageView,
        defaultImage: Int,
        circular: Boolean,
        blur: Boolean
    ) {
        if (url.isEmpty()) {
            loadDefaultPhoto(resourceIdToUri(defaultImage), imageView, defaultImage, circular, blur)
            return
        }
        Glide.with(context)
            .load(url)
            .apply(applyRequestOptions(circular, blur))
            .apply(RequestOptions().placeholder(defaultImage))
            .apply(RequestOptions().error(defaultImage))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    private fun loadDefaultPhoto(
        uri: Uri,
        imageView: ImageView,
        @DrawableRes defaultImage: Int,
        circular: Boolean,
        blur: Boolean
    ) {
        Glide.with(context)
            .load(uri)
            .apply(applyRequestOptions(circular, blur))
            .apply(RequestOptions().placeholder(defaultImage))
            .apply(RequestOptions().error(defaultImage))
            .into(imageView)
    }

    private fun applyRequestOptions(circular: Boolean, blur: Boolean): RequestOptions {
        val requestOptions =
            if (circular) RequestOptions.circleCropTransform() else RequestOptions()
        val bitmapTransformation = if (blur) BlurTransformation(5, 10) else null

        return if (blur)
            requestOptions.transform(bitmapTransformation!!)
        else
            requestOptions
    }

    private fun resourceIdToUri(@DrawableRes resourceId: Int): Uri {
        return Uri.parse(String.format(Locale.ENGLISH, ANDROID_RESOURCE_FORMAT, resourceId))
    }

    companion object {
        private const val ANDROID_RESOURCE_FORMAT =
            "android.resource://" + BuildConfig.APPLICATION_ID + "/%d"
    }
}