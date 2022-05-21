package com.bangkit.artnesia

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.FragmentTransaction
import com.bangkit.artnesia.databinding.ActivityMainBinding
import com.bangkit.artnesia.ui.activity.CameraActivity
import com.bangkit.artnesia.ui.fragment.ShopFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val bottomNavigationView: BottomNavigationView
        get() = findViewById(R.id.bottomNavMenu)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomItemSelectedListener)

        if (savedInstanceState != null) {
            savedInstanceState.getInt(SELECTED_MENU)
        } else {
            bottomNavigationView.selectedItemId = R.id.homeMenu
        }
    }

    private val bottomItemSelectedListener =
        object: BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.homeMenu -> {
                        val fragment = HomeFragment()
                        supportFragmentManager.beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.nav_fragment, fragment, fragment::class.java.simpleName)
                            .commit()
                        return true
                    }
                    R.id.shopMenu -> {
                        val fragment = ShopFragment()
                        supportFragmentManager.beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.nav_fragment, fragment, fragment::class.java.simpleName)
                            .commit()
                        return true
                    }
                    R.id.cameraMenu -> {
                        loadActivity(CameraActivity())
                        return true
                    }
                    R.id.literatureMenu -> {
                        val fragment = ExploreFragment()
                        supportFragmentManager.beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.nav_fragment, fragment, fragment::class.java.simpleName)
                            .commit()
                        return true
                    }
                    R.id.profileMenu -> {
                        val fragment = ProfileFragment()
                        supportFragmentManager.beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.nav_fragment, fragment, fragment::class.java.simpleName)
                            .commit()
                        return true
                    }
                }
                return false
            }
        }

    private fun loadActivity(activity: Activity){
        val intentActivity = Intent(this, activity::class.java)
        startActivity(intentActivity)
    }

    companion object {
        const val SELECTED_MENU = "selected_menu"
    }
}