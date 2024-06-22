package com.jeffrwatts.geomonitor.data.volcano

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MonitoredVolcanoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(volcanoes: List<MonitoredVolcano>)

    @Query("SELECT * FROM monitored_volcanos")
    suspend fun getAllVolcanoes(): List<MonitoredVolcano>

    @Query("SELECT * FROM monitored_volcanos WHERE volcanoID = :volcanoID")
    suspend fun getVolcanoById(volcanoID: String): MonitoredVolcano?

    @Query("DELETE FROM monitored_volcanos")
    suspend fun deleteAll()
}