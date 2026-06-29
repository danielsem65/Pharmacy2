package com.semdev.pharma.inv.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MedicineDao {

    @Query("SELECT * FROM medicines ORDER BY name ASC")
    suspend fun getAll(): List<MedicineEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(medicines: List<MedicineEntity>)

    @Query("DELETE FROM medicines")
    suspend fun deleteAll()
}
