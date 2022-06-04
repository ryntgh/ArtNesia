package com.bangkit.artnesia.ui.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.R
import com.bangkit.artnesia.data.local.LiteratureData
import com.bangkit.artnesia.data.model.Literature
import com.bangkit.artnesia.data.model.LiteratureModel
import com.bangkit.artnesia.databinding.ActivityDetailLiteratureBinding
import com.bangkit.artnesia.ui.adapter.ExploreLiteratureAdapter
import com.bangkit.artnesia.ui.adapter.LiteratureAdapter
import com.bumptech.glide.Glide
import com.google.firebase.firestore.*
import java.io.IOException

class DetailLiteratureActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLiteratureBinding
    private lateinit var mLiteratureDetail: Literature

    private var mLiteratureId: String = ""
    private val mFireStore = FirebaseFirestore.getInstance()

    private lateinit var literatureAdapter: LiteratureAdapter
    private lateinit var literatureList: ArrayList<Literature>
    private lateinit var literatureRV: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLiteratureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(EXTRA_LITERATURE_ID)) {
            mLiteratureId =
                intent.getStringExtra(EXTRA_LITERATURE_ID)!!
        }

        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        getLiteratureDetails(mLiteratureId)

        getLiterature()
    }

    private fun getLiteratureDetails(literatureId: String) {

        mFireStore.collection(LITERATURE)
            .document(literatureId)
            .get()
            .addOnSuccessListener { document ->
                Log.e(this.javaClass.simpleName, document.toString())

                val lit = document.toObject(Literature::class.java)!!
                this.productDetailsSuccess(lit)
            }
            .addOnFailureListener { e ->


                Log.e(this.javaClass.simpleName, "Error while getting the product details.", e)
            }
    }

    private fun productDetailsSuccess(literature: Literature) {

        mLiteratureDetail = literature

        loadProductPicture(literature.image, binding.imageView2)

        binding.tvDetailClasssification.text = literature.classification
        binding.tvDetailArtName.text = literature.name
        binding.tvDetailOrigin.text = literature.origin
        binding.tvDeailDesc.text = literature.description

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

    private fun getLiterature(){
        literatureRV = binding.rvLiteratureMore
        literatureRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        literatureRV.setHasFixedSize(true)

        literatureList = arrayListOf()

        literatureAdapter  = LiteratureAdapter(this , literatureList)

        literatureRV.adapter = literatureAdapter

        getLiteratureData()
    }

    private fun getLiteratureData() {
        mFireStore.collection("literature")
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
                            val lit = dc.document.toObject(Literature::class.java)
                            lit.literature_id = dc.document.id

                            literatureList.add(lit)
                        }
                    }
                    //randomize list
                    literatureList.shuffle()

                    literatureAdapter.notifyDataSetChanged()
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }

    companion object {
        const val EXTRA_LITERATURE_ID: String = "extra_literature_id"
        const val LITERATURE: String = "literature"
    }
}