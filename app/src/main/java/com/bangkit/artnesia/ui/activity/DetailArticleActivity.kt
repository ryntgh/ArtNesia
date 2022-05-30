package com.bangkit.artnesia.ui.activity

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.artnesia.R
import com.bangkit.artnesia.data.local.ArticleData
import com.bangkit.artnesia.data.model.ArticleModel
import com.bangkit.artnesia.databinding.ActivityDetailArticleBinding
import com.bangkit.artnesia.ui.adapter.ArticleAdapter
import com.bumptech.glide.Glide

class DetailArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailArticleBinding
    private lateinit var articleAdapter: ArticleAdapter

    private fun getArticle(): List<ArticleModel> {
        return ArticleData.generateArticle()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        articleAdapter = ArticleAdapter(this)
        articleAdapter.setData(getArticle())

        binding.rvArticleMore.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvArticleMore.setHasFixedSize(true)
        binding.rvArticleMore.adapter = articleAdapter

        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val img: ImageView = findViewById(R.id.iv_article_detail)
        val nameTxt: TextView = findViewById(R.id.tv_detail_articleTittle)
        val summaryTxt: TextView = findViewById(R.id.tv_article_desc)
        val moreText: TextView = findViewById(R.id.tv_article_another)

        val i = this.intent

        val images = i.extras!!.getString("IMAGE_KEY")
        val name = i.extras!!.getString("NAME_KEY")
        val summary = i.extras!!.getString("SUMMARY_KEY")
        moreText.text = getString(R.string.find_another)
        title = "Detail $name"

        img.setImageURI(Uri.parse(images))
        nameTxt.text = name
        summaryTxt.text = summary

        Glide.with(this)
            .load(images)
            .into(img)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }
}