package com.bangkit.artnesia.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.artnesia.R
import com.bangkit.artnesia.data.model.Order
import com.bangkit.artnesia.databinding.ActivityMyOrderBinding
import com.bangkit.artnesia.ui.adapter.CartListAdapter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MyOrderActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMyOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var myOrderDetails: Order = Order()

        if (intent.hasExtra(EXTRA_MY_ORDER_DETAILS)) {
            myOrderDetails =
                intent.getParcelableExtra<Order>(EXTRA_MY_ORDER_DETAILS)!!
        }

        setupUI(myOrderDetails)
    }

    private fun setupUI(orderDetails: Order) {

        binding.tvOrderDetailsId.text = orderDetails.title

        // Date Format in which the date will be displayed in the UI.
        val dateFormat = "dd MMM yyyy HH:mm"
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = orderDetails.order_datetime

        val orderDateTime = formatter.format(calendar.time)
        binding.tvOrderDetailsDate.text = orderDateTime

        // Get the difference between the order date time and current date time in hours.
        // If the difference in hours is 1 or less then the order status will be PENDING.
        // If the difference in hours is 2 or greater then 1 then the order status will be PROCESSING.
        // And, if the difference in hours is 3 or greater then the order status will be DELIVERED.

        val diffInMilliSeconds: Long = System.currentTimeMillis() - orderDetails.order_datetime
        val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(diffInMilliSeconds)
        Log.e("Difference in Hours", "$diffInHours")

        when {
            diffInHours < 1 -> {
                binding.tvOrderStatus.text = "Pending"
                binding.tvOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.primary_light
                    )
                )
            }
            diffInHours < 2 -> {
                binding.tvOrderStatus.text = "In Process"
                binding.tvOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.primary
                    )
                )
            }
            else -> {
                binding.tvOrderStatus.text = "Delivered"
                binding.tvOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.primary_dark
                    )
                )
            }
        }

        binding.rvMyOrderItemsList.layoutManager = LinearLayoutManager(this)
        binding.rvMyOrderItemsList.setHasFixedSize(true)

        val cartListAdapter =
            CartListAdapter(this, orderDetails.items, false, CartActivity())
        binding.rvMyOrderItemsList.adapter = cartListAdapter

        binding.tvMyOrderDetailsAddressType.text = orderDetails.address.type
        binding.tvMyOrderDetailsFullName.text = orderDetails.address.name
        binding.tvMyOrderDetailsAddress.text =
            "${orderDetails.address.address}, ${orderDetails.address.zipCode}"
        binding.tvMyOrderDetailsAdditionalNote.text = orderDetails.address.additionalNote

        if (orderDetails.address.otherDetails.isNotEmpty()) {
            binding.tvMyOrderDetailsOtherDetails.visibility = View.VISIBLE
            binding.tvMyOrderDetailsOtherDetails.text = orderDetails.address.otherDetails
        } else {
            binding.tvMyOrderDetailsOtherDetails.visibility = View.GONE
        }
        binding.tvMyOrderDetailsMobileNumber.text = orderDetails.address.mobileNumber

        binding.tvOrderDetailsSubTotal.text = orderDetails.sub_total_amount
        binding.tvOrderDetailsShippingCharge.text = orderDetails.shipping_charge
        binding.tvOrderDetailsTotalAmount.text = orderDetails.total_amount
    }

    companion object{
        const val EXTRA_MY_ORDER_DETAILS: String = "extra_my_order_details"
    }
}