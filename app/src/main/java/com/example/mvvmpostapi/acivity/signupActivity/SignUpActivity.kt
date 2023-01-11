package com.example.mvvmpostapi.acivity.signupActivity

import android.Manifest
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mvvmpostapi.R
import com.example.mvvmpostapi.acivity.loginActivity.LoginActivity
import com.example.mvvmpostapi.databinding.ActivitySignUpBinding
import com.example.mvvmpostapi.utlis.Dilogmain
import com.example.mvvmpostapi.utlis.Status
import com.example.mypostapi.Utlis.Utils
import com.example.viewmodeldemo.utils.PathUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlin.math.log

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    companion object {
        private const val CAMERA_PERMISSION = 100
    }

    var imageuri: String = ""
    private lateinit var binding: ActivitySignUpBinding

    private lateinit var viewModel: SignViewModel

    private lateinit var dilogmain: Dilogmain
    private lateinit var progressDialog: ProgressDialog
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var LauncharPermission: ActivityResultLauncher<String>
    private var isCamera: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        viewModel = ViewModelProvider(this).get(SignViewModel::class.java)
        dilogmain = Dilogmain(this)
        progressDialog = ProgressDialog(this)
        /*  for data bindinig */
        viewModelbinding()
        //for photoPicker
        imagePicker()
        // for image delect
        imagedelect()
        //forPermmision
        launcherpermmision()
    }

    private fun viewModelbinding() {
        binding.signUpViewModel = viewModel
        viewModel.dataLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismiss()
                    if (it.data?.status == true) {
                        dilogmain.dialog(
                            getString(R.string.MvvmPostApiDaggerHilt),
                            it.data.message,
                            getString(R.string.signinkey_Dilog),
                            1
                        )
                    } else if (it.data?.status == false) {
                        progressDialog.dismiss()
                        dilogmain.dialog(
                            getString(R.string.MvvmPostApiDaggerHilt),
                            it.data.message,
                            getString(R.string.signinkey_Dilog),
                            0
                        )
                    }
                }
                Status.LOADING -> {
                    progressDialog.setMessage(getString(R.string.Loding))
                    progressDialog.setCancelable(false)
                    progressDialog.show()
                    /*Toast.makeText(this, "L", Toast.LENGTH_SHORT).show()*/
                }
                Status.ERROR -> {
                    progressDialog.dismiss()
                    /* Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()*/
                }
            }
        }
        binding.imageUploadBtn.setOnClickListener {
            inputphoto()
        }
        binding.loginlinkforSign.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnsign.setOnClickListener {
            binding.fullnamesighup.helperText = ""
            binding.emailEditLayoutSign.helperText = ""
            binding.passwordEditLayoutSign.helperText = ""
            //    Utils().hideSoftKeyBoard(this@SignUpActivity)
            viewModel.imagepath = imageuri
            if (viewModel.validation()) {
                if (Utils().isNetworkAvailable(this@SignUpActivity)) {
                    viewModel.signupUser()
                } else {
                    Toast.makeText(
                        this@SignUpActivity,
                        R.string.pleaseturninternet,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                binding.fullnamesighup.helperText = viewModel.fullnamerro
                binding.emailEditLayoutSign.helperText = viewModel.emailerro
                binding.passwordEditLayoutSign.helperText = viewModel.passworderro
            }
        }
    }

    private fun launcherpermmision() {
        LauncharPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    if (isCamera) {
                        openCamera()
                    } else {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                } else {
                    Dilogmain(this).showPermisssionDeniedDialog(
                        getString(R.string.MvvmPostApiDaggerHilt),
                        getString(R.string.Permisssion),
                        0
                    )
                }
            }
    }

    private fun imagedelect() {
        binding.logoimageremove.setOnClickListener {
            Glide.with(binding.logoidLogin)
                .load(R.drawable.logo)
                .placeholder(R.drawable.logo)
                .into(binding.logoidLogin)
            viewModel.imagepath = ""
            Toast.makeText(this, "Remove", Toast.LENGTH_SHORT).show()
        }
    }

    private fun imagePicker() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.logoidLogin.setImageURI(uri)
                imageuri = PathUtil.getPath(this, uri).toString()
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    }

    /*  for data bindinig */
    private fun inputphoto() {
        val builder = MaterialAlertDialogBuilder(this@SignUpActivity)
        builder.setTitle(R.string.MvvmPostApiDaggerHilt)
        builder.setMessage(R.string.dialogtitle)
        builder.setNegativeButton("Camera") { _, _ ->
            isCamera = true
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openCamera()

            } else {
                LauncharPermission.launch(Manifest.permission.CAMERA)
            }
        }
        builder.setPositiveButton("Gallary") { _, _ ->
            isCamera = false
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                opengallary()

            } else {
                LauncharPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        builder.show()
    }

    private fun opengallary() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    // for Camera On Activityresult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CAMERA_PERMISSION -> {
                val pic = data?.getParcelableExtra<Bitmap>("data")
                binding.logoidLogin.setImageBitmap(pic)
                val uri = Utils().getImageUri(this, pic!!)
                imageuri = PathUtil.getPath(this, uri!!)!!.toString()
               // Log.e("TAG FOR IMAGE URI"  , "onActivityResult: "+imageuri)

            }
        }
    }

    private fun openCamera() {
        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        Log.e(TAG, "openCamera: " + i)
        startActivityForResult(i, CAMERA_PERMISSION)
    }
}