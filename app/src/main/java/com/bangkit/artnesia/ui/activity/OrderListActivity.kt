package com.bangkit.artnesia.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.artnesia.data.model.Order
import com.bangkit.artnesia.databinding.ActivityOrderListBinding
import com.bangkit.artnesia.ui.adapter.MyOrderAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OrderListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderListBinding
    private val mFireStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getMyOrdersList()
    }

    private fun getMyOrdersList() {
        mFireStore.collection(ORDERS)
            .whereEqualTo(USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e(this.javaClass.simpleName, document.documents.toString())
                val list: ArrayList<Order> = ArrayList()

                for (i in document.documents) {

                    val orderItem = i.toObject(Order::class.java)!!
                    orderItem.id = i.id

                    list.add(orderItem)
                }

                this.populateOrdersListInUI(list)
            }
            .addOnFailureListener { e ->
                Log.e(this.javaClass.simpleName, "Error while getting the orders list.", e)
            }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun populateOrdersListInUI(ordersList: ArrayList<Order>) {

        if (ordersList.size > 0) {

            binding.rvMyOrderItems.visibility = View.VISIBLE
            binding.tvNoOrdersFound.visibility = View.GONE

            binding.rvMyOrderItems.layoutManager = LinearLayoutManager(this)
            binding.rvMyOrderItems.setHasFixedSize(true)

            val myOrdersAdapter = MyOrderAdapter(this, ordersList)
            binding.rvMyOrderItems.adapter = myOrdersAdapter
        } else {
            binding.rvMyOrderItems.visibility = View.GONE
            binding.tvNoOrdersFound.visibility = View.VISIBLE
        }
    }

    companion object{
        const val ORDERS: String = "orders"
        const val USER_ID: String = "user_id"
    }
}