package com.example.myprojectfinancia.Home.Data

import com.example.myprojectfinancia.Home.UI.home.Models.Movements.MovementsItemSave

data class UserFinancia(
    val uid: String,
    val name: String,
    val movements: List<MovementsItemSave> = emptyList()
) {
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "uid" to this.uid,
            "name" to this.name
        )
    }
}
