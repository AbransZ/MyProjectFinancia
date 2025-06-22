package com.example.myprojectfinancia.Home.Data

data class UserFinancia(
    val uid:String,
    val name:String
){
    fun toMap():MutableMap<String,Any>{
        return mutableMapOf(
            "uid" to this.uid,
            "name" to this.name
        )
    }
}
