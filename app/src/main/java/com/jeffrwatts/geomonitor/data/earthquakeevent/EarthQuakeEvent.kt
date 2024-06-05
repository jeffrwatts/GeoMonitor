package com.jeffrwatts.geomonitor.data.earthquakeevent

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "earthquake_events")
data class EarthQuakeEvent(
    @PrimaryKey val id: String,
    val time: Long,
    val place: String,
    val magnitude: Double,
    val magType: String,
    val status: String,
    val latitude: Double,
    val longitude: Double,
    val depth: Double,
    val tsunami: Boolean,
    val alert: Boolean
)