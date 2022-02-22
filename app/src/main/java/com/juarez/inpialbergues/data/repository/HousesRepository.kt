package com.juarez.inpialbergues.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.juarez.inpialbergues.data.db.HouseDao
import com.juarez.inpialbergues.data.models.House
import com.juarez.inpialbergues.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class HousesRepository @Inject constructor(
    firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val houseDao: HouseDao,
) {
    private val collectionRef = firestore.collection("houses")

    fun getHouses(): Flow<Resource<List<House>>> = flow {
        emit(Resource.Loading)
        val snapshot = collectionRef.get().await()
        val houses = snapshot.toObjects<House>()
        emit(Resource.Success(houses))
    }

    fun saveHouse(uri: Uri, fileName: String, newHouse: House): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        val url = saveImage(uri, fileName)
//        newHouse.id = UUID.randomUUID().toString()
//        newHouse.url = url
        val house = newHouse.copy(id = UUID.randomUUID().toString(), url = url)
        collectionRef.add(house).await()
        emit(Resource.Success(true))
    }

    private suspend fun saveImage(uri: Uri, fileName: String): String {
        val task = storage.reference.child("houses/${fileName}").putFile(uri).await()
        val url = task.storage.downloadUrl.await()
        return url.toString()
    }

    private suspend fun deleteImage(filename: String) {
        storage.reference.child(("images/${filename}")).delete().await()
    }

    fun updateHouse(house: House, fileName: String, uri: Uri?): Flow<Resource<Boolean>> =
        flow {
            emit(Resource.Loading)
            if (uri != null) {
                fileName.toString()
                // delete and upload new photo
            }
            val query = collectionRef.whereEqualTo("id", house.id).get().await()
            val uuid = query.documents[0].id
            collectionRef.document(uuid).set(house).await()
            emit(Resource.Success(true))
        }

}