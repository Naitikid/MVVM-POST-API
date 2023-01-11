package com.example.mvvmpostapi.acivity.signupActivity

import com.example.mvvmpostapi.model.DefaultResponse
import com.example.mypostapi.Webservice.UserwebServiceApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class SignUpRepository constructor(private val userwebServiceApi: UserwebServiceApi) {
    suspend fun registerUser(image: MultipartBody.Part, hashMap: HashMap<String,RequestBody>):Response<DefaultResponse>
    {
        return userwebServiceApi.registerUser(image,hashMap)
    }
}