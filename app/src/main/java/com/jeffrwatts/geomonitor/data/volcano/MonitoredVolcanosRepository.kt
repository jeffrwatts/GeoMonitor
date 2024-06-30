package com.jeffrwatts.geomonitor.data.volcano

import com.jeffrwatts.geomonitor.network.USGSVolcanoApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MonitoredVolcanoRepository @Inject constructor(
    private val dao: MonitoredVolcanoDao,
    private val api: USGSVolcanoApi
) {
    private var cachedTime: Long = 0

    fun getAllMonitoredVolcanoes(): Flow<List<MonitoredVolcano>> = flow {
        var volcanoes = dao.getAllVolcanoes()

        // Populate for the first time if necessary.
        if (volcanoes.isEmpty()) {
            volcanoes = getVolcanoesWithDetail()
            dao.insertAll(volcanoes)
        }

        val useCache = (System.currentTimeMillis() - cachedTime <= TimeUnit.HOURS.toMillis(1))
        if (!useCache) {
            updateVolcanoStatus()
            cachedTime = System.currentTimeMillis()
            volcanoes = dao.getAllVolcanoes()
        }

        emit(volcanoes)
    }

    private suspend fun getVolcanoesWithDetail(): List<MonitoredVolcano> {
        val currentTime = Instant.now().toEpochMilli()
        val monitoredVolcanoes = api.getMonitoredVolcanoes()

        val detailedVolcanoes = monitoredVolcanoes.mapNotNull { monitoredVolcano ->
            val volcanoID = monitoredVolcano.volcanoID
            if (volcanoID != null) {
                val details = api.getVolcanoDetails(volcanoID)
                MonitoredVolcano(
                    volcanoID = details.volcanoID,
                    volcanoName = details.volcanoName,
                    observationAbbr = details.observationAbbr,
                    observationFullname = details.observationFullname,
                    region = details.region,
                    latitude = details.latitude,
                    longitude = details.longitude,
                    elevation = details.elevation,
                    alertLevel = AlertLevel.NORMAL,
                    colorCode = ColorCode.GREEN,
                    updateUtc = currentTime,
                    imageUrl = details.imageUrl,
                    description = details.description
                )
            } else {
                null
            }
        }
        return detailedVolcanoes
    }

    private suspend fun updateVolcanoStatus() {
        val currentTime = Instant.now().toEpochMilli()
        dao.updateAllVolcanoStatus(AlertLevel.NORMAL, ColorCode.GREEN, currentTime)

        val elevatedVolcanoes = api.getElevatedVolcanoes()
        elevatedVolcanoes.forEach { elevated ->
            dao.updateVolcanoStatus(elevated.volcanoID,
                AlertLevel.valueOf(elevated.alertLevel),
                ColorCode.valueOf(elevated.colorCode),
                convertToEpoch(elevated.elevatedTime))
        }
    }

    private fun convertToEpoch(timeString: String): Long {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val localDateTime = LocalDateTime.parse(timeString, formatter)
        return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
    }

    fun getVolcanoById(volcanoID: String): Flow<MonitoredVolcano?> = flow {
        emit(dao.getVolcanoById(volcanoID))
    }
}
