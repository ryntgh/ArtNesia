package com.bangkit.artnesia.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.artnesia.data.model.History
import com.bangkit.artnesia.databinding.ActivityHistoryBinding
import com.bangkit.artnesia.ui.adapter.HistoryAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val mFireStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getHistoryList()
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun getHistoryList() {
        mFireStore.collection("history")
            .whereEqualTo("user_id", getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e(this.javaClass.simpleName, document.documents.toString())
                val list: ArrayList<History> = ArrayList()
                for (i in document.documents) {
                    val soldProduct = i.toObject(History::class.java)!!
                    soldProduct.id = i.id
                    list.add(soldProduct)
                }
                successSoldProductsList(list)
            }
            .addOnFailureListener { e ->
                Log.e(this.javaClass.simpleName, "Error while getting the list of sold products.", e)
            }
    }

    private fun successSoldProductsList(soldProductsList: ArrayList<History>) {

        if (soldProductsList.size > 0) {
            binding.rvHistoryItems.visibility = View.VISIBLE
            binding.tvNoHistoryFound.visibility = View.GONE

            binding.rvHistoryItems.layoutManager = LinearLayoutManager(this)
            binding.rvHistoryItems.setHasFixedSize(true)

            val soldProductsListAdapter =
                HistoryAdapter(this, soldProductsList)
            binding.rvHistoryItems.adapter = soldProductsListAdapter
        } else {
            binding.rvHistoryItems.visibility = View.GONE
            binding.tvNoHistoryFound.visibility = View.VISIBLE
        }
    }
}