package com.example.myprojectfinancia.Index.Data

import com.example.myprojectfinancia.Index.home.Models.Movements.MovementsItemSave

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
