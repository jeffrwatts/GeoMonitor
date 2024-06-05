package com.jeffrwatts.geomonitor.data.earthquakeevent

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EarthquakeEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<EarthQuakeEvent>)

    // Retrieve all earthquake events
    @Query("SELECT * FROM earthquake_events")
    suspend fun getAllEvents(): List<EarthQuakeEvent>
}
