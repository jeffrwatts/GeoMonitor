package com.jeffrwatts.geomonitor.network

import retrofit2.http.GET
import retrofit2.http.Path

interface USGSVolcanoApi {
    @GET("hans-public/api/volcano/getMonitoredVolcanoes")
    suspend fun getMonitoredVolcanoes(): List<USGSMonitoredVolcano>

    @GET("hans-public/api/volcano/getVolcano/{volcanoID}")
    suspend fun getVolcanoDetails(@Path("volcanoID") volcanoID: String): USGSVolcanoDetails
}