package com.bangkit.artnesia.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.artnesia.databinding.ActivityDetectionBinding
import com.bangkit.artnesia.ml.ArtNesiaModel
import com.bangkit.artnesia.ui.utils.rotateBitmap
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.model.Model
import java.io.File

class DetectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetectionBinding
    private lateinit var result: Bitmap

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
            outputGenerator(result)
        }

        val imageGallery = intent.getStringExtra("uriImage")
        if (imageGallery != null) {
            val fileUri = Uri.parse(imageGallery)
            result = BitmapFactory.decodeStream(contentResolver.openInputStream(fileUri))
            binding.ivPlaceholder.setImageBitmap(result)
            outputGenerator(result)
        }

        binding.moreButton.setOnClickListener {
            val intent = Intent(this@DetectionActivity, LiteratureActivity::class.java)
            intent.putExtra("imageDetection", binding.tvResult.text.toString())
            startActivity(intent)
        }
    }

    private fun outputGenerator(bitmap: Bitmap) {
        val options = Model.Options.Builder()
            .setDevice(Model.Device.GPU)
            .build()

        val birdsModel = ArtNesiaModel.newInstance(this, options)

        val newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val tfimage = TensorImage.fromBitmap(newBitmap)

        val outputs = birdsModel.process(tfimage)
            .probabilityAsCategoryList.apply {
                sortByDescending { it.score }
            }

        val highProbabilityOutput = outputs[0]

        binding.tvResult.text = highProbabilityOutput.label
        Log.i("TAG", "outputGenerator: $highProbabilityOutput")
    }
}
