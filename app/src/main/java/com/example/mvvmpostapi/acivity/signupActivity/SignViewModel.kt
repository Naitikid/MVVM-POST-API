package com.example.mvvmpostapi.acivity.signupActivity

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmpostapi.R
import com.example.mvvmpostapi.model.DefaultResponse
import com.example.mvvmpostapi.utlis.Resource
import com.example.mvvmpostapi.utlis.ResponseCodeCheck
import com.example.mypostapi.Webservice.RetrofitClient
import com.example.mypostapi.Webservice.UserwebServiceApi
import com.example.mypostapi.emailPattern
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class SignViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

   @Inject
   lateinit var retrofit: Retrofit

    var fullname: String = ""
    var email: String = ""
    var password: String = ""
    var imagepath: String = ""

    var fullnamerro: String = ""
    var emailerro: String = ""
    var passworderro: String = ""

    var responseCodeCheck: ResponseCodeCheck = ResponseCodeCheck()
    private var datamutable: MutableLiveData<Resource<DefaultResponse>> = MutableLiveData()
    var dataLiveData: MutableLiveData<Resource<DefaultResponse>> = datamutable


    fun validation(): Boolean {
        fullnamerro = ""
        emailerro = ""
        passworderro = ""

        if (fullname.isEmpty()) {
            fullnamerro = getApplication<Application>().getString(R.string.entereUsername)
            return false
        } else if (email.isEmpty()) {
            emailerro = getApplication<Application>().getString(R.string.enteremail)
            return false
        }else if(!email.matches(emailPattern.toRegex())){
            emailerro = getApplication<Application>().getString(R.string.enterecorrect)
            return false
        }
        else if (password.isEmpty()) {
            passworderro = getApplication<Application>().getString(R.string.enterepassword)
            return false
        }else if(password.length<6)
        {
            passworderro=getApplication<Application>().getString(R.string.enterepassword6latter)
            return false
        }else if(imagepath.isEmpty())
        {
            Toast.makeText(getApplication(), R.string.pleaseselectone, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun signupUser() {
        val file=File(imagepath)
        Log.e("TAG", "signupUser: "+file, )
        val requestFile: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val multipartBody=MultipartBody.Part.createFormData("profile_picture",file.name,requestFile)
        val fullNameS = RequestBody.create(MediaType.parse("multipart/form-data"), fullname)
        val emailS = RequestBody.create(MediaType.parse("multipart/form-data"), email)
        val passwordS = RequestBody.create(MediaType.parse("multipart/form-data"), password)

        val hashMap: HashMap<String, RequestBody> = HashMap()
        hashMap.apply {
            put("name", fullNameS)
            put("email", emailS)
            put("password", passwordS)
        }

       datamutable.postValue(Resource.loading(null))

//        val webserviceinterface=RetrofitClient.getRetrofit().create(UserwebServiceApi::class.java)
        val webserviceinterface=retrofit.create(UserwebServiceApi::class.java)
        val signUpRepository= SignUpRepository(webserviceinterface)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository:Response<DefaultResponse> = signUpRepository.registerUser(multipartBody,hashMap)
                datamutable.postValue(responseCodeCheck.getResponseResult(repository))
            }catch(e:Exception){
                datamutable.postValue(Resource.error("fill the details", null))
            }

        }
        // Below code for Retrofit

       /* RetrofitClient.getRetrofit().create(UserwebServiceApi::class.java).registerUser(multipartBody,hashMap)
            .enqueue(object : Callback<DefaultResponse?> {
                override fun onResponse(
                    call: Call<DefaultResponse?>,
                    response: Response<DefaultResponse?>
                ) {
                    if (response.body() !== null) {

                        try {
                            datamutable.postValue(responseCodeCheck.getResponseResult(Response.success(response.body())))
                        } catch (e: Exception) {
                            datamutable.postValue(Resource.error(e.message!!, null))
                        }
                    }
                }
                override fun onFailure(call: Call<DefaultResponse?>, t: Throwable) {
                    datamutable.postValue(Resource.error(t.message!!, null))
                    *//*Toast.makeText(getApplication(), t.message, Toast.LENGTH_SHORT).show()*//*
                }
            })*/
    }
}

