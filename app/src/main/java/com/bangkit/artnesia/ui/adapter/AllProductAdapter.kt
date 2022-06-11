package com.bangkit.artnesia.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.R
import com.bangkit.artnesia.data.model.Product
import com.bangkit.artnesia.ui.activity.DetailProductActivity
import com.bumptech.glide.Glide

class AllProductAdapter(
    private val context: Context,
    private var list: ArrayList<Product>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_my_product,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            Glide.with(context)
                .load(model.image)
                .centerCrop()
                .into(holder.productImage)

            holder.productName.text = model.title
            holder.productPrice.text = "Rp "+model.price

            holder.buttonDelete.visibility = View.GONE

            holder.itemView.setOnClickListener {
                val intent = Intent(context, DetailProductActivity::class.java)
                intent.putExtra(DetailProductActivity.EXTRA_PRODUCT_ID, model.product_id)
                intent.putExtra(DetailProductActivity.EXTRA_PRODUCT_OWNER_ID, model.user_id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage : ImageView = itemView.findViewById(R.id.iv_item_image)
        val productName : TextView = itemView.findViewById(R.id.tv_item_name)
        val productPrice : TextView = itemView.findViewById(R.id.tv_item_price)
        val buttonDelete : ImageButton = itemView.findViewById(R.id.ib_delete_product)
    }
}