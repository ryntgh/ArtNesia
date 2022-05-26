package com.bangkit.artnesia.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.ui.activity.DetailLiteratureActivity
import com.bangkit.artnesia.data.model.ArticleModel
import com.bangkit.artnesia.databinding.ArticleRowBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ArticleAdapter (val activity: Activity): RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    private var article = arrayListOf<ArticleModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ArticleRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return article.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(article[position])
    }

    fun setData(courseItems: List<ArticleModel>) {
        article.clear()
        article.addAll(courseItems)
    }

    inner class ViewHolder(private val binding: ArticleRowBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(article: ArticleModel){
            with(binding){
                Glide.with(itemView.context)
                    .load(article.image)
                    .apply(RequestOptions().override(350, 550))
                    .into(binding.imgItemArticle)

                binding.tvArticleTittle.text = article.name
                binding.tvArticleDescription.text = article.description

                itemView.setOnClickListener {
                    val i = Intent(activity, DetailLiteratureActivity::class.java)
                    i.putExtra("IMAGE_KEY", article.image)
                    i.putExtra("NAME_KEY", article.name)
                    i.putExtra("SUMMARY_KEY", article.description)
                    activity.startActivity(i)
                }
            }
        }
    }
}