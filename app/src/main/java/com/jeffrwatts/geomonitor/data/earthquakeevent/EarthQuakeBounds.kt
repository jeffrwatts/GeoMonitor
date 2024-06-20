package com.jeffrwatts.geomonitor.data.earthquakeevent

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

data class EarthQuakeBounds(
    val minLatitude: Double,
    val maxLatitude: Double,
    val minLongitude: Double,
    val maxLongitude: Double,
    val latestEventTime: Long  // Timestamp of the latest earthquake event
)

fun convertToLatLngBounds(bounds: EarthQuakeBounds): LatLngBounds {
    val southwest = LatLng(bounds.minLatitude, bounds.minLongitude)
    val northeast = LatLng(bounds.maxLatitude, bounds.maxLongitude)
    return LatLngBounds(southwest, northeast)
}