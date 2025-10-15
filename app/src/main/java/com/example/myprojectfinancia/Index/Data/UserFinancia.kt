package com.example.myprojectfinancia.Index.Data

import com.example.myprojectfinancia.Index.Plans.ModelsPlans.planItem
import com.example.myprojectfinancia.Index.home.Models.Movements.MovementsItemSave
import java.util.Date

data class UserFinancia(
    val uid: String,
    val name: String,
    val resgistrationDate: Date = Date(System.currentTimeMillis()),
    val movements: List<MovementsItemSave> = emptyList(),
    val plans: List<planItem> = emptyList()
)
