package com.khaled.omdbmoves.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.khaled.omdbmoves.R
import com.khaled.omdbmoves.databinding.ActivityMainBinding
import com.khaled.omdbmoves.di.Injectable
import com.khaled.omdbmoves.utils.extensions.viewModel

class MainActivity : AppCompatActivity(), Injectable {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by lazy { viewModel<MainActivityViewModel>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
}
