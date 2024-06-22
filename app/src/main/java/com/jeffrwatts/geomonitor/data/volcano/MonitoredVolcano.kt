package com.jeffrwatts.geomonitor.data.volcano

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monitored_volcanos")
data class MonitoredVolcano(
    @PrimaryKey val volcanoID: String,
    val volcanoName: String,
    val observationAbbr: String,
    val observationFullname: String,
    val latitude: Double,
    val longitude: Double,
    val elevation: Int,
)