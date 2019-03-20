package com.khaled.omdbmoves.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.khaled.omdbmoves.R
import com.khaled.omdbmoves.di.photos.PhotosManager

/**
 * Binding adapters that work in activities or fragments using a [PhotosManager] instance.
 */
class PhotoManagerBindingAdapters(private val photosManager: PhotosManager) {

    @BindingAdapter("url", "circular", "blur", requireAll = false)
    fun loadImage(imageView: ImageView, url: String?, circular: Boolean?, blur: Boolean?) {
        var tmdbUrl: String? = null
        val prefix = url?.split("/")?.get(1)
        prefix?.let {
            tmdbUrl = "https://image.tmdb.org/t/p/w185//$prefix"
        }
        photosManager.loadPhoto(
            tmdbUrl ?: "",
            imageView,
            R.drawable.default_movie,
            circular ?: false,
            blur ?: false
        )
    }
}