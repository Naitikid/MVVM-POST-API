package com.example.mvvmpostapi.model

data class DefaultResponse (val status :Boolean, val message:String ,val data: UserData)


class UserData (val id:String, val name :String,val email:String,val profile_picture:String)




