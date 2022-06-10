package com.bangkit.artnesia.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.artnesia.data.model.Address
import com.bangkit.artnesia.databinding.ActivityAddressListBinding
import com.bangkit.artnesia.ui.adapter.AddressListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddressListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressListBinding
    private var mSelectAddress: Boolean = false
    private val mFireStore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(EXTRA_SELECT_ADDRESS)) {
            mSelectAddress =
                intent.getBooleanExtra(EXTRA_SELECT_ADDRESS, false)
        }

        binding.tvAddAddress.setOnClickListener {
            val intent = Intent(this@AddressListActivity, AddAddressActivity::class.java)
            startActivityForResult(intent, ADD_ADDRESS_REQUEST_CODE)
        }

        getAddressList()
    }

    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ADD_ADDRESS_REQUEST_CODE) {
                getAddressList()
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Request Cancelled", "To add the address.")
        }
    }

    private fun getAddressList() {
        mFireStore.collection(ADDRESSES)
            .whereEqualTo(USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e(this.javaClass.simpleName, document.documents.toString())
                val addressList: ArrayList<Address> = ArrayList()

                for (i in document.documents) {
                    val address = i.toObject(Address::class.java)!!
                    address.id = i.id
                    addressList.add(address)
                }

                successAddressListFromFirestore(addressList)
            }
            .addOnFailureListener { e ->
                Log.e(this.javaClass.simpleName, "Error while getting the address list.", e)
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

    private fun successAddressListFromFirestore(addressList: ArrayList<Address>) {
        if (addressList.size > 0) {

            binding.rvAddressList.visibility = View.VISIBLE
            binding.tvNoAddressFound.visibility = View.GONE

            binding.rvAddressList.layoutManager = LinearLayoutManager(this@AddressListActivity)
            binding.rvAddressList.setHasFixedSize(true)

            val addressAdapter = AddressListAdapter(this@AddressListActivity, addressList, mSelectAddress, this)
            binding.rvAddressList.adapter = addressAdapter

        } else {
            binding.rvAddressList.visibility = View.GONE
            binding.tvNoAddressFound.visibility = View.VISIBLE
        }
    }

    fun deleteAddress(addressId: String) {

        mFireStore.collection(ADDRESSES)
            .document(addressId)
            .delete()
            .addOnSuccessListener {
                deleteAddressSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    this.javaClass.simpleName,
                    "Error while deleting the address.",
                    e
                )
            }
    }

    private fun deleteAddressSuccess() {
        Toast.makeText(
            this,
            "Delete Success",
            Toast.LENGTH_SHORT
        ).show()

        getAddressList()
    }

    companion object{
        const val EXTRA_SELECT_ADDRESS: String = "extra_select_address"
        const val ADD_ADDRESS_REQUEST_CODE: Int = 121
        const val ADDRESSES: String = "addresses"
        const val USER_ID: String = "user_id"

    }
}