package com.bangkit.artnesia.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.R
import com.bangkit.artnesia.data.remote.response.LiteratureItem
import com.bangkit.artnesia.databinding.LiteratureRowBinding
import com.bangkit.artnesia.ui.activity.DetectionLiteratureActivity
import com.bangkit.artnesia.ui.utils.DiffCallBack
import com.bumptech.glide.Glide
import java.util.*

class LitAdapter : RecyclerView.Adapter<LitAdapter.ListViewHolder>(), Filterable {
    private var listLiterature = ArrayList<LiteratureItem>()
    var literatureFilterList = ArrayList<LiteratureItem>()

    init {
        literatureFilterList = listLiterature
    }

    inner class ListViewHolder(binding: LiteratureRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var ivLiterature = binding.imgItemLiterature
        var tvName = binding.tvLiteratureName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val literatureRowBinding =
            LiteratureRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(literatureRowBinding)
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = literatureFilterList[position]

        holder.apply {
            tvName.text = data.name
            Glide.with(itemView.context)
                .load(data.image)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(ivLiterature)

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

    override fun getItemCount(): Int = literatureFilterList.size

    fun setData(litData: ArrayList<LiteratureItem>) {
        val diffUtilCallback = DiffCallBack(literatureFilterList, litData)

        if (this.listLiterature == null) {
            this.listLiterature = litData
            this.literatureFilterList = litData
            notifyItemChanged(0, literatureFilterList.size)
        } else {
            val result: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffUtilCallback)
            this.listLiterature = litData
            this.literatureFilterList = litData
            result.dispatchUpdatesTo(this)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    literatureFilterList = listLiterature
                } else {
                    val filteredList: ArrayList<LiteratureItem> =
                        ArrayList<LiteratureItem>()
                    for (result in listLiterature) {
                        if (result.name?.lowercase(Locale.getDefault())
                                ?.contains(charString.lowercase(Locale.getDefault())) == true
                        ) {
                            filteredList.add(result)
                        }
                    }
                    literatureFilterList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = literatureFilterList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                literatureFilterList = results?.values as ArrayList<LiteratureItem>

                notifyDataSetChanged()
            }
        }
    }

    companion object {
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_ORIGIN = "extra_origin"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_CLASS = "extra_class"
    }
}