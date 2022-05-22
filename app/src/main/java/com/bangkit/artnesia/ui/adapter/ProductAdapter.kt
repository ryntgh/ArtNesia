package com.bangkit.artnesia.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.data.model.ProductModel
import com.bangkit.artnesia.databinding.ItemCoverBinding
import com.bangkit.artnesia.databinding.ItemProductBinding
import com.bangkit.artnesia.ui.activity.ProductDetailActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ProductAdapter (val activity: Activity): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var product = arrayListOf<ProductModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return product.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(product[position])
    }

    fun setData(courseItems: List<ProductModel>) {
        product.clear()
        product.addAll(courseItems)
    }

    inner class ViewHolder(private val binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product: ProductModel){
            with(binding){
                Glide.with(itemView.context)
                    .load(product.productImage)
                    .apply(RequestOptions().override(350, 550))
                    .into(binding.productImageItemProduct)

                binding.productRatingItemProduct.rating = product.productRating
                binding.productNameItemProduct.text = product.productName
                binding.productPriceItemProduct.text = "Rp " + product.productPrice

                itemView.setOnClickListener {
                    val i = Intent(activity, ProductDetailActivity::class.java)
                    i.putExtra("NAME_KEY", product.productName)
                    i.putExtra("PRICE_KEY", product.productPrice)
                    i.putExtra("DESC_KEY", product.productDes)
                    i.putExtra("RATING_KEY", product.productRating)
                    i.putExtra("BRAND_KEY", product.productBrand)
                    i.putExtra("IMAGE_KEY", product.productImage)
                    i.putExtra("CATEGORY_KEY", product.productCategory)

                    activity.startActivity(i)
                }
            }
        }
    }
}