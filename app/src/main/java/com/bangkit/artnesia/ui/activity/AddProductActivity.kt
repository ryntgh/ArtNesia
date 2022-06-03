package com.bangkit.artnesia.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bangkit.artnesia.R
import com.bangkit.artnesia.data.model.Product
import com.bangkit.artnesia.databinding.ActivityAddProductBinding
import com.bangkit.artnesia.ui.utils.CustomDialog
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

class AddProductActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityAddProductBinding
    private var mSelectedImageFileUri: Uri? = null
    private var mProductImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.ivAddUpdateProduct.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {
                R.id.iv_add_update_product -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        showImageChooser(this@AddProductActivity)
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            2
                        )
                    }
                }

                R.id.btn_submit -> {
                    if (validateProductDetails()) {
                        uploadProductImage()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageChooser(this@AddProductActivity)
            } else {
                Toast.makeText(
                    this,
                    "Oops, you just denied the permission for storage. You can also allow it from settings.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == 2
            && data!!.data != null
        ) {
            binding.ivAddUpdateProduct.setImageDrawable(
                ContextCompat.getDrawable(
                    this@AddProductActivity,
                    R.drawable.ic_edit
                )
            )

            mSelectedImageFileUri = data.data!!

            try {
                loadProductPicture(
                    mSelectedImageFileUri!!,
                    binding.ivProductImage
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun validateProductDetails(): Boolean {
        return when {

            mSelectedImageFileUri == null -> {
                showErrorSnackBar("Please select product image", true)
                false
            }

            TextUtils.isEmpty(binding.etProductCategory.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter product category", true)
                false
            }

            TextUtils.isEmpty(binding.etProductTitle.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter product title", true)
                false
            }

            TextUtils.isEmpty(binding.etProductPrice.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter product price", true)
                false
            }

            TextUtils.isEmpty(binding.etProductDescription.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter product description", true)
                false
            }

            TextUtils.isEmpty(binding.etProductQuantity.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter product quantity", true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun uploadProductImage() {

        CustomDialog.showLoading(this)

        uploadImageToCloudStorage(
            this@AddProductActivity,
            mSelectedImageFileUri,
            "Product_Image"
        )
    }

    private fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "."
                    + getFileExtension(
                activity,
                imageFileURI
            )
        )

        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())
                        imageUploadSuccess(uri.toString())
                    }
            }
            .addOnFailureListener { exception ->
                CustomDialog.hideLoading()
                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

    private fun getFileExtension(activity: Activity, uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

    private fun imageUploadSuccess(imageURL: String) {
        mProductImageURL = imageURL
        uploadProductDetails()
    }

    private fun uploadProductDetails() {
        val username =
            this.getSharedPreferences(ARTNESIA_PREFERENCES, Context.MODE_PRIVATE)
                .getString(LOGGED_IN_USERNAME, "")!!

        val product = Product(
            getCurrentUserID(),
            username,
            binding.etProductTitle.text.toString().trim { it <= ' ' },
            binding.etProductPrice.text.toString().trim { it <= ' ' },
            binding.etProductCategory.text.toString().trim { it <= ' ' },
            binding.etProductDescription.text.toString().trim { it <= ' ' },
            binding.etProductQuantity.text.toString().trim { it <= ' ' },
            mProductImageURL
        )

        FirebaseFirestore.getInstance().collection(PRODUCTS)
            .document()
            .set(product, SetOptions.merge())
            .addOnSuccessListener {
                this.productUploadSuccess()
            }
            .addOnFailureListener { e ->
                CustomDialog.hideLoading()
                Log.e(
                    this.javaClass.simpleName,
                    "Error while uploading the product details.",
                    e
                )
            }
    }

    private fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    private fun productUploadSuccess() {

        // Hide the progress dialog
        CustomDialog.hideLoading()

        Toast.makeText(
            this@AddProductActivity,
            "Your product is uploaded successfully",
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }

    private fun showImageChooser(activity: Activity) {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        activity.startActivityForResult(galleryIntent, 2)
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

    private fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.error
                )
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.success
                )
            )
        }
        snackBar.show()
    }

    companion object {
        const val ARTNESIA_PREFERENCES: String = "MyShopPalPrefs"
        const val LOGGED_IN_USERNAME: String = "logged_in_username"
        const val PRODUCTS: String = "products"
    }
}