package com.juarez.inpialbergues.domain

import android.net.Uri
import com.juarez.inpialbergues.data.models.House
import com.juarez.inpialbergues.data.repository.HousesRepository
import com.juarez.inpialbergues.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveHouseUseCase @Inject constructor(private val repository: HousesRepository) {
    operator fun invoke(uri: Uri, fileName: String, newHouse: House): Flow<Resource<Boolean>> {
        return repository.saveHouse(uri, fileName, newHouse)
    }
}