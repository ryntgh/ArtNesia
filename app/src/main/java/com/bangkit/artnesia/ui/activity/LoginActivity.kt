package com.bangkit.artnesia.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.bangkit.artnesia.R
import com.bangkit.artnesia.data.model.User
import com.bangkit.artnesia.databinding.ActivityLoginBinding
import com.bangkit.artnesia.ui.utils.CustomDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ActivityLoginBinding
    private val mFireStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding.loginButton.setOnClickListener(this)
        binding.registerTextView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.loginButton -> {
                    logInRegisteredUser()
                }
                R.id.registerTextView -> {
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.emailEditText.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter an email id", true)
                false
            }
            TextUtils.isEmpty(binding.passwordEditText.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter a password", true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun logInRegisteredUser() {
        if (validateLoginDetails()) {
            CustomDialog.showLoading(this)

            val email = binding.emailEditText.text.toString().trim { it <= ' ' }
            val password = binding.passwordEditText.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        getUserDetails(this@LoginActivity)
                    } else {
                        // Hide the progress dialog
                        CustomDialog.hideLoading()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    fun userLoggedInSuccess(user: User) {
        CustomDialog.hideLoading()
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        /*
        if (user.profileCompleted == 0) {
            // If the user profile is incomplete then launch the UserProfileActivity.
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else {
            // Redirect the user to Dashboard Screen after log in.
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        }
         */
        finish()
    }

    fun getUserDetails(activity: Activity) {
        mFireStore.collection("users")
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val user = document.toObject(User::class.java)!!
                val sharedPreferences =
                    activity.getSharedPreferences(
                        "MyShopPalPrefs",
                        Context.MODE_PRIVATE
                    )
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    "logged_in_username",
                    user.name
                )
                editor.apply()
                when (activity) {
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is LoginActivity -> {
                        CustomDialog.hideLoading()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun showErrorSnackBar(message : String, errorMessage : Boolean){
        val snackBar = Snackbar.make(findViewById(android.R.id.content),message, Snackbar.LENGTH_LONG)

        val snackBarView = snackBar.view

        if (errorMessage){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this,R.color.secondary)
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this,R.color.primary)
            )
        }
        snackBar.show()
    }
}