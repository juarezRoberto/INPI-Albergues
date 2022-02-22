package com.juarez.inpialbergues.domain

import android.net.Uri
import com.juarez.inpialbergues.data.models.House
import com.juarez.inpialbergues.data.repository.HousesRepository
import com.juarez.inpialbergues.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateHouseUseCase @Inject constructor(private val repository: HousesRepository) {
    operator fun invoke(house: House, fileName: String, uri: Uri?): Flow<Resource<Boolean>> {
        return repository.updateHouse(house, fileName, uri)
    }
}