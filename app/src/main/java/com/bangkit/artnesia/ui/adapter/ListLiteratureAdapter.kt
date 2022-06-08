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
import com.bangkit.artnesia.data.model.Literature
import com.bangkit.artnesia.ui.activity.DetailLiteratureActivity
import com.bumptech.glide.Glide

class ListLiteratureAdapter(
    private val context: Context,
    private val literatureList: ArrayList<Literature>
) : RecyclerView.Adapter<ListLiteratureAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.literature_row, parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return literatureList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val literature: Literature = literatureList[position]

        Glide.with(context)
            .load(literature.image)
            .centerCrop()
            .into(holder.literatureImage)

        holder.literatureName.text = literature.name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailLiteratureActivity::class.java)
            intent.putExtra(DetailLiteratureActivity.EXTRA_LITERATURE_ID, literature.literature_id)
            context.startActivity(intent)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val literatureImage: ImageView = itemView.findViewById(R.id.img_item_literature)
        val literatureName: TextView = itemView.findViewById(R.id.tv_literatureName)
    }
}