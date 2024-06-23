package com.jeffrwatts.geomonitor.data.volcano

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

enum class AlertLevel(val order: Int) {
    NORMAL(1),
    ADVISORY(2),
    WATCH(3),
    WARNING(4)
}

enum class ColorCode {
    GREEN,
    YELLOW,
    ORANGE,
    RED
}

class Converters {
    @TypeConverter
    fun fromAlertLevel(value: AlertLevel): String {
        return value.name
    }

    @TypeConverter
    fun toAlertLevel(value: String): AlertLevel {
        return AlertLevel.valueOf(value)
    }

    @TypeConverter
    fun fromColorCode(value: ColorCode): String {
        return value.name
    }

    @TypeConverter
    fun toColorCode(value: String): ColorCode {
        return ColorCode.valueOf(value)
    }
}

@Entity(tableName = "monitored_volcanos")
data class MonitoredVolcano(
    @PrimaryKey val volcanoID: String,
    val volcanoName: String,
    val observationAbbr: String,
    val observationFullname: String,
    val region: String,
    val latitude: Double,
    val longitude: Double,
    val elevation: Int,
    val alertLevel: AlertLevel,
    val colorCode: ColorCode,
    val updateUtc: Long,
    val imageUrl: String?,
    val description: String?
)