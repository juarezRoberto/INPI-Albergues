package com.juarez.inpialbergues.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.juarez.inpialbergues.data.models.House
import com.juarez.inpialbergues.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HousesRepository @Inject constructor(
    firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
) {
    private val collectionRef = firestore.collection("houses")

    fun getHouses(): Flow<Resource<List<House>>> = flow {
        emit(Resource.Loading)
        val snapshot = collectionRef.get().await()
        val houses = snapshot.toObjects<House>()
        val housesMapped = houses.mapIndexed { index, house ->
            House(id = index + 1,
                name = house.name,
                url = house.url,
                address = house.address
            )
        }
        emit(Resource.Success(housesMapped))
    }

    fun saveHouse(uri: Uri, fileName: String, newHouse: House): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        val task = storage.reference.child("houses/${fileName}").putFile(uri).await()
        val url = task.storage.downloadUrl.await()
        newHouse.url = url.toString()
        collectionRef.add(newHouse).await()
        emit(Resource.Success(true))
    }
}