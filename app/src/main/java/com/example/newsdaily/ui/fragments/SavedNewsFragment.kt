package com.androiddevs.mvvmnewsapp.ui.fragments

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsdaily.R
import com.example.newsdaily.adapter.NewsAdapter
import com.example.newsdaily.models.Article
import com.example.newsdaily.ui.NewsActivity
import com.example.newsdaily.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_saved_news.*
import kotlinx.android.synthetic.main.item_article_preview.*

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news){

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setUpRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_savedNewsFragment2_to_articleNewsFragment2,
                bundle
            )
        }
        newsAdapter.setOnShareNewsClick {
            shareClick(ivArticleImage,it)

        }
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view, "Successfully deleted Article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.savedArticle(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvSavedNews)
        }

        viewModel.getSavedArticle().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles)
        })
  }
    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        rvSavedNews.apply {
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
        val chooser = Intent.createChooser(intent, "share this meme using.")
        startActivity(chooser, null)

    }
}