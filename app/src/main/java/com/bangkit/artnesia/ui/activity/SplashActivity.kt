package com.bangkit.artnesia.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.bangkit.artnesia.R
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // It is deprecated in the API level 30.
        Handler().postDelayed(
            {
                // Get the current logged in user id
                val currentUser = FirebaseAuth.getInstance().currentUser
                var currentUserID = ""
                if (currentUser != null) {
                    currentUserID = currentUser.uid
                }

                if (currentUserID.isNotEmpty()) {
                    // Launch main activity
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                } else {
                    // Launch the Login Activity
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                }
                finish()
            },
            2500
        )
    }
}