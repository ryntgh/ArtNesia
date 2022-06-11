package com.bangkit.artnesia.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.artnesia.data.model.Product
import com.bangkit.artnesia.databinding.ActivitySearchProductBinding
import com.bangkit.artnesia.ui.adapter.AllProductAdapter
import com.google.firebase.firestore.FirebaseFirestore

class SearchProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchProductBinding
    private val mFireStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        getProductList()
    }

    private fun getProductList() {
        mFireStore.collection("products")
            .get()
            .addOnSuccessListener { document ->
                Log.e(this.javaClass.simpleName, document.documents.toString())
                val list: ArrayList<Product> = ArrayList()
                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)!!
                    product.product_id = i.id
                    list.add(product)
                }
                successSoldProductsList(list)
            }
            .addOnFailureListener { e ->
                Log.e(this.javaClass.simpleName, "Error while getting the list of sold products.", e)
            }
    }

    private fun searchProductList(search:String) {
        mFireStore.collection("products")
            .whereArrayContains("title",search)
            .get()
            .addOnSuccessListener { document ->
                Log.e(this.javaClass.simpleName, document.documents.toString())
                val list: ArrayList<Product> = ArrayList()
                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)!!
                    product.product_id = i.id
                    list.add(product)
                }
                successSoldProductsList(list)
            }
            .addOnFailureListener { e ->
                Log.e(this.javaClass.simpleName, "Error while getting the list of sold products.", e)
            }
    }

    private fun successSoldProductsList(productList: ArrayList<Product>) {

        if (productList.size > 0) {
            binding.rvProductList.visibility = View.VISIBLE
            binding.tvNoSearchProductFound.visibility = View.GONE

            binding.rvProductList.layoutManager = LinearLayoutManager(this)
            binding.rvProductList.setHasFixedSize(true)

            val soldProductsListAdapter =
                AllProductAdapter(this, productList)
            binding.rvProductList.adapter = soldProductsListAdapter
        } else {
            binding.rvProductList.visibility = View.GONE
            binding.tvNoSearchProductFound.visibility = View.VISIBLE
        }
    }
}