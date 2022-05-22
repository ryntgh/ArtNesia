package com.bangkit.artnesia.ui.activity

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bangkit.artnesia.R
import com.bumptech.glide.Glide

class ProductDetailActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val img: ImageView = findViewById(R.id.productImage_ProductDetailsPage)
        val nameTxt: TextView = findViewById(R.id.productName_ProductDetailsPage)
        val brandTxt: TextView = findViewById(R.id.productBrand_ProductDetailsPage)
        val descTxt: TextView = findViewById(R.id.productDes_ProductDetailsPage)
        val ratingStar: RatingBar = findViewById(R.id.productRating_singleProduct)
        val priceTxt: TextView = findViewById(R.id.productPrice_ProductDetailsPage)
        val ratingTxt: TextView = findViewById(R.id.RatingProductDetails)

        val i = this.intent
        val name    = i.extras!!.getString("NAME_KEY")

        title = "Detail $name"

        img.setImageURI(Uri.parse(i.extras!!.getString("IMAGE_KEY")))
        nameTxt.text = i.extras!!.getString("NAME_KEY")
        brandTxt.text = i.extras!!.getString("BRAND_KEY")
        descTxt.text = i.extras!!.getString("DESC_KEY")
        ratingStar.rating = i.extras!!.getFloat("RATING_KEY")
        priceTxt.text = "Rp " + i.extras!!.getString("PRICE_KEY")
        ratingTxt.text = i.extras!!.getFloat("RATING_KEY").toString()

        Glide.with(this)
            .load(i.extras!!.getString("IMAGE_KEY"))
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