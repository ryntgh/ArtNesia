package com.bangkit.artnesia.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.R
import com.bangkit.artnesia.data.model.Product
import com.bangkit.artnesia.ui.activity.MyProductActivity
import com.bumptech.glide.Glide

class MyProductListAdapter (
    private val context: Context,
    private val productList : ArrayList<Product>,
    private val activity: MyProductActivity
): RecyclerView.Adapter<MyProductListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyProductListAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_my_product, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyProductListAdapter.MyViewHolder, position: Int) {
        val product: Product = productList[position]

        Glide.with(context)
            .load(product.image)
            .centerCrop()
            .into(holder.productImage)

        holder.buttonDelete.setOnClickListener {
            activity.deleteProduct(product.product_id)
        }

        holder.productName.text = product.title
        holder.productPrice.text = "Rp "+product.price
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage : ImageView = itemView.findViewById(R.id.iv_item_image)
        val productName : TextView = itemView.findViewById(R.id.tv_item_name)
        val productPrice : TextView = itemView.findViewById(R.id.tv_item_price)
        val buttonDelete : ImageButton = itemView.findViewById(R.id.ib_delete_product)
    }
}