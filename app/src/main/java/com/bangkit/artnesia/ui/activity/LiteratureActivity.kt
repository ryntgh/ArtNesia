package com.bangkit.artnesia.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.data.model.Literature
import com.bangkit.artnesia.databinding.ActivityLiteratureBinding
import com.bangkit.artnesia.ui.adapter.ListLiteratureAdapter
import com.google.firebase.firestore.*

class LiteratureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLiteratureBinding

    private val mFireStore = FirebaseFirestore.getInstance()

    private lateinit var literatureAdapter: ListLiteratureAdapter
    private lateinit var literatureList: ArrayList<Literature>
    private lateinit var literatureRV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiteratureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        getLiterature()
        val resultStr = intent.getStringExtra("imageDetection")
        binding.search.setQuery(resultStr, false)
    }

    private fun getLiterature() {
        literatureRV = binding.rvListLiterature
        literatureRV.layoutManager = LinearLayoutManager(this)
        literatureRV.setHasFixedSize(true)

        literatureList = arrayListOf()

        literatureAdapter = ListLiteratureAdapter(this, literatureList)

        literatureRV.adapter = literatureAdapter

        getLiteratureData()
    }

    private fun getLiteratureData() {
        mFireStore.collection("literature")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("Firestore error", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            val lit = dc.document.toObject(Literature::class.java)
                            lit.literature_id = dc.document.id

                            literatureList.add(lit)
                        }
                    }
                    literatureList.shuffle()

                    literatureAdapter.notifyDataSetChanged()
                }
            })
    }
}