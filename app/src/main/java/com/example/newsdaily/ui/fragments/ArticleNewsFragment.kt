package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.newsdaily.R
import com.example.newsdaily.ui.NewsActivity
import com.example.newsdaily.ui.NewsViewModel

class ArticleNewsFragment : Fragment(R.layout.fragment_article){

    lateinit var viewModel: NewsViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
    }
}