package com.bangkit.artnesia.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.artnesia.R
import com.bangkit.artnesia.databinding.ActivityDetectionLiteratureBinding
import com.bangkit.artnesia.ui.adapter.LitAdapter.Companion.EXTRA_CLASS
import com.bangkit.artnesia.ui.adapter.LitAdapter.Companion.EXTRA_DESC
import com.bangkit.artnesia.ui.adapter.LitAdapter.Companion.EXTRA_IMAGE
import com.bangkit.artnesia.ui.adapter.LitAdapter.Companion.EXTRA_NAME
import com.bangkit.artnesia.ui.adapter.LitAdapter.Companion.EXTRA_ORIGIN
import com.bumptech.glide.Glide

class DetectionLiteratureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetectionLiteratureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionLiteratureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val image = intent.getStringExtra(EXTRA_IMAGE)
        val origin = intent.getStringExtra(EXTRA_ORIGIN)
        val name = intent.getStringExtra(EXTRA_NAME)
        val desc = intent.getStringExtra(EXTRA_DESC)
        val classification = intent.getStringExtra(EXTRA_CLASS)

        binding.apply {
            tvDetectionArtName.text = name
            tvDetectionOrigin.text = origin
            tvDetectionClasssification.text = classification
            tvDetectionDesc.text = desc

            Glide.with(this@DetectionLiteratureActivity)
                .load(image)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(ivDetectionLiterature)
        }
    }
}