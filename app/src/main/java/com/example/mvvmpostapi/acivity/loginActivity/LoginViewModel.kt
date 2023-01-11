package com.example.mvvmpostapi.acivity.loginActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mvvmpostapi.R
import com.example.mvvmpostapi.model.DefaultResponse
import com.example.mvvmpostapi.sharedpreferences.SharedpreferencesApi
import com.example.mvvmpostapi.utlis.Resource
import com.example.mvvmpostapi.utlis.ResponseCodeCheck
import com.example.mypostapi.Webservice.RetrofitClient
import com.example.mypostapi.Webservice.UserwebServiceApi
import com.example.mypostapi.emailPattern
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    private lateinit var sharedpre: SharedpreferencesApi

    @Inject
    lateinit var retrofit: Retrofit

    var emaillogin: String = ""
    var passwordlogin: String = ""

    var emailloginerro: String = ""
    var passwrodloginerro: String = ""

    var responseCodeCheckL: ResponseCodeCheck = ResponseCodeCheck()
    private var datamutableL: MutableLiveData<Resource<DefaultResponse>> = MutableLiveData()
    var dataLiveDataL: MutableLiveData<Resource<DefaultResponse>> = datamutableL

    fun validationlogin(): Boolean {

        emailloginerro = ""
        passwrodloginerro = ""


        if (emaillogin.isEmpty()) {
            emailloginerro = getApplication<Application>().getString(R.string.entereUsername)
            return false
        } else if (!emaillogin.matches(emailPattern.toRegex())) {
            emailloginerro = getApplication<Application>().getString(R.string.enterecorrect)
            return false
        } else if (passwordlogin.isEmpty()) {
            passwrodloginerro = getApplication<Application>().getString(R.string.enteremail)
            return false
        } else if (passwordlogin.length < 6) {
            passwordlogin = getApplication<Application>().getString(R.string.enterepassword6latter)
            return false
        }
        return true
    }

    fun loginuser() {
        val hashMap: HashMap<String, String> = HashMap()
        hashMap.apply {
            put("email", emaillogin)
            put("password", passwordlogin)
        }
        // below code for
        // val webserviceinterface=RetrofitClient.getRetrofit().create(UserwebServiceApi::class.java)

        val webserviceinterface = retrofit.create(UserwebServiceApi::class.java)
        val loginRepository = LoginRepository(webserviceinterface)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository: Response<DefaultResponse> = loginRepository.loginUser(hashMap)
                datamutableL.postValue(responseCodeCheckL.getResponseResult(repository))
            } catch (e: Exception) {
                datamutableL.postValue(Resource.error("fill the details", null))
            }
        }
        //  below code for Retrofit use RetrofitClient

        /* datamutableL.postValue(Resource.loading(null))
         RetrofitClient.getRetrofit().create(UserwebServiceApi::class.java).loginUser(hashMap)
             .enqueue(object : Callback<DefaultResponse?> {
                 override fun onResponse(
                     call: Call<DefaultResponse?>,
                     response: Response<DefaultResponse?>
                 ) {
                     if (response.body() !== null) {
                         try {
                             datamutableL.postValue(
                                 responseCodeCheckL.getResponseResult(
                                     Response.success(
                                         response.body()

                                     )
                                 )
                             )
                         } catch (e: Exception) {
                             datamutableL.postValue(Resource.error("fill the details", null))
                         }
                     }
                 }
                 override fun onFailure(call: Call<DefaultResponse?>, t: Throwable) {
                     datamutableL.postValue(Resource.error(t.message.toString(),null))
                     Toast.makeText(getApplication(), t.message, Toast.LENGTH_SHORT).show()
                 }
             })*/
    }
}
