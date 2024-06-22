package com.jeffrwatts.geomonitor.network

import com.google.gson.annotations.SerializedName

data class USGSMonitoredVolcano(
    @SerializedName("vnum") val volcanoID: String?,
    @SerializedName("volcano_name") val volcanoName: String,
)