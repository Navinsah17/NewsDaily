package com.example.newsdaily.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsdaily.R
import com.example.newsdaily.db.ArticleDatabase
import com.example.newsdaily.repository.NewsRepository
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)

        val navHostFragment= supportFragmentManager.findFragmentById(R.id.navhostfragment) as NavHostFragment
        val navController= navHostFragment.navController
        navigationv.setupWithNavController(navController)
    }
}