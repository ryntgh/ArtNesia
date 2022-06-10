package com.bangkit.artnesia.ui.adapter

import android.annotation.SuppressLint
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
import com.bangkit.artnesia.data.model.Address
import com.bangkit.artnesia.ui.activity.AddAddressActivity
import com.bangkit.artnesia.ui.activity.AddressListActivity
import com.bangkit.artnesia.ui.activity.CheckoutActivity
import com.bangkit.artnesia.ui.activity.CheckoutActivity.Companion.EXTRA_SELECTED_ADDRESS

class AddressListAdapter (
    private val context: Context,
    private val addressList : ArrayList<Address>,
    private val selectAddress: Boolean,
    private val activity: AddressListActivity
): RecyclerView.Adapter<AddressListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)

        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val address: Address = addressList[position]

        holder.addressName.text = address.name
        holder.addressType.text = address.type
        holder.addressDetail.text = "${address.address}, ${address.zipCode}"
        holder.addressPhone.text = address.mobileNumber

        if (selectAddress) {
            holder.deleteButton.visibility = View.GONE
            holder.editButton.visibility = View.GONE

            holder.itemView.setOnClickListener {
                val intent = Intent(context, CheckoutActivity::class.java)
                intent.putExtra(EXTRA_SELECTED_ADDRESS, address)
                context.startActivity(intent)
            }
        }else{
            holder.deleteButton.visibility = View.VISIBLE
            holder.editButton.visibility = View.VISIBLE

            holder.deleteButton.setOnClickListener {
                activity.deleteAddress(address.id)
            }

            holder.editButton.setOnClickListener {
                notifyEditItem(activity, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    private fun notifyEditItem(activity: Activity, position: Int) {
        val intent = Intent(context, AddAddressActivity::class.java)
        intent.putExtra(AddAddressActivity.EXTRA_ADDRESS_DETAILS, addressList[position])
        activity.startActivityForResult(intent, AddressListActivity.ADD_ADDRESS_REQUEST_CODE)
        notifyItemChanged(position)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val addressName : TextView = itemView.findViewById(R.id.tv_address_full_name)
        val addressType : TextView = itemView.findViewById(R.id.tv_address_type)
        val addressDetail : TextView = itemView.findViewById(R.id.tv_address_details)
        val addressPhone : TextView = itemView.findViewById(R.id.tv_address_mobile_number)
        val deleteButton : ImageView = itemView.findViewById(R.id.iv_address_delete)
        val editButton : ImageView = itemView.findViewById(R.id.iv_address_edit)
    }
}