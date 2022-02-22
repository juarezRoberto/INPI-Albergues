package com.juarez.inpialbergues.ui.saveeditHouse

sealed class UpdateHouseState {
    data class Loading(val isLoading: Boolean) : UpdateHouseState()
    object Success : UpdateHouseState()
}