package com.bangkit.artnesia.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.data.model.Article
import com.bangkit.artnesia.databinding.ActivityDetailArticleBinding
import com.bangkit.artnesia.ui.adapter.ArticleAdapter
import com.bumptech.glide.Glide
import com.google.firebase.firestore.*
import java.io.IOException

class DetailArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailArticleBinding
    private lateinit var mArticleDetail: Article

    private var mArticleId: String = ""
    private val mFireStore = FirebaseFirestore.getInstance()

    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var articleList: ArrayList<Article>
    private lateinit var articleRV: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(EXTRA_ARTICLE_ID)) {
            mArticleId =
                intent.getStringExtra(EXTRA_ARTICLE_ID)!!
        }

        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        getArticleDetails(mArticleId)

        getArticle()
    }

    private fun getArticleDetails(articleId: String) {

        mFireStore.collection(ARTICLE)
            .document(articleId)
            .get()
            .addOnSuccessListener { document ->
                Log.e(this.javaClass.simpleName, document.toString())

                val artc = document.toObject(Article::class.java)!!
                this.articleDetailsSuccess(artc)
            }
            .addOnFailureListener { e ->
                Log.e(this.javaClass.simpleName, "Error while getting the article details.", e)
            }
    }

    private fun articleDetailsSuccess(article: Article) {

        mArticleDetail = article

        loadArticlePicture(article.image, binding.ivArticleDetail)

        binding.tvDetailArticleTittle.text = article.name
        binding.tvArticleDesc.text = article.description

    }

    private fun loadArticlePicture(image: Any, imageView: ImageView) {
        try {
            Glide
                .with(this)
                .load(image)
                .into(imageView)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getArticle() {
        articleRV = binding.rvArticleMore
        articleRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        articleRV.setHasFixedSize(true)

        articleList = arrayListOf()

        articleAdapter = ArticleAdapter(this, articleList)

        articleRV.adapter = articleAdapter

        getArticleData()
    }

    private fun getArticleData() {
        mFireStore.collection("article")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("Firestore error", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            val artc = dc.document.toObject(Article::class.java)
                            artc.article_id = dc.document.id

                            articleList.add(artc)
                        }
                    }
                    articleList.shuffle()

                    articleAdapter.notifyDataSetChanged()
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }

    companion object {
        const val EXTRA_ARTICLE_ID: String = "extra_article_id"
        const val ARTICLE: String = "article"
    }
}