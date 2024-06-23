package com.jeffrwatts.geomonitor.network

import com.google.gson.annotations.SerializedName


data class USGSMonitoredVolcano(
    @SerializedName("vnum") val volcanoID: String?,
    @SerializedName("volcano_name") val volcanoName: String,
)

data class USGSVolcanoDetails(
    @SerializedName("vnum") val volcanoID: String,
    @SerializedName("volcano_name") val volcanoName: String,
    @SerializedName("obs_abbr") val observationAbbr: String,
    @SerializedName("obs_fullname") val observationFullname: String,
    val region: String,
    val latitude: Double,
    val longitude: Double,
    @SerializedName("elevation_meters") val elevation: Int,
    @SerializedName("volcano_image_url") val imageUrl: String?,
    @SerializedName("boilerplate") val description: String?
)

data class USGSVolcanoesElevated (
    @SerializedName("vnum") val volcanoID: String,
    @SerializedName("volcano_name") val volcanoName: String,
    @SerializedName("obs_abbr") val observationAbbr: String,
    @SerializedName("obs_fullname") val observationFullname: String,
    @SerializedName("color_code") val colorCode: String,
    @SerializedName("alert_level") val alertLevel: String,
    @SerializedName("sent_utc") val elevatedTime: String
)
