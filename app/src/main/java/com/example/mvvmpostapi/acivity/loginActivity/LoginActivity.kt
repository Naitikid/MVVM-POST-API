package com.example.mvvmpostapi.acivity.loginActivity

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmpostapi.R
import com.example.mvvmpostapi.databinding.ActivityLoginBinding
import com.example.mvvmpostapi.sharedpreferences.SharedpreferencesApi
import com.example.mvvmpostapi.utlis.Dilogmain
import com.example.mvvmpostapi.utlis.Status
import com.example.mypostapi.Utlis.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginviewModel: LoginViewModel
    @Inject
    lateinit var sharedpre:SharedpreferencesApi

    // private lateinit var sharedpre: SharedpreferencesApi


    private lateinit var dilogmain: Dilogmain
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_login)
        loginviewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        dilogmain = Dilogmain(this)

      //  sharedpre= SharedpreferencesApi(this)

        progressDialog= ProgressDialog(this)
        binding.loginviewmodel = loginviewModel
        loginviewModel.dataLiveDataL.observe(this){
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data?.status==true) {
                        progressDialog.dismiss()
                        dilogmain.dialog(getString(R.string.MvvmPostApiDaggerHilt), it.data.message, getString(R.string.loginkey_Dilog), 1)
                            sharedpre.setPrefString("id",it.data.data.id)
                      //  sharedpre.setPrefString("id", it.data.data.id)

                        Log.e("TAG-for ID", "onCreate: " + it.data.data.id)

                       /* startActivity(Intent(this, HomeActivity::class.java))*/
                    }else if (it.data?.status==false){
                        progressDialog.dismiss()
                        dilogmain.dialog(getString(R.string.MvvmPostApiDaggerHilt), it.data.message, getString(R.string.loginkey_Dilog), 0)
                    }
                   /* Toast.makeText(application,it.data?.message, Toast.LENGTH_SHORT).show()*/
                }
                Status.LOADING -> {
                    progressDialog.setMessage(getString(R.string.Loding))
                    progressDialog.setCancelable(false)
                    progressDialog.show()
                    /*Toast.makeText(this, "L", Toast.LENGTH_SHORT).show()*/
                }
                Status.ERROR -> {
                    progressDialog.dismiss()
                    Toast.makeText(this, "E", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnLogin.setOnClickListener {
            binding.emailEditLayoutLogin.helperText=""
            binding.passwordEditLayoutLogin.helperText=""
//            Utils().hideSoftKeyBoard(this@LoginActivity)
            if (loginviewModel.validationlogin()) {
                if (Utils().isNetworkAvailable(this@LoginActivity)){
                    loginviewModel.loginuser()
                }else{
                    Toast.makeText(
                        this@LoginActivity,
                        R.string.pleaseturninternet,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                binding.emailEditLayoutLogin.helperText = loginviewModel.emailloginerro
                binding.passwordEditLayoutLogin.helperText = loginviewModel.passwrodloginerro
            }
        }
    }
}