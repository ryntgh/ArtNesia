package com.bangkit.artnesia.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.R
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

        //binding.addToCartProductDetailsPage.setOnClickListener(this)

        getProductDetailsFirestore(mProductId)

        //getRecommendProduct()
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
                    //addToCart()
                }

            }
        }
    }

    /*
    private fun addToCart() {

        val addToCart = Cart(
            FirestoreClass().getCurrentUserID(),
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )

        // Show the progress dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().addCartItems(this@ProductDetailsActivity, addToCart)
    }
     */

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
        /*
        else{

            // There is no need to check the cart list if the product owner himself is seeing the product details.
            if (getCurrentUserID() == product.user_id) {
                // Hide Progress dialog.
                hideProgressDialog()
            } else {
                FirestoreClass().checkIfItemExistInCart(this@ProductDetailsActivity, mProductId)
            }
        }
         */
    }

    private fun loadProductPicture(image: Any, imageView: ImageView) {
        try {
            // Load the user image in the ImageView.
            Glide
                .with(this)
                .load(image) // Uri or URL of the image
                .centerCrop() // Scale type of the image.
                .into(imageView) // the view in which the image will be loaded.
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /*
    fun productExistsInCart() {

        // Hide the progress dialog.
        hideProgressDialog()

        // Hide the AddToCart button if the item is already in the cart.
        btn_add_to_cart.visibility = View.GONE
        // Show the GoToCart button if the item is already in the cart. User can update the quantity from the cart list screen if he wants.
        btn_go_to_cart.visibility = View.VISIBLE
    }

     */

    /*
    fun addToCartSuccess() {
        Toast.makeText(
            this@ProductDetailsActivity,
            resources.getString(R.string.success_message_item_added_to_cart),
            Toast.LENGTH_SHORT
        ).show()

        // Hide the AddToCart button if the item is already in the cart.
        btn_add_to_cart.visibility = View.GONE
        // Show the GoToCart button if the item is already in the cart. User can update the quantity from the cart list screen if he wants.
        btn_go_to_cart.visibility = View.VISIBLE
    }
     */

    /*
    fun checkIfItemExistInCart(activity: ProductDetailsActivity, productId: String) {

        mFireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .whereEqualTo(Constants.PRODUCT_ID, productId)
            .get()
            .addOnSuccessListener { document ->

                Log.e(activity.javaClass.simpleName, document.documents.toString())

                // If the document size is greater than 1 it means the product is already added to the cart.
                if (document.documents.size > 0) {
                    activity.productExistsInCart()
                } else {
                    activity.hideProgressDialog()
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is an error.
                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while checking the existing cart list.",
                    e
                )
            }
    }
     */

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
    }
}