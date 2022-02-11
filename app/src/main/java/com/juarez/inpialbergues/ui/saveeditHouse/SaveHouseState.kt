package com.juarez.inpialbergues.ui.saveeditHouse

sealed class SaveHouseState {
    data class Loading(val isLoading: Boolean) : SaveHouseState()
    object Success : SaveHouseState()
}