package com.example.newsdaily.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsdaily.R
import com.example.newsdaily.models.Article
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
    private val items:ArrayList<Article> = ArrayList()
    //inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,differCallback)

    class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder{
        //val inflater = LayoutInflater.from(parent.context)
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_article_preview,
            parent,false)
//        val viewHolder=NewsViewHolder(view)
//
//        view.findViewById<ImageButton>(R.id.imageButton).setOnClickListener{
//            listener.shareClick(view.findViewById<ImageView>(R.id.ivArticleImage),items[viewHolder.adapterPosition])
//        }
        return ArticleViewHolder(view)

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(ivArticleImage)
            tvSource.text = article.source.name
            tvTitle.text = article.title
            tvDescription.text = article.description
            tvPublishedAt.text = article.publishedAt
            setOnClickListener{
                onItemClickListener?.let { it(article) }

            }
        }
        holder.itemView.imageButton.setOnClickListener{
                onShareNewsClick?.let {
                    it(article)
                }

        }
    }


//    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
//        val ivArticleImage:ImageView = itemView.findViewById(R.id.ivArticleImage)
//        val tvSource: TextView = itemView.findViewById(R.id.tvSource)
//        val tvPublishedAt: TextView = itemView.findViewById(R.id.tvPublishedAt)
//        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
//
//    }

    private var onItemClickListener: ((Article) -> Unit)? = null
    private var onShareNewsClick: ((Article) -> Unit)? = null


    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
    fun setOnShareNewsClick(listener: (Article) -> Unit) {
        onShareNewsClick = listener
    }

//    interface NewsClicked{
//        fun shareClick(imageView: ImageView,item : Article)
//    }

}