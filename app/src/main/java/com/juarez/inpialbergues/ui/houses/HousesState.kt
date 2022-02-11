package com.juarez.inpialbergues.ui.houses

import com.juarez.inpialbergues.models.House

sealed class HousesState {
    object Loading : HousesState()
    object Empty : HousesState()
    data class Error(val message: String) : HousesState()
    data class Success(val data: List<House>) : HousesState()
}