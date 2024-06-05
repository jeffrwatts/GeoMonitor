package com.jeffrwatts.geomonitor.network

import com.google.gson.annotations.SerializedName
import com.jeffrwatts.geomonitor.data.earthquakeevent.EarthQuakeEvent

data class USGSEarthquakeResponse(
    val type: String,
    val features: List<Feature>
)

data class Feature(
    val type: String,
    val properties: Properties,
    val geometry: Geometry,
    val id: String
)

data class Properties(
    val mag: Double,
    val place: String,
    val time: Long,
    val updated: Long,
    val url: String,
    val detail: String,
    val status: String,
    @SerializedName("tsunami") val tsunamiFlag: Int,
    val magType: String
)

data class Geometry(
    val type: String,
    val coordinates: List<Double>
)

fun Feature.toEarthquakeEvent(): EarthQuakeEvent {
    return EarthQuakeEvent(
        id = this.id,
        time = this.properties.time,
        place = this.properties.place,
        magnitude = this.properties.mag,
        magType = this.properties.magType,
        status = this.properties.status,
        latitude = this.geometry.coordinates[1], // Assuming the coordinates are [longitude, latitude, depth]
        longitude = this.geometry.coordinates[0],
        depth = this.geometry.coordinates[2],
        tsunami = this.properties.tsunamiFlag != 0, // Convert integer tsunami flag to Boolean
        alert = false // This needs clarification, as it's not provided directly in the properties
    )
}
