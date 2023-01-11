package com.example.mvvmpostapi.acivity.homeActivity

import com.example.mvvmpostapi.model.DefaultResponse
import com.example.mypostapi.Webservice.UserwebServiceApi
import retrofit2.Response

class HomeRepository constructor(private val userwebServiceApi: UserwebServiceApi) {
    suspend fun profileUser(hashMap: HashMap<String,String>):Response<DefaultResponse>
    {
        return userwebServiceApi.profileUser(hashMap)
    }
}