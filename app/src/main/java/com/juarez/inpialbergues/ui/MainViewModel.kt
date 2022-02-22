package com.juarez.inpialbergues.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juarez.inpialbergues.data.models.House
import com.juarez.inpialbergues.domain.GetHousesUseCase
import com.juarez.inpialbergues.domain.SaveHouseUseCase
import com.juarez.inpialbergues.domain.UpdateHouseUseCase
import com.juarez.inpialbergues.ui.houses.HousesState
import com.juarez.inpialbergues.ui.saveeditHouse.SaveHouseState
import com.juarez.inpialbergues.ui.saveeditHouse.UpdateHouseState
import com.juarez.inpialbergues.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveHouseUseCase: SaveHouseUseCase,
    private val getHousesUseCase: GetHousesUseCase,
    private val updateHouseUseCase: UpdateHouseUseCase,
) : ViewModel() {

    private val _housesState = MutableStateFlow<HousesState>(HousesState.Empty)
    val housesState = _housesState.asStateFlow()
    private val _saveHouseState = MutableStateFlow<SaveHouseState>(SaveHouseState.Loading(false))
    val saveHouseState = _saveHouseState.asStateFlow()
    private val _updateHouseState =
        MutableStateFlow<UpdateHouseState>(UpdateHouseState.Loading(false))
    val updateHouseState = _updateHouseState.asStateFlow()

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

    private fun getFileName(extension: String?): String {
        val dateFormatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val dateFileName = dateFormatter.format(Date())
        return "${dateFileName}.${extension ?: "png"}"
    }

    fun saveHouse(newHouse: House, uri: Uri, extension: String?) {
        val fileName = getFileName(extension)
        saveHouseUseCase(uri, fileName, newHouse).onEach {
            when (it) {
                is Resource.Loading -> {
                    _saveHouseState.value = SaveHouseState.Loading(true)
                }
                is Resource.Success -> {
                    _saveHouseState.value = SaveHouseState.Loading(false)
                    _saveHouseState.value = SaveHouseState.Success
                }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    fun updateHouse(house: House, uri: Uri?, extension: String?) {
        val fileName = getFileName(extension)
        updateHouseUseCase(house, fileName, uri).onEach {
            when (it) {
                is Resource.Loading -> _updateHouseState.value = UpdateHouseState.Loading(true)
                is Resource.Success -> {
                    _updateHouseState.value = UpdateHouseState.Loading(false)
                    _updateHouseState.value = UpdateHouseState.Success
                }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }
}