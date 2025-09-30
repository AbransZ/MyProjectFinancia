package com.example.myprojectfinancia.Data.API.StateUI

import com.example.myprojectfinancia.Data.API.network.DolarOficial

sealed class UiStateDolar {
    object isLoading : UiStateDolar()
    object neutral : UiStateDolar()
    data class error(val error: String) : UiStateDolar()
    data class success(val dolar: DolarOficial) : UiStateDolar()

}