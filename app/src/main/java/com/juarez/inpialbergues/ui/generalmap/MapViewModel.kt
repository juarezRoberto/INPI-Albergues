package com.juarez.inpialbergues.ui.generalmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juarez.inpialbergues.domain.GetHousesUseCase
import com.juarez.inpialbergues.ui.houses.HousesState
import com.juarez.inpialbergues.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getHousesUseCase: GetHousesUseCase,
) : ViewModel() {

    private val _housesState = MutableStateFlow<HousesState>(HousesState.Empty)
    val housesState = _housesState.asStateFlow()

    fun getHouses() {
        getHousesUseCase().onEach {
            when (it) {
                is Resource.Loading -> _housesState.value = HousesState.Loading
                is Resource.Success -> _housesState.value = HousesState.Success(it.data)
                else -> Unit
            }
        }.catch { e ->
            _housesState.value = HousesState.Error(e.localizedMessage ?: "Unexpected error")
        }.launchIn(viewModelScope)
    }
}