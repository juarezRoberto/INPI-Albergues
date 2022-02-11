package com.juarez.inpialbergues.domain

import com.juarez.inpialbergues.data.models.House
import com.juarez.inpialbergues.data.repository.HousesRepository
import com.juarez.inpialbergues.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHousesUseCase @Inject constructor(private val repository: HousesRepository) {
    operator fun invoke(): Flow<Resource<List<House>>> = repository.getHouses()
}