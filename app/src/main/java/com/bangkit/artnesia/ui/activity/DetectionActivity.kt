package com.bangkit.artnesia.ui.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.artnesia.databinding.ActivityDetectionBinding
import com.bangkit.artnesia.ui.utils.rotateBitmap
import java.io.File

class DetectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetectionBinding
    private lateinit var result: Bitmap
    private var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val imageCamera = intent.getSerializableExtra("picture") as File?
        val isBackCamera = intent.getBooleanExtra("isBackCamera", true)

        if (imageCamera != null) {
            result = BitmapFactory.decodeFile(imageCamera.path)
            result = rotateBitmap(
                BitmapFactory.decodeFile(imageCamera.path),
                isBackCamera
            )
            binding.ivPlaceholder.setImageBitmap(result)
        }

        val imageGallery = intent.getStringExtra("uriImage")
        if (imageGallery != null) {
            fileUri = Uri.parse(imageGallery)
            binding.ivPlaceholder.setImageURI(fileUri)
        }
    }
}