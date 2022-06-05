package com.bangkit.artnesia.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.R
import com.bangkit.artnesia.data.model.Product
import com.bangkit.artnesia.data.model.ProductModel
import com.bangkit.artnesia.databinding.ItemCoverBinding
import com.bangkit.artnesia.databinding.ItemProductBinding
import com.bangkit.artnesia.ui.activity.DetailProductActivity
import com.bangkit.artnesia.ui.activity.ProductDetailActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ProductAdapter (
    private val context: Context,
    private val productList : ArrayList<Product>
): RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {

    private val limit = 8

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductAdapter.MyViewHolder, position: Int) {
        val product: Product = productList[position]

        Glide.with(context)
            .load(product.image)
            .centerCrop()
            .into(holder.productImage)

        holder.productName.text = product.title
        holder.productPrice.text = "Rp "+product.price

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailProductActivity::class.java)
            intent.putExtra(DetailProductActivity.EXTRA_PRODUCT_ID, product.product_id)
            intent.putExtra(DetailProductActivity.EXTRA_PRODUCT_OWNER_ID, product.user_id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return if (productList.size > limit) {
            limit
        } else {
            productList.size
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage : ImageView = itemView.findViewById(R.id.productImage_itemProduct)
        val productName : TextView = itemView.findViewById(R.id.productName_itemProduct)
        val productPrice : TextView = itemView.findViewById(R.id.productPrice_itemProduct)
    }
}