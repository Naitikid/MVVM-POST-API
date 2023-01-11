package com.example.mvvmpostapi.acivity.homeActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mvvmpostapi.R
import com.example.mvvmpostapi.databinding.ActivityHomeBinding
import com.example.mvvmpostapi.sharedpreferences.SharedpreferencesApi
import com.example.mvvmpostapi.utlis.Dilogmain
import com.example.mvvmpostapi.utlis.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
   // private lateinit var sharedpre: SharedpreferencesApi
    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeviewmodel: Homeviewmodel
    @Inject
    lateinit var sharedpre:SharedpreferencesApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        homeviewmodel = ViewModelProvider(this).get(Homeviewmodel::class.java)
        binding.profileviewmodel = homeviewmodel
        homeviewmodel.dataLiveDataH.observe(this)

        {
            when (it.status) {
                Status.SUCCESS -> {
                  //  sharedpre=SharedpreferencesApi(this)
                    binding.usernameid.setText("${it.data?.data?.id}")
                    binding.Emailtext.setText("${it.data?.data?.email}")
                    Glide.with(this).load(it.data?.data?.profile_picture).placeholder(R.drawable.logo).into(binding.imageviewforHome)
                    Log.e("TAG-for ID for Home", "onCreate: " + it.data?.data?.profile_picture)

                //    sharedpre.setPrefBoolean("userlogin", true)

                    sharedpre.setPrefBoolean("userlogin", true)
                    it.data?.data?.id.toString()
                    Toast.makeText(application, it.data?.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    Toast.makeText(this, "L", Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    Toast.makeText(this, "E", Toast.LENGTH_SHORT).show()
                }
            }
        }

            homeviewmodel.profileuser()
        }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                Dilogmain(this@HomeActivity).diloglogout(R.string.MvvmPostApiDaggerHilt,R.string.areyousure)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menulogout, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
