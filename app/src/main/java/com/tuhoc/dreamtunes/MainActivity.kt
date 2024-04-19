package com.tuhoc.dreamtunes

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.tuhoc.dreamtunes.R
import com.tuhoc.dreamtunes.bases.BaseActivity
import com.tuhoc.dreamtunes.data.pojo.User
import com.tuhoc.dreamtunes.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun initView() {
        super.initView()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bnvNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.searchFragment, R.id.favoriteFragment, R.id.profileFragment -> {
                    binding.bnvNav.visibility = View.VISIBLE
                }

                else -> {
                    binding.bnvNav.visibility = View.GONE
                }
            }
        }

        binding.bnvNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }

                R.id.searchFragment -> {
                    navController.navigate(R.id.searchFragment)
                    true
                }

                R.id.favoriteFragment -> {
                    navController.navigate(R.id.favoriteFragment)
                    true
                }

                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
            }
            false
        }


        showPhotoPermission()

    }

    private fun showPhotoPermission() {

        val p3 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)

        if (p3 != PackageManager.PERMISSION_GRANTED) {
            requestPhotoPermission()
        }
    }


    private fun requestPhotoPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.POST_NOTIFICATIONS
            ), 123
        )
    }

}