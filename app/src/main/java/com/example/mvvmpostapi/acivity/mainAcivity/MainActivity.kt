package com.example.mvvmpostapi.acivity.mainAcivity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.mvvmpostapi.R
import com.example.mvvmpostapi.acivity.homeActivity.HomeActivity
import com.example.mvvmpostapi.acivity.signupActivity.SignUpActivity
import com.example.mvvmpostapi.databinding.ActivityMainBinding
import com.example.mvvmpostapi.sharedpreferences.SharedpreferencesApi
import dagger.Provides
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


// private  lateinit var sharedpre : SharedpreferencesApi

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var sharedpre: SharedpreferencesApi

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  sharedpre = SharedpreferencesApi(this)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_main)
        startscreen()
    }
    private fun startscreen() {
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        Handler(Looper.getMainLooper()).postDelayed({

            if (sharedpre.getPrefBoolean("userlogin"))
            {
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                startActivity(Intent(this, SignUpActivity::class.java))
            }
        }, 1500)
    }
}

