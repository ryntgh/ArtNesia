package com.bangkit.artnesia.ui.activity

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.artnesia.R
import com.bangkit.artnesia.data.local.LiteratureData
import com.bangkit.artnesia.data.model.LiteratureModel
import com.bangkit.artnesia.databinding.ActivityDetailLiteratureBinding
import com.bangkit.artnesia.ui.adapter.ExploreLiteratureAdapter
import com.bumptech.glide.Glide

class DetailLiteratureActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLiteratureBinding
    private lateinit var literatureAdapter: ExploreLiteratureAdapter

    private fun getLiterature(): List<LiteratureModel> {
        return LiteratureData.generateLiterature()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLiteratureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        literatureAdapter = ExploreLiteratureAdapter(this)
        literatureAdapter.setData(getLiterature())

        binding.rvLiteratureMore.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvLiteratureMore.setHasFixedSize(true)
        binding.rvLiteratureMore.adapter = literatureAdapter

        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val img: ImageView = findViewById(R.id.imageView2)
        val nameTxt: TextView = findViewById(R.id.tv_detail_artName)
        val originTxt: TextView = findViewById(R.id.tv_detail_origin)
        val summaryTxt: TextView = findViewById(R.id.tv_deail_desc)
        val moreText: TextView = findViewById(R.id.tv_detail_another)

        val i = this.intent

        val images = i.extras!!.getString("IMAGE_KEY")
        val name = i.extras!!.getString("NAME_KEY")
        val origin = i.extras!!.getString("ORIGIN_KEY")
        val summary = i.extras!!.getString("SUMMARY_KEY")

        title = "Detail $name"

        img.setImageURI(Uri.parse(images))
        nameTxt.text = name
        originTxt.text = origin
        summaryTxt.text = summary
        moreText.text = getString(R.string.find_another)

        Glide.with(this)
            .load(images)
            .into(img)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }
}