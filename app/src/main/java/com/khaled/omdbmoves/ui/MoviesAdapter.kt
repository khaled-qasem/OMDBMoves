package com.khaled.omdbmoves.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.khaled.omdbmoves.R
import com.khaled.omdbmoves.binding.PhotoManagerDataBindingComponent
import com.khaled.omdbmoves.data.model.Movie
import com.khaled.omdbmoves.databinding.ListItemMovieBinding
import com.khaled.omdbmoves.utils.ui.DataBoundListAdapter

class MoviesAdapter(private val photoManagerDataBindingComponent: PhotoManagerDataBindingComponent) :
    DataBoundListAdapter<Movie, ListItemMovieBinding>(
        object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.title == newItem.title
            }
        }
    ) {
    override fun createBinding(parent: ViewGroup): ListItemMovieBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_movie,
            parent,
            false,
            photoManagerDataBindingComponent
        )
    }

    override fun bind(binding: ListItemMovieBinding, item: Movie) {
        binding.movie = item
    }
}