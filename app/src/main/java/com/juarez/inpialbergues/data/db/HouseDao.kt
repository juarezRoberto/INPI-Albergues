package com.juarez.inpialbergues.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juarez.inpialbergues.data.models.HouseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HouseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg houses: HouseEntity)

    @Query("SELECT * from houses_table")
    fun getAllHouses(): Flow<List<HouseEntity>>

    @Query("DELETE from houses_table")
    suspend fun deleteAllHouses()
}