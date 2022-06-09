package com.bangkit.artnesia.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.R
import com.bangkit.artnesia.data.model.Article
import com.bangkit.artnesia.ui.activity.DetailArticleActivity
import com.bumptech.glide.Glide

class ArticleAdapter(
    private val context: Context,
    private val articleList: ArrayList<Article>
) : RecyclerView.Adapter<ArticleAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.article_row, parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article: Article = articleList[position]

        Glide.with(context)
            .load(article.image)
            .into(holder.articleImage)

        holder.articleName.text = article.name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailArticleActivity::class.java)
            intent.putExtra(DetailArticleActivity.EXTRA_ARTICLE_ID, article.article_id)
            context.startActivity(intent)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val articleImage: ImageView = itemView.findViewById(R.id.img_item_article)
        val articleName: TextView = itemView.findViewById(R.id.tv_articleTittle)
    }
}