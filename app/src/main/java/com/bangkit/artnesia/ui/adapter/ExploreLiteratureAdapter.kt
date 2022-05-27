package com.bangkit.artnesia.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.data.model.LiteratureModel
import com.bangkit.artnesia.databinding.LiteratureRowBinding
import com.bangkit.artnesia.ui.activity.DetailLiteratureActivity
import com.bumptech.glide.Glide

class ExploreLiteratureAdapter(val activity: Activity) :
    RecyclerView.Adapter<ExploreLiteratureAdapter.ViewHolder>() {

    private var listLiterature = arrayListOf<LiteratureModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LiteratureRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listLiterature.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listLiterature[position])
    }

    fun setData(courseItems: List<LiteratureModel>) {
        listLiterature.clear()
        listLiterature.addAll(courseItems)
    }

    inner class ViewHolder(private val binding: LiteratureRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(literature: LiteratureModel) {
            Glide.with(itemView.context)
                .load(literature.image)
                .into(binding.imgItemLiterature)

            binding.tvLiteratureName.text = literature.name

            itemView.setOnClickListener {
                val i = Intent(activity, DetailLiteratureActivity::class.java)
                i.putExtra("IMAGE_KEY", literature.image)
                i.putExtra("NAME_KEY", literature.name)
                i.putExtra("ORIGIN_KEY", literature.origin)
                i.putExtra("SUMMARY_KEY", literature.description)
                activity.startActivity(i)
            }
        }
    }
}