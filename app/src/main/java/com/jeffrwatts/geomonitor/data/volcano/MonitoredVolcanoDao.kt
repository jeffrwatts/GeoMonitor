package com.jeffrwatts.geomonitor.data.volcano

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MonitoredVolcanoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(volcanoes: List<MonitoredVolcano>)

    @Query("UPDATE monitored_volcanos SET alertLevel = :alertLevel, colorCode = :colorCode, updateUtc = :updateUtc")
    suspend fun updateAllVolcanoStatus(alertLevel: AlertLevel, colorCode: ColorCode, updateUtc: Long)

    @Query("UPDATE monitored_volcanos SET alertLevel = :alertLevel, colorCode = :colorCode, updateUtc = :updateUtc WHERE volcanoID = :volcanoID")
    suspend fun updateVolcanoStatus(volcanoID: String, alertLevel: AlertLevel, colorCode: ColorCode, updateUtc: Long)

    @Query("SELECT * FROM monitored_volcanos")
    suspend fun getAllVolcanoes(): List<MonitoredVolcano>

    @Query("SELECT * FROM monitored_volcanos WHERE volcanoID = :volcanoID")
    suspend fun getVolcanoById(volcanoID: String): MonitoredVolcano?

    @Query("DELETE FROM monitored_volcanos")
    suspend fun deleteAll()
}