package com.bangkit.artnesia.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.artnesia.data.model.SoldProduct
import com.bangkit.artnesia.databinding.ActivitySoldProductBinding
import com.bangkit.artnesia.ui.adapter.SoldProductAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SoldProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySoldProductBinding
    private val mFireStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoldProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSoldProductList()
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    private fun getSoldProductList() {
        mFireStore.collection("sold_product")
            .whereEqualTo("seller_id", getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e(this.javaClass.simpleName, document.documents.toString())
                val list: ArrayList<SoldProduct> = ArrayList()
                for (i in document.documents) {
                    val soldProduct = i.toObject(SoldProduct::class.java)!!
                    soldProduct.id = i.id
                    list.add(soldProduct)
                }
                successSoldProductsList(list)
            }
            .addOnFailureListener { e ->
                Log.e(this.javaClass.simpleName, "Error while getting the list of sold products.", e)
            }
    }

    private fun successSoldProductsList(soldProductsList: ArrayList<SoldProduct>) {

        if (soldProductsList.size > 0) {
            binding.rvSoldProductList.visibility = View.VISIBLE
            binding.tvNoSoldProductFound.visibility = View.GONE

            binding.rvSoldProductList.layoutManager = LinearLayoutManager(this)
            binding.rvSoldProductList.setHasFixedSize(true)

            val soldProductsListAdapter =
                SoldProductAdapter(this, soldProductsList)
            binding.rvSoldProductList.adapter = soldProductsListAdapter

            var subTotal: Double = 0.0

            for (item in soldProductsList) {

                val price = item.price.toDouble()
                val quantity = item.sold_quantity.toInt()

                subTotal += (price * quantity)
            }

            //ada pajak 5 persen dari subtotal
            val tax: Double = subTotal * 0.05

            binding.tvSubTotal.text = "Rp "+subTotal
            binding.tvTax.text = "Rp "+tax.toString()

            if (subTotal > 0) {
                binding.llIncome.visibility = View.VISIBLE

                val total = subTotal - tax
                binding.tvTotalAmount.text = "Rp "+total
            } else {
                binding.llIncome.visibility = View.GONE
            }
        } else {
            binding.rvSoldProductList.visibility = View.GONE
            binding.tvNoSoldProductFound.visibility = View.VISIBLE
        }
    }
}