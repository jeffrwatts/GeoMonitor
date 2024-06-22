package com.jeffrwatts.geomonitor.data.volcano

import com.jeffrwatts.geomonitor.network.USGSVolcanoApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MonitoredVolcanoRepository @Inject constructor(
    private val dao: MonitoredVolcanoDao,
    private val api: USGSVolcanoApi
) {
    fun getAllMonitoredVolcanoes(): Flow<List<MonitoredVolcano>> = flow {
        val volcanoes = dao.getAllVolcanoes()
        if (volcanoes.isEmpty()) {
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
                        latitude = details.latitude,
                        longitude = details.longitude,
                        elevation = details.elevation,
                    )
                } else {
                    null
                }
            }
            dao.insertAll(detailedVolcanoes)
            emit(detailedVolcanoes)
        } else {
            emit(volcanoes)
        }
    }

    fun getVolcanoById(volcanoID: String): Flow<MonitoredVolcano?> = flow {
        emit(dao.getVolcanoById(volcanoID))
    }
}
