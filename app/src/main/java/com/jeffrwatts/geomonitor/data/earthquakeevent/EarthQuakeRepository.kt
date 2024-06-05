package com.jeffrwatts.geomonitor.data.earthquakeevent

import com.jeffrwatts.geomonitor.network.USGSEarthquakeApi
import com.jeffrwatts.geomonitor.network.toEarthquakeEvent
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class EarthquakeRepository @Inject constructor(
    private val dao: EarthquakeEventDao,
    private val usgsApi: USGSEarthquakeApi
) {
    // Get earthquake events with the option to force refresh from the API
    fun getEarthquakeEvents(forceRefresh: Boolean): Flow<List<EarthQuakeEvent>> = flow {
        if (forceRefresh) {
            val success = fetchAndStoreEarthquakes()
            if (success) {
                emit(dao.getAllEvents()) // Emit fresh data from the database
            } else {
                // Handle error appropriately or emit local data
                emit(dao.getAllEvents()) // This can be adjusted based on your error handling strategy
            }
        } else {
            emit(dao.getAllEvents()) // Emit existing data from the database
        }
    }.flowOn(Dispatchers.IO) // Operations performed on IO dispatcher

    // Helper method to fetch data from the API and store it in the Room database
    private suspend fun fetchAndStoreEarthquakes(): Boolean {
        try {
            val earthquakeResponse = usgsApi.getEarthquakes(
                format = "geojson",
                startTime = "2024-06-01",
                endTime = "2024-06-30",
                minMagnitude = 1.5,
                latitude = 19.5429,
                longitude = -155.6659,
                maxRadiusKm = 100
            )
            val events = earthquakeResponse.features.map { it.toEarthquakeEvent() }
            dao.insertAll(events)
            return true // Return true when data is successfully fetched and stored
        } catch (e: Exception) {
            e.printStackTrace() // Log the error
            return false // Return false in case of an exception
        }
    }
}
