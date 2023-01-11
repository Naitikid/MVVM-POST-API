package com.example.mypostapi.Webservice

import com.example.mvvmpostapi.model.DefaultResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface UserwebServiceApi{

 //   below is simple way
 /*   @FormUrlEncoded
    @POST("register.php")
    fun registerUser(@FieldMap hashMap: HashMap<String,String>
    ): Call<DefaultResponse>*/

    // below is Multipart Ways
   /* @Multipart
    @POST("register.php")
    fun registerUser(
        @Part image: MultipartBody.Part,
        @PartMap hashMap: HashMap<String, RequestBody>
    ):Call<DefaultResponse>*/

    // below code for Coroutine ways

    @Multipart
    @POST("register.php")
    suspend fun registerUser(
        @Part image: MultipartBody.Part,
        @PartMap hashMap: HashMap<String, RequestBody>
    ):Response<DefaultResponse>


    // below is Coroutine ways

    @FormUrlEncoded
    @POST("login.php")
    suspend fun loginUser(@FieldMap hashMap: HashMap<String, String>):Response<DefaultResponse>

    // below in simple way hashMap

    /*@FormUrlEncoded
    @POST("login.php")
    fun loginUser(@FieldMap hashMap: HashMap<String,String>
    ):Call<DefaultResponse>*/

    // below in simple way hash Map

    /*@FormUrlEncoded
    @POST("profile.php")
    fun profileUser(
          @FieldMap hashMap: HashMap<String,String>
    @Field("user_id")user_id:String
    ):Call<DefaultResponse>*/

    // below is Coroutine ways

    @FormUrlEncoded
    @POST("profile.php")
    suspend fun profileUser(@FieldMap hashMap: HashMap<String, String>):Response<DefaultResponse>
}

// belew is Simple way not used hashmap

/*

interface UserwebServiceApi {
    @FormUrlEncoded
    @POST("register.php")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<DefaultResponse>



    @FormUrlEncoded
    @POST("login.php")
    fun loginUser(

        @Field("id") id: Int,

        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("profile.php")
    fun profileuser(
        @Field("user_id") user_id: String
    ): Call<ProfileResponse>
}
*/
