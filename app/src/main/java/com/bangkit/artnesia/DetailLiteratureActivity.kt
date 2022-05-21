package com.bangkit.artnesia

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class DetailLiteratureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_literature)

        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val img: ImageView = findViewById(R.id.imageView2)
        val nameTxt: TextView = findViewById(R.id.tv_detail_artName)
        val originTxt: TextView = findViewById(R.id.tv_detail_origin)
        val summaryTxt: TextView = findViewById(R.id.tv_deail_desc)

        val i = this.intent

        val images  = i.extras!!.getString("IMAGE_KEY")
        val name    = i.extras!!.getString("NAME_KEY")
        val origin  = i.extras!!.getString("ORIGIN_KEY")
        val summary = i.extras!!.getString("SUMMARY_KEY")

        title = "Detail $name"

        img.setImageURI(Uri.parse(images))
        nameTxt.text = name
        originTxt.text = origin
        summaryTxt.text = summary

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