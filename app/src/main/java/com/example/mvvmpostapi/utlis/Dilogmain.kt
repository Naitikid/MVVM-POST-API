package com.example.mvvmpostapi.utlis

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import com.example.mvvmpostapi.BuildConfig
import com.example.mvvmpostapi.R
import com.example.mvvmpostapi.acivity.homeActivity.HomeActivity
import com.example.mvvmpostapi.acivity.loginActivity.LoginActivity
import com.example.mvvmpostapi.acivity.mainAcivity.MainActivity
import com.example.mvvmpostapi.sharedpreferences.SharedpreferencesApi
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject


class Dilogmain(val activity: Activity) {

    @Inject
    lateinit var sharedpre: SharedpreferencesApi

    @Inject
    lateinit var context: Context

    // private lateinit var sharedpre: SharedpreferencesApi


    fun dialog(title: String, massage: String, type: String, isCheck: Int) {
        val materialAlertDialog = MaterialAlertDialogBuilder(activity)
        materialAlertDialog.setTitle(title)
        materialAlertDialog.setIcon(R.drawable.logo)
        materialAlertDialog.setMessage(massage)
        materialAlertDialog.setPositiveButton(activity.resources.getString(R.string.ok)) { A, b ->

            if (type == "Login") {
                if (isCheck == 1) {
                    activity.startActivity(Intent(activity, HomeActivity::class.java))
                    activity.finishAffinity()
                }
            } else if (type == "Signin") {
                if (isCheck == 1) {
                    activity.startActivity(Intent(activity, LoginActivity::class.java))
                    activity.finishAffinity()
                }
            }
        }
        materialAlertDialog.create()
        materialAlertDialog.show()
    }

    @SuppressLint("RestrictedApi")
    fun diloglogout(title: Int, massage: Int) {

        sharedpre = SharedpreferencesApi(activity)
        val materialAlertDiloglogout = MaterialAlertDialogBuilder(activity)
        materialAlertDiloglogout.setTitle(title)
        materialAlertDiloglogout.setIcon(R.drawable.logo)
        materialAlertDiloglogout.setMessage(massage)
        materialAlertDiloglogout.setNegativeButton("Cancel", null)
        materialAlertDiloglogout.setPositiveButton("Ok") { A, B ->
            sharedpre.setPrefBoolean("userlogin", false)

            //   sharedpre.setPrefBoolean("userlogin", false)
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
            Toast.makeText(activity, R.string.logout, Toast.LENGTH_SHORT).show()
        }
        materialAlertDiloglogout.show()
    }

    fun showPermisssionDeniedDialog(title: String, message: String, requestcode: Int) {
        val builder = MaterialAlertDialogBuilder(activity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setMessage("Setting")
        builder.setNegativeButton("Go To Setting") { _, _ ->
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
            activity.startActivityIfNeeded(intent, requestcode)
        }
        builder.setPositiveButton("No") { cancel, _ ->
            cancel.dismiss()
        }
        builder.create()
        builder.show()
    }
    /*fun photocamera() {
     val builder = MaterialAlertDialogBuilder(activity)
       builder.setTitle(R.string.MvvmPostApi)
       builder.setMessage(R.string.dialogtitle)
       builder.setNegativeButton("Camera") { _, _ ->
         //  camerapermission()
       }
      builder.setPositiveButton("Gallary") { _, _ ->
          //  gallarypermission()
       }
       builder.show()
   }*/
}
