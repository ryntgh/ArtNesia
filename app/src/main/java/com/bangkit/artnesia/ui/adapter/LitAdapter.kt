package com.bangkit.artnesia.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.R
import com.bangkit.artnesia.data.remote.response.LiteratureItem
import com.bangkit.artnesia.databinding.LiteratureRowBinding
import com.bangkit.artnesia.ui.activity.DetectionLiteratureActivity
import com.bangkit.artnesia.ui.utils.DiffCallBack
import com.bumptech.glide.Glide

class LitAdapter : RecyclerView.Adapter<LitAdapter.ListViewHolder>() {
    private var listStory = ArrayList<LiteratureItem>()

    inner class ListViewHolder(binding: LiteratureRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var ivStory = binding.imgItemLiterature
        var tvName = binding.tvLiteratureName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemRowStoryBinding =
            LiteratureRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemRowStoryBinding)
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listStory[position]

        holder.apply {
            tvName.text = data.name
            Glide.with(itemView.context)
                .load(data.image)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(ivStory)

            holder.itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetectionLiteratureActivity::class.java)
                intent.putExtra(EXTRA_IMAGE, data.image)
                intent.putExtra(EXTRA_ORIGIN, data.origin)
                intent.putExtra(EXTRA_NAME, data.name)
                intent.putExtra(EXTRA_DESC, data.description)
                intent.putExtra(EXTRA_CLASS, data.classification)

                itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = listStory.size

    fun setData(newStory: ArrayList<LiteratureItem>) {
        val diffUtilCallback = DiffCallBack(listStory, newStory)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        listStory = newStory
        diffResult.dispatchUpdatesTo(this)
    }

    companion object {
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_ORIGIN = "extra_origin"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_CLASS = "extra_class"
    }
}