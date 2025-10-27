package com.example.myprojectfinancia.Index.Plans.ModelsPlans

data class planItem(
    val id: String = "",
    val Name: String,
    val Category: String,
    val Description: String,
    val Objective: String,
    val Actualy: String,
    val Advice: String,
    val Date: String
) {
    fun countPlansCompleted(): Int {
        var amountPlansCompleted = 0
        if (Actualy.toDoubleOrNull() == Objective.toDoubleOrNull()) {
            amountPlansCompleted = +1
        }
        return amountPlansCompleted
    }
}