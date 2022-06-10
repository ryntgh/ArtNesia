package com.bangkit.artnesia.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.R
import com.bangkit.artnesia.data.model.Cart
import com.bangkit.artnesia.data.model.Product
import com.bangkit.artnesia.databinding.ActivityDetailProductBinding
import com.bangkit.artnesia.ui.adapter.ProductAdapter
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import java.io.IOException

class DetailProductActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailProductBinding
    private lateinit var mProductDetails: Product

    private var mProductId: String = ""
    private val mFireStore = FirebaseFirestore.getInstance()

    private lateinit var recProductAdapter: ProductAdapter
    private lateinit var recProductList: ArrayList<Product>
    private lateinit var recProductRV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        if (intent.hasExtra(EXTRA_PRODUCT_ID)) {
            mProductId =
                intent.getStringExtra(EXTRA_PRODUCT_ID)!!
        }

        var productOwnerId: String = ""

        if (intent.hasExtra(EXTRA_PRODUCT_OWNER_ID)) {
            productOwnerId =
                intent.getStringExtra(EXTRA_PRODUCT_OWNER_ID)!!
        }

        if (getCurrentUserID() == productOwnerId) {
            binding.linearLayout7.visibility = View.GONE
        } else {
            binding.linearLayout7.visibility = View.VISIBLE
        }

        binding.addToCartProductDetailsPage.setOnClickListener(this)
        binding.goToCartProductDetailsPage.setOnClickListener(this)

        getProductDetailsFirestore(mProductId)

        if (intent.hasExtra(IS_MYPRODUCT)) {
            binding.recLayout.visibility = View.GONE
        }else{
            binding.recLayout.visibility = View.VISIBLE
            getRecommendProduct()
        }
    }

    private fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }


    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.addToCart_ProductDetailsPage -> {
                    addToCart()
                }

                R.id.goToCart_ProductDetailsPage -> {
                    startActivity(Intent(this, CartActivity::class.java))
                }

            }
        }
    }

    private fun addToCart() {

        val addToCart = Cart(
            getCurrentUserID(),
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            DEFAULT_CART_QUANTITY,
            "",
            mProductDetails.user_id

        )

        addCartItems(addToCart)
    }

    private fun addCartItems(addToCart: Cart) {

        mFireStore.collection("cart_items")
            .document()
            .set(addToCart, SetOptions.merge())
            .addOnSuccessListener {
                this.addToCartSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    this.javaClass.simpleName,
                    "Error while creating the document for cart item.",
                    e
                )
            }
    }

    private fun getProductDetailsFirestore(productId: String) {
        mFireStore.collection(PRODUCTS)
            .document(productId)
            .get()
            .addOnSuccessListener { document ->
                Log.e(this.javaClass.simpleName, document.toString())
                val product = document.toObject(Product::class.java)!!
                this.productDetailsSuccess(product)
            }
            .addOnFailureListener { e ->
                Log.e(this.javaClass.simpleName, "Error while getting the product details.", e)
            }
    }

    private fun productDetailsSuccess(product: Product) {

        mProductDetails = product

        loadProductPicture(product.image, binding.productImageProductDetailsPage)

        binding.productNameProductDetailsPage.text = product.title
        binding.productPriceProductDetailsPage.text = "Rp "+product.price
        binding.productDesProductDetailsPage.text = product.description
        binding.productBrandProductDetailsPage.text = product.user_name
        binding.productStockProductDetailsPage.text = product.stock_quantity


        if(product.stock_quantity.toInt() == 0){
            binding.linearLayout7.visibility = View.GONE

            binding.productStockProductDetailsPage.text = "Out Of Stock"

            binding.productStockProductDetailsPage.setTextColor(
                ContextCompat.getColor(
                    this@DetailProductActivity,
                    R.color.error
                )
            )
        }
        else{
            if (getCurrentUserID() != product.user_id) {
                checkIfItemExistInCart(mProductId)
            }
        }

    }

    private fun loadProductPicture(image: Any, imageView: ImageView) {
        try {
            Glide
                .with(this)
                .load(image)
                .centerCrop()
                .into(imageView)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    private fun productExistsInCart() {
        binding.addToCartProductDetailsPage.visibility = View.GONE
        binding.goToCartProductDetailsPage.visibility = View.VISIBLE
    }

    private fun addToCartSuccess() {
        Toast.makeText(
            this,
            "Product is added to your bag",
            Toast.LENGTH_SHORT
        ).show()

        binding.addToCartProductDetailsPage.visibility = View.GONE
        binding.goToCartProductDetailsPage.visibility = View.VISIBLE
    }

    private fun checkIfItemExistInCart(productId: String) {

        mFireStore.collection("cart_items")
            .whereEqualTo(USER_ID, getCurrentUserID())
            .whereEqualTo(PRODUCT_ID, productId)
            .get()
            .addOnSuccessListener { document ->

                Log.e(this.javaClass.simpleName, document.documents.toString())

                if (document.documents.size > 0) {
                    this.productExistsInCart()
                }
            }
            .addOnFailureListener { e ->
                Log.e(
                    this.javaClass.simpleName,
                    "Error while checking the existing cart list.",
                    e
                )
            }
    }

    private fun getRecommendProduct(){
        recProductRV = binding.RecomRecViewProductDetailsPage
        recProductRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recProductRV.setHasFixedSize(true)

        recProductList = arrayListOf()

        recProductAdapter  = ProductAdapter(this , recProductList)

        recProductRV.adapter = recProductAdapter

        getProductData(recProductList, recProductAdapter)
    }

    private fun getProductData(productlist : ArrayList<Product>, productAdapt: ProductAdapter) {
        mFireStore.collection("products")
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

                            productlist.add(product)
                        }
                    }
                    //randomize list
                    productlist.shuffle()

                    productAdapt.notifyDataSetChanged()
                }
            })
    }

    companion object {
        const val EXTRA_PRODUCT_ID: String = "extra_product_id"
        const val EXTRA_PRODUCT_OWNER_ID: String = "extra_product_owner_id"
        const val IS_MYPRODUCT: String = "is_myproduct"
        const val PRODUCTS: String = "products"
        const val DEFAULT_CART_QUANTITY: String = "1"
        const val USER_ID: String = "user_id"
        const val PRODUCT_ID: String = "product_id"
    }
}