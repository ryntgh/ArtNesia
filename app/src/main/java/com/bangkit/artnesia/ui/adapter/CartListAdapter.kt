package com.bangkit.artnesia.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.R
import com.bangkit.artnesia.data.model.Cart
import com.bangkit.artnesia.ui.activity.CartActivity
import com.bumptech.glide.Glide

class CartListAdapter (
    private val context: Context,
    private val cartList : ArrayList<Cart>,
    private val updateCartItems: Boolean,
    private val activity: CartActivity
): RecyclerView.Adapter<CartListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_cart_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product: Cart = cartList[position]

        Glide.with(context)
            .load(product.image)
            .centerCrop()
            .into(holder.cartImage)

        holder.cartName.text = product.title
        holder.cartPrice.text = "Rp "+product.price
        holder.cartQuantity.text = product.cart_quantity

        if (product.cart_quantity == "0") {
            holder.cartRemove.visibility = View.GONE
            holder.cartAdd.visibility = View.GONE

            if (updateCartItems) {
                holder.cartDelete.visibility = View.VISIBLE
            } else {
                holder.cartDelete.visibility = View.GONE
            }

            holder.cartQuantity.text = "OUT OF STOCK"

            holder.cartQuantity.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.error
                )
            )
        } else {

            if (updateCartItems) {
                holder.cartRemove.visibility = View.VISIBLE
                holder.cartAdd.visibility = View.VISIBLE
                holder.cartDelete.visibility = View.VISIBLE
            } else {

                holder.cartRemove.visibility = View.GONE
                holder.cartAdd.visibility = View.GONE
                holder.cartDelete.visibility = View.GONE
            }

            holder.cartQuantity.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.textAd
                )
            )
        }

        holder.cartRemove.setOnClickListener {

            if (product.cart_quantity == "1") {
                activity.removeItemFromCart(product.id)
            } else {

                val cartQuantity: Int = product.cart_quantity.toInt()
                val itemHashMap = HashMap<String, Any>()
                itemHashMap["cart_quantity"] = (cartQuantity - 1).toString()
                activity.updateMyCart(context, product.id, itemHashMap)
            }
        }

        holder.cartAdd.setOnClickListener {

            val cartQuantity: Int = product.cart_quantity.toInt()
            if (cartQuantity < product.stock_quantity.toInt()) {
                val itemHashMap = HashMap<String, Any>()
                itemHashMap["cart_quantity"] = (cartQuantity + 1).toString()
                activity.updateMyCart(context, product.id, itemHashMap)
            } else {
                if (context is CartActivity) {
                    context.showErrorSnackBar(
                        context.resources.getString(
                            R.string.msg_for_available_stock,
                            product.stock_quantity
                        ),
                        true
                    )
                }
            }
        }

        holder.cartDelete.setOnClickListener {
            activity.removeItemFromCart(product.id)
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cartImage : ImageView = itemView.findViewById(R.id.iv_cart_item_image)
        val cartName : TextView = itemView.findViewById(R.id.tv_cart_item_title)
        val cartPrice : TextView = itemView.findViewById(R.id.tv_cart_item_price)
        val cartQuantity : TextView = itemView.findViewById(R.id.tv_cart_quantity)
        val cartRemove : ImageButton = itemView.findViewById(R.id.ib_remove_cart_item)
        val cartAdd : ImageButton = itemView.findViewById(R.id.ib_add_cart_item)
        val cartDelete : ImageButton = itemView.findViewById(R.id.ib_delete_cart_item)
    }
}