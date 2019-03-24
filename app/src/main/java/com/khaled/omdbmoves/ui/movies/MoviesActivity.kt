package com.khaled.omdbmoves.ui.movies

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.khaled.omdbmoves.R
import com.khaled.omdbmoves.binding.PhotoManagerDataBindingComponent
import com.khaled.omdbmoves.databinding.ActivityMoviesBinding
import com.khaled.omdbmoves.di.Injectable
import com.khaled.omdbmoves.ui.details.DetailsActivity
import com.khaled.omdbmoves.utils.extensions.viewModel
import com.khaled.omdbmoves.utils.ui.SimpleDividerItemDecoration
import onTextChanged
import javax.inject.Inject

class MoviesActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var photoManagerDataBindingComponent: PhotoManagerDataBindingComponent
    private lateinit var binding: ActivityMoviesBinding
    private val moviesActivityViewModel by lazy { viewModel<MoviesActivityViewModel>() }
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movies)
        initViews()
        initViewModel()
    }

    private fun initViews() {
        with(binding) {
            lifecycleOwner = this@MoviesActivity
            viewModel = moviesActivityViewModel
            searchMovie.onTextChanged { moviesActivityViewModel.searchMovie(it) }
        }

        moviesAdapter =
            MoviesAdapter(photoManagerDataBindingComponent) {
                startActivity(DetailsActivity.newIntent(this, it))
            }

        with(binding.movies) {
            layoutManager = LinearLayoutManager(this@MoviesActivity)
            adapter = moviesAdapter
            addItemDecoration(SimpleDividerItemDecoration(this@MoviesActivity))
        }
        binding.swipeRefreshLayout.setOnRefreshListener { moviesActivityViewModel.getMovies(true) }
    }

    private fun initViewModel() {
        moviesActivityViewModel.moviesLiveData.observe(this, Observer {
            moviesAdapter.submitList(it)
        })

        moviesActivityViewModel.error.observe(this, Observer {
            Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_LONG).show()
        })
    }

    override fun onDestroy() {
        moviesActivityViewModel.closeRealm()
        super.onDestroy()
    }
}
