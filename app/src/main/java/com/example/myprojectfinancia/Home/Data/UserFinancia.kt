package com.example.myprojectfinancia.Home.Data

import com.example.myprojectfinancia.Home.UI.home.Models.MovementsItem

data class UserFinancia(
    val uid: String,
    val name: String,
    val movements: List<MovementsItem> = emptyList()
) {
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "uid" to this.uid,
            "name" to this.name
        )
    }
}
