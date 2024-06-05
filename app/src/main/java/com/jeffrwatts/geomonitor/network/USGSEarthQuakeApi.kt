package com.jeffrwatts.geomonitor.network

import retrofit2.http.GET
import retrofit2.http.Query

interface USGSEarthquakeApi {
    @GET("query")
    suspend fun getEarthquakes(
        @Query("format") format: String = "geojson",
        @Query("starttime") startTime: String,
        @Query("endtime") endTime: String,
        @Query("minmagnitude") minMagnitude: Double,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("maxradiuskm") maxRadiusKm: Int
    ): USGSEarthquakeResponse
}
