package com.jeffrwatts.geomonitor.ui.earthquakemap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLngBounds
import com.jeffrwatts.geomonitor.data.earthquakeevent.EarthQuakeEvent
import com.jeffrwatts.geomonitor.data.earthquakeevent.EarthquakeRepository
import com.jeffrwatts.geomonitor.data.volcano.MonitoredVolcano
import com.jeffrwatts.geomonitor.data.volcano.MonitoredVolcanoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject


@HiltViewModel
class EarthQuakeMapViewModel @Inject constructor(
    private val repository: EarthquakeRepository,
    private val volcanoRepository: MonitoredVolcanoRepository
) : ViewModel() {

    private val _earthquakes = MutableStateFlow<List<EarthQuakeEvent>>(emptyList())
    val earthquakes: StateFlow<List<EarthQuakeEvent>> = _earthquakes

    private val _volcanoes = MutableStateFlow<List<MonitoredVolcano>>(emptyList())
    val volcanoes: StateFlow<List<MonitoredVolcano>> = _volcanoes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadMonitoredVolcanoes()
    }

    private fun loadMonitoredVolcanoes() {
        viewModelScope.launch {
            _isLoading.value = true
            volcanoRepository.getAllMonitoredVolcanoes().collect { volcanoList ->
                _volcanoes.value = sortVolcanoes(volcanoList)
                _isLoading.value = false
            }
        }
    }

    fun onMapBoundsChanged(bounds: LatLngBounds) {
        viewModelScope.launch {
            _isLoading.value = true
            val now = Instant.now()
            val yesterday = now.minus(24, ChronoUnit.HOURS)

            // Formatter for ISO 8601
            val formatter = DateTimeFormatter.ISO_INSTANT

            // Format yesterday's Instant to ISO 8601 string
            val startTime = formatter.format(yesterday)
            val endTime = formatter.format(now)

            repository.getEarthQuakeEvents(bounds, startTime, endTime).collect { earthquakeList ->
                _earthquakes.value = sortEarthquakes(earthquakeList)
                _isLoading.value = false
            }
        }
    }

    // Sorting functions
    private fun sortVolcanoes(volcanoes: List<MonitoredVolcano>): List<MonitoredVolcano> {
        return volcanoes.sortedBy { it.alertLevel.order }
    }

    private fun sortEarthquakes(earthquakes: List<EarthQuakeEvent>): List<EarthQuakeEvent> {
        return earthquakes.sortedBy { it.magnitude }
    }
}
