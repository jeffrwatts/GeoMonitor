package com.jeffrwatts.geomonitor

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jeffrwatts.geomonitor.data.earthquakeevent.EarthQuakeEvent
import com.jeffrwatts.geomonitor.data.earthquakeevent.EarthquakeEventDao
import com.jeffrwatts.geomonitor.data.volcano.MonitoredVolcano
import com.jeffrwatts.geomonitor.data.volcano.MonitoredVolcanoDao

@Database(entities = [
    EarthQuakeEvent::class,
    MonitoredVolcano::class], version = 1, exportSchema = false)
abstract class GeoServiceDatabase : RoomDatabase() {
    abstract fun earthQuakeEventDao(): EarthquakeEventDao
    abstract fun monitoredVolcanoDao(): MonitoredVolcanoDao


    companion object {
        @Volatile
        private var Instance: GeoServiceDatabase? = null

        fun getDatabase(context: Context): GeoServiceDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context = context, GeoServiceDatabase::class.java, "database")
                    .build()
                    .also {
                        Instance = it
                    }
            }
        }
    }
}