package com.jeffrwatts.geomonitor.data.earthquakeevent

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.jeffrwatts.geomonitor.network.USGSEarthquakeApi
import com.jeffrwatts.geomonitor.network.toEarthquakeEvent
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.TimeUnit

class EarthquakeRepository @Inject constructor(
    private val dao: EarthquakeEventDao,
    private val usgsApi: USGSEarthquakeApi
) {
    private var cachedLatLngBounds: LatLngBounds = LatLngBounds(LatLng(0.0, 0.0), LatLng(0.0, 0.0))
    private var cachedTime: Long = 0

    private fun containsEntirely(outerBounds: LatLngBounds, innerBounds: LatLngBounds): Boolean {
        return outerBounds.contains(innerBounds.northeast) && outerBounds.contains(innerBounds.southwest)
    }

    fun getEarthQuakeEvents(bounds: LatLngBounds, startTime: String, endTime: String): Flow<List<EarthQuakeEvent>> = flow {
        val currentTime = System.currentTimeMillis()
        val useCached = containsEntirely(cachedLatLngBounds, bounds) &&
                currentTime - cachedTime <= TimeUnit.HOURS.toMillis(1)

        if (!useCached) {
            val expandedBounds = expandBounds(bounds, 1.0)
            val success = fetchEarthquakes(expandedBounds, startTime, endTime)
            if (success) {
                cachedLatLngBounds = expandedBounds
                cachedTime = currentTime
            }
        }

        emit(dao.getAllEvents())
    }.flowOn(Dispatchers.IO)

    private fun expandBounds(bounds: LatLngBounds, expandFactor: Double): LatLngBounds {
        val latSpan = bounds.northeast.latitude - bounds.southwest.latitude
        val lngSpan = bounds.northeast.longitude - bounds.southwest.longitude

        val newSouthwest = LatLng(
            bounds.southwest.latitude - latSpan * expandFactor,
            bounds.southwest.longitude - lngSpan * expandFactor
        )
        val newNortheast = LatLng(
            bounds.northeast.latitude + latSpan * expandFactor,
            bounds.northeast.longitude + lngSpan * expandFactor
        )

        return LatLngBounds(newSouthwest, newNortheast)
    }

    private suspend fun fetchEarthquakes(bounds: LatLngBounds, startTime: String, endTime: String): Boolean {
        val minLatitude = bounds.southwest.latitude
        val maxLatitude = bounds.northeast.latitude
        val minLongitude = bounds.southwest.longitude
        val maxLongitude = bounds.northeast.longitude

        return try {
            val earthquakeResponse = usgsApi.getEarthquakesByBounds(
                format = "geojson",
                startTime = startTime,
                endTime = endTime,
                minMagnitude = 1.5,
                minLatitude = minLatitude,
                maxLatitude = maxLatitude,
                minLongitude = minLongitude,
                maxLongitude = maxLongitude
            )
            val events = earthquakeResponse.features.map { it.toEarthquakeEvent() }
            dao.insertAll(events)
            true // Return true when data is successfully fetched and stored
        } catch (e: Exception) {
            e.printStackTrace() // Log the error
            false // Return false in case of an exception
        }
    }
}
