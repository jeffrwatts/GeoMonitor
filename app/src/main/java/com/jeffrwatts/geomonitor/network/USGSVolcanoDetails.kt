package com.jeffrwatts.geomonitor.network

import com.google.gson.annotations.SerializedName

data class USGSVolcanoDetails(
    @SerializedName("vnum") val volcanoID: String,
    @SerializedName("volcano_name") val volcanoName: String,
    @SerializedName("obs_abbr") val observationAbbr: String,
    @SerializedName("obs_fullname") val observationFullname: String,
    val latitude: Double,
    val longitude: Double,
    @SerializedName("elevation_meters") val elevation: Int,
)
