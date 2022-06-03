package com.bangkit.artnesia.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.R
import com.bangkit.artnesia.data.model.Product
import com.bangkit.artnesia.databinding.ActivityMyProductBinding
import com.bangkit.artnesia.ui.adapter.MyProductListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class MyProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyProductBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var productArrayList: ArrayList<Product>
    private lateinit var myProductAdapter: MyProductListAdapter
    private val mFireStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getListProduct()
    }

    private fun getListProduct(){
        recyclerView = findViewById(R.id.rv_my_product_items)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        productArrayList = arrayListOf()

        myProductAdapter  = MyProductListAdapter(this@MyProductActivity , productArrayList, this)

        recyclerView.adapter = myProductAdapter

        getMyProductData()
    }

    private fun getMyProductData() {
        mFireStore.collection("products")
            .whereEqualTo("user_id", getCurrentUserID())
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error!=null){
                        Log.e("Firestore error", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            //productArrayList.add(dc.document.toObject(Product::class.java))
                            val product = dc.document.toObject(Product::class.java)
                            product.product_id = dc.document.id

                            productArrayList.add(product)
                        }
                    }

                    myProductAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun deleteProduct(productID: String) {
        showAlertDialogToDeleteProduct(productID)
    }

    private fun productDeleteSuccess() {

        Toast.makeText(
            this@MyProductActivity,
            "Successfully delete product",
            Toast.LENGTH_SHORT
        ).show()

        getListProduct()
    }

    private fun showAlertDialogToDeleteProduct(productID: String) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete")
        builder.setMessage("Are you sure to delete this product?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            deleteProductFromFirestore(productID)
            dialogInterface.dismiss()
        }

        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun deleteProductFromFirestore(productId: String) {

        mFireStore.collection("products")
            .document(productId)
            .delete()
            .addOnSuccessListener {
                productDeleteSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    this.javaClass.simpleName,
                    "Error while deleting the product.",
                    e
                )
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_product_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId) {
            R.id.action_add_product -> {
                startActivity(Intent(this@MyProductActivity, AddProductActivity::class.java))
                true
            }
            else -> {return super.onOptionsItemSelected(item)}
        }
    }
}