package com.bangkit.artnesia.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.data.model.CategoryModel
import com.bangkit.artnesia.data.model.LiteratureModel
import com.bangkit.artnesia.databinding.CategoryRowBinding
import com.bangkit.artnesia.databinding.LiteratureRowBinding
import com.bangkit.artnesia.ui.activity.DetailLiteratureActivity
import com.bumptech.glide.Glide

class CategoryAdapter(val activity: Activity) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var listCategory = arrayListOf<CategoryModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listCategory[position])
    }

    fun setData(courseItems: List<CategoryModel>) {
        listCategory.clear()
        listCategory.addAll(courseItems)
    }

    inner class ViewHolder(private val binding: CategoryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryModel) {

            binding.tvCategoryDisplay.text = category.categoryName

        }
    }
}