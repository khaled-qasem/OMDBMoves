package com.khaled.omdbmoves.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.khaled.omdbmoves.R
import com.khaled.omdbmoves.binding.PhotoManagerDataBindingComponent
import com.khaled.omdbmoves.data.model.Movie
import com.khaled.omdbmoves.databinding.ActivityDetatilsBinding
import com.khaled.omdbmoves.di.Injectable
import javax.inject.Inject

class DetailsActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var photoManagerDataBindingComponent: PhotoManagerDataBindingComponent
    private lateinit var binding: ActivityDetatilsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_detatils,
            photoManagerDataBindingComponent
        )
        initViews()
    }

    override fun onOptionsItemSelected(item: MenuItem?) =
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun initViews() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val movie: Movie? = intent.extras?.getParcelable(MOVIE_EXTRA) as? Movie
        binding.movie = movie
    }

    companion object {
        val MOVIE_EXTRA = DetailsActivity::class.java.simpleName + "_MOVIE_EXTRA"
        fun newIntent(context: Context, movie: Movie) =
            Intent(context, DetailsActivity::class.java).apply {
                putExtra(MOVIE_EXTRA, movie)
            }
    }
}
