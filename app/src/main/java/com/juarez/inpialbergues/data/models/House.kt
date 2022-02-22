package com.juarez.inpialbergues.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class House(
    var id: String = "",
    val name: String = "",
    var url: String = "",
    val address: String = "",
    val latitude: String = "",
    val longitude: String = "",
) : Parcelable

@Entity(tableName = "houses_table")
data class HouseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val latitude: String = "",
    val longitude: String = "",
)

//fun House.toEntity(): HouseEntity {
//    return HouseEntity(
//        id = id,
//        name = name,
//        latitude = latitude,
//        longitude = longitude
//    )
//}