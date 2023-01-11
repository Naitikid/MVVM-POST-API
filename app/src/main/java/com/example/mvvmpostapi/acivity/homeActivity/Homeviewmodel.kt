package com.example.mvvmpostapi.acivity.homeActivity

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mvvmpostapi.model.DefaultResponse
import com.example.mvvmpostapi.sharedpreferences.SharedpreferencesApi
import com.example.mvvmpostapi.utlis.Resource
import com.example.mvvmpostapi.utlis.ResponseCodeCheck
import com.example.mypostapi.Webservice.RetrofitClient
import com.example.mypostapi.Webservice.UserwebServiceApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class Homeviewmodel @Inject constructor(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var retrofit: Retrofit
    lateinit var sharedpre: SharedpreferencesApi
    lateinit var hid: String

    var userid: String = ""
    var emailidhome: String = ""
    var usernamehome: String = " "

    var responseCodeCheckH: ResponseCodeCheck = ResponseCodeCheck()
    private var datamutableH: MutableLiveData<Resource<DefaultResponse>> = MutableLiveData()
    var dataLiveDataH: MutableLiveData<Resource<DefaultResponse>> = datamutableH


    fun profileuser() {
        sharedpre = SharedpreferencesApi(getApplication())
        hid = sharedpre.getPrefString("id", "").toString()
        Log.e("TAG-for ID for Home", "onCreate: " + hid)
        val hashMap: HashMap<String, String> = HashMap()
        hashMap.apply {
            put("user_id", hid)
        }

        //     val webserviceinterface=RetrofitClient.getRetrofit().create(UserwebServiceApi::class.java)
        val webserviceinterface = retrofit.create(UserwebServiceApi::class.java)
        val homeRepository = HomeRepository(webserviceinterface)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository: Response<DefaultResponse> = homeRepository.profileUser(hashMap)
                datamutableH.postValue(responseCodeCheckH.getResponseResult(repository))
            } catch (e: Exception) {
                datamutableH.postValue(Resource.error("fill the details", null))
            }
        }
        /* RetrofitClient.getRetrofit().create(UserwebServiceApi::class.java).profileUser(hid)
             .enqueue(object : Callback<DefaultResponse?> {
                 override fun onResponse(
                     call: Call<DefaultResponse?>,
                     response: Response<DefaultResponse?>
                 ) {

                     Log.e("TAG", "onResponse:$hid ", )
                     if (response.body() !== null) {
                         try {
                             datamutableH.postValue(
                                 responseCodeCheckH.getResponseResult(
                                     Response.success(
                                         response.body()
                                     )
                                 )
                             )
                         } catch (e: Exception) {
                             datamutableH.postValue(Resource.error("fill the details", null))
                         }
                     }
                 }
                 override fun onFailure(call: Call<DefaultResponse?>, t: Throwable) {
                     Toast.makeText(getApplication(), t.message, Toast.LENGTH_SHORT).show()
                 }
             })*/
    }
}
