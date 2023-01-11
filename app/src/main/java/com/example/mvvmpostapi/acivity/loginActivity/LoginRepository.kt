package com.example.mvvmpostapi.acivity.loginActivity

import com.example.mvvmpostapi.model.DefaultResponse
import com.example.mypostapi.Webservice.UserwebServiceApi
import retrofit2.Response

class LoginRepository constructor(private val userwebServiceApi: UserwebServiceApi) {
    suspend fun  loginUser(hashMap: HashMap<String,String>):Response<DefaultResponse>
    {
        return userwebServiceApi.loginUser(hashMap)
    }
}