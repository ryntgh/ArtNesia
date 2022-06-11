package com.bangkit.artnesia.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.artnesia.data.model.*
import com.bangkit.artnesia.databinding.ActivityCheckoutBinding
import com.bangkit.artnesia.ui.adapter.CartListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private val mFireStore = FirebaseFirestore.getInstance()
    private var mAddressDetails: Address? = null
    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartItemsList: ArrayList<Cart>
    private var mSubTotal: Double = 0.0
    private var mTotalAmount: Double = 0.0
    private lateinit var mOrderDetails: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(EXTRA_SELECTED_ADDRESS)) {
            mAddressDetails =
                intent.getParcelableExtra<Address>(EXTRA_SELECTED_ADDRESS)!!
        }

        if (mAddressDetails != null) {
            binding.tvCheckoutAddressType.text = mAddressDetails?.type
            binding.tvCheckoutFullName.text = mAddressDetails?.name
            binding.tvCheckoutAddress.text = "${mAddressDetails!!.address}, ${mAddressDetails!!.zipCode}"
            binding.tvCheckoutAdditionalNote.text = mAddressDetails?.additionalNote

            if (mAddressDetails?.otherDetails!!.isNotEmpty()) {
                binding.tvCheckoutOtherDetails.text = mAddressDetails?.otherDetails
            }
            binding.tvCheckoutMobileNumber.text = mAddressDetails?.mobileNumber
        }

        binding.btnPlaceOrder.setOnClickListener {
            placeAnOrder()
        }

        getAllProductsList()
    }

    private fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        mProductsList = productsList

        getCartList()
    }

    private fun getCartList() {
        mFireStore.collection(CART_ITEMS)
            .whereEqualTo(USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e(this.javaClass.simpleName, document.documents.toString())
                val list: ArrayList<Cart> = ArrayList()

                for (i in document.documents) {
                    val cartItem = i.toObject(Cart::class.java)!!
                    cartItem.id = i.id
                    list.add(cartItem)
                }
                successCartItemsList(list)
            }
            .addOnFailureListener { e ->
                Log.e(this.javaClass.simpleName, "Error while getting the cart list items.", e)
            }
    }

    private fun successCartItemsList(cartList: ArrayList<Cart>) {
        for (product in mProductsList) {
            for (cart in cartList) {
                if (product.product_id == cart.product_id) {
                    cart.stock_quantity = product.stock_quantity
                }
            }
        }

        mCartItemsList = cartList

        binding.rvCartListItems.layoutManager = LinearLayoutManager(this@CheckoutActivity)
        binding.rvCartListItems.setHasFixedSize(true)

        val cartListAdapter = CartListAdapter(this@CheckoutActivity, mCartItemsList, false, CartActivity())
        binding.rvCartListItems.adapter = cartListAdapter

        for (item in mCartItemsList) {

            val availableQuantity = item.stock_quantity.toInt()

            if (availableQuantity > 0) {
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()

                mSubTotal += (price * quantity)
            }
        }

        binding.tvCheckoutSubTotal.text = "Rp $mSubTotal"
        binding.tvCheckoutShippingCharge.text = "Rp10000"

        if (mSubTotal > 0) {
            binding.llCheckoutPlaceOrder.visibility = View.VISIBLE

            mTotalAmount = mSubTotal + 10000.0
            binding.tvCheckoutTotalAmount.text = "Rp $mTotalAmount"
        } else {
            binding.llCheckoutPlaceOrder.visibility = View.GONE
        }
    }

    private fun placeAnOrder() {
        mOrderDetails = Order(
            getCurrentUserID(),
            mCartItemsList,
            mAddressDetails!!,
            "My order ${System.currentTimeMillis()}",
            mCartItemsList[0].image,
            mSubTotal.toString(),
            "10000.0",
            mTotalAmount.toString(),
            System.currentTimeMillis()
        )

        placeOrder(mOrderDetails)
    }

    private fun placeOrder(order: Order) {

        mFireStore.collection(ORDERS)
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                orderPlacedSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    this.javaClass.simpleName,
                    "Error while placing an order.",
                    e
                )
            }
    }


    private fun orderPlacedSuccess() {
        updateAllDetails(mCartItemsList, mOrderDetails)
    }

    private fun updateAllDetails(cartList: ArrayList<Cart>, order: Order) {

        val writeBatch = mFireStore.batch()

        // Prepare the History details
        for (cart in cartList) {
            val history = History(
                getCurrentUserID(),
                cart.title,
                cart.price,
                cart.cart_quantity,
                cart.image,
                order.title,
                order.order_datetime,
                order.sub_total_amount,
                order.shipping_charge,
                order.total_amount,
                order.address
            )

            val documentReference = mFireStore.collection("history")
                .document()
            writeBatch.set(documentReference, history)
        }

        for (cart in cartList) {
            val soldProduct = SoldProduct(
                getCurrentUserID(),
                cart.title,
                cart.price,
                cart.cart_quantity,
                cart.image,
                cart.seller_id,
                order.title,
                order.order_datetime,
                order.sub_total_amount,
                order.shipping_charge,
                order.total_amount,
                order.address
            )

            val documentReference = mFireStore.collection("sold_product")
                .document()
            writeBatch.set(documentReference, soldProduct)
        }

        // Update the product stock in the products collection based to cart quantity.
        for (cart in cartList) {

            val productHashMap = HashMap<String, Any>()

            productHashMap[STOCK_QUANTITY] =
                (cart.stock_quantity.toInt() - cart.cart_quantity.toInt()).toString()

            val documentReference = mFireStore.collection(PRODUCTS)
                .document(cart.product_id)

            writeBatch.update(documentReference, productHashMap)
        }

        // Delete the list of cart items
        for (cart in cartList) {

            val documentReference = mFireStore.collection(CART_ITEMS)
                .document(cart.id)
            writeBatch.delete(documentReference)
        }

        writeBatch.commit().addOnSuccessListener {
            allDetailsUpdatedSuccessfully()
        }.addOnFailureListener { e ->
            Log.e(
                this.javaClass.simpleName,
                "Error while updating all the details after order placed.",
                e
            )
        }
    }

    private fun allDetailsUpdatedSuccessfully() {

        Toast.makeText(this@CheckoutActivity, "Your order placed successfully.", Toast.LENGTH_SHORT)
            .show()

        val intent = Intent(this@CheckoutActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun getAllProductsList() {
        mFireStore.collection(PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List", document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()
                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id
                    productsList.add(product)
                }
                successProductsListFromFireStore(productsList)
            }
            .addOnFailureListener { e ->
                Log.e("Get Product List", "Error while getting all product list.", e)
            }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    companion object{
        const val EXTRA_SELECTED_ADDRESS: String = "extra_selected_address"
        const val PRODUCTS: String = "products"
        const val CART_ITEMS: String = "cart_items"
        const val ORDERS: String = "orders"
        const val USER_ID: String = "user_id"
        const val STOCK_QUANTITY: String = "stock_quantity"
    }
}