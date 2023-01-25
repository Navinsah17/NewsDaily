package com.example.newsdaily.repository

import com.example.newsdaily.api.RetrofitInstance
import com.example.newsdaily.db.ArticleDatabase
import com.example.newsdaily.models.Article
import retrofit2.Retrofit
import retrofit2.http.Query

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String,pageNumber: Int)=
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)
//---------------------------------------------------------------

    suspend fun searchNews(searchQuery: String,pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}