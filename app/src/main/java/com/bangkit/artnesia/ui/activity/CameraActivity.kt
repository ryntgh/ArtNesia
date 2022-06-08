package com.bangkit.artnesia.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bangkit.artnesia.R
import com.bangkit.artnesia.databinding.ActivityCameraBinding
import com.bangkit.artnesia.ui.utils.createFile
import com.bangkit.artnesia.ui.utils.reduceFileImage
import com.bangkit.artnesia.ui.utils.rotateBitmap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var result: Bitmap
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.captureImage.setOnClickListener { takePhoto() }

        binding.galleryButton.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose a Picture")
            launcherIntentGallery.launch(chooser)
        }

        binding.galleryImageUse.setOnClickListener {
            val i = Intent(this@CameraActivity, DetectionActivity::class.java)
            i.putExtra("uriImage", imageUri.toString())
            startActivity(i)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )

            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    "Camera not Started",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    AlertDialog.Builder(this@CameraActivity).apply {
                        result = BitmapFactory.decodeFile(photoFile.path)
                        result = rotateBitmap(
                            BitmapFactory.decodeFile(photoFile.path),
                            cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
                        )
                        binding.apply {
                            galleryImagePreview.setImageBitmap(result)
                            galleryImagePreview.visibility = View.VISIBLE
                            viewFinder.visibility = View.INVISIBLE
                            captureImage.visibility = View.INVISIBLE
                            galleryButton.visibility = View.INVISIBLE
                            galleryImageUse.visibility = View.INVISIBLE
                        }
                        setCancelable(false)
                        setTitle("Success!")
                        setMessage("Image taken successfully, use this image?")
                        setNegativeButton("No") { _, _ ->
                            binding.apply {
                                galleryImagePreview.visibility = View.INVISIBLE
                                viewFinder.visibility = View.VISIBLE
                                captureImage.visibility = View.VISIBLE
                                galleryButton.visibility = View.VISIBLE
                                galleryImageUse.visibility = View.INVISIBLE
                            }
                        }
                        setPositiveButton("Yes") { _, _ ->
                            val file = reduceFileImage(photoFile)
                            val intent = Intent(this@CameraActivity, DetectionActivity::class.java)
                            intent.putExtra("picture", file)
                            intent.putExtra(
                                "isBackCamera",
                                cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
                            )
                            startActivity(intent)
                            finish()
                        }
                        setIcon(R.drawable.logo)
                        create()
                        show()
                    }
                }
            }
        )
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            imageUri = selectedImg
            binding.apply {
                galleryImagePreview.setImageURI(selectedImg)
                viewFinder.visibility = View.INVISIBLE
                captureImage.visibility = View.INVISIBLE
                galleryButton.visibility = View.VISIBLE
                galleryImageUse.visibility = View.VISIBLE
                galleryImagePreview.visibility = View.VISIBLE
            }
        }
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }


    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "No permission granted.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}