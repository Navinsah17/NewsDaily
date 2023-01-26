package com.androiddevs.mvvmnewsapp.ui.fragments

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.newsdaily.R
import com.example.newsdaily.adapter.NewsAdapter
import com.example.newsdaily.models.Article
import com.example.newsdaily.ui.NewsActivity
import com.example.newsdaily.ui.NewsViewModel
import com.example.newsdaily.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.item_article_preview.*

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    val TAG = "BreakingNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setUpRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment2_to_articleNewsFragment2,
                bundle
            )
        }
        newsAdapter.setOnShareNewsClick {
            shareClick(ivArticleImage, it)
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.d(TAG, "An error occured $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }

            }
        })
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun shareClick(imageView: ImageView, item: Article) {
        val intent = Intent(Intent.ACTION_SEND)
        if (imageView.height > 10) {
            val bitmap = imageView!!.drawable as BitmapDrawable
            val bitmap1 = bitmap.bitmap
            val path = MediaStore.Images.Media.insertImage(
                requireActivity().contentResolver,
                bitmap1,
                item.title,
                null
            )
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
        }
        intent.type = "text/*"
        intent.putExtra(Intent.EXTRA_TEXT, item.title + "\n checkout here " + item.url)
        val chooser = Intent.createChooser(intent, "share this news using.")
        startActivity(chooser, null)

    }
}