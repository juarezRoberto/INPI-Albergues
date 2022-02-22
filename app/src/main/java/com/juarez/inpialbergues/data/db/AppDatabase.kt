package com.juarez.inpialbergues.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.juarez.inpialbergues.data.models.HouseEntity

@Database(entities = [HouseEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun houseDao(): HouseDao
}