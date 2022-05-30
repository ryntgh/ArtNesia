package com.bangkit.artnesia.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bangkit.artnesia.R
import com.bangkit.artnesia.data.model.User
import com.bangkit.artnesia.databinding.ActivityRegisterBinding
import com.bangkit.artnesia.ui.utils.CustomDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private val mFireStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()

        binding.signupButton.setOnClickListener {

            registerUser()
        }

        binding.loginTextView.setOnClickListener{
            onBackPressed()
        }
    }

    fun setupActionBar(){
        val actionbar = supportActionBar
        actionbar!!.title = "Register"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.nameEditText.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter name!", true)
                false
            }

            TextUtils.isEmpty(binding.emailEditText.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter email!", true)
                false
            }

            TextUtils.isEmpty(binding.passwordEditText.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter password!", true)
                false
            }

            TextUtils.isEmpty(binding.passwordConfirmEditText.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter confirm password", true)
                false
            }

            binding.passwordEditText.text.toString().trim { it <= ' ' } != binding.passwordConfirmEditText.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(
                    "Password does not match",
                    true
                )
                false
            }

            else -> {
                true
            }
        }
    }


    private fun registerUser() {
        if (validateRegisterDetails()) {

            CustomDialog.showLoading(this)

            val email: String = binding.emailEditText.text.toString().trim { it <= ' ' }
            val password: String = binding.passwordEditText.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        if (task.isSuccessful) {

                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user = User(
                                firebaseUser.uid,
                                binding.nameEditText.text.toString().trim { it <= ' ' },
                                binding.emailEditText.text.toString().trim { it <= ' ' }
                            )

                            registerUser(this@RegisterActivity, user)
                        } else {

                            CustomDialog.hideLoading()

                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })
        }
    }

    /**
     * A function to notify the success result of Firestore entry when the user is registered successfully.
     */
    private fun userRegistrationSuccess() {

        CustomDialog.hideLoading()

        Toast.makeText(
            this@RegisterActivity,
            "You are registered successfully",
            Toast.LENGTH_SHORT
        ).show()

        FirebaseAuth.getInstance().signOut()
        finish()
    }

    private fun registerUser(activity: RegisterActivity, userInfo: User) {
        mFireStore.collection("users")
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                CustomDialog.hideLoading()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
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
}