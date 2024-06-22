package com.jeffrwatts.geomonitor.ui.earthquakemap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLngBounds
import com.jeffrwatts.geomonitor.data.earthquakeevent.EarthQuakeEvent
import com.jeffrwatts.geomonitor.data.earthquakeevent.EarthquakeRepository
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
    private val repository: EarthquakeRepository
) : ViewModel() {

    private val _earthquakes = MutableStateFlow<List<EarthQuakeEvent>>(emptyList())
    val earthquakes: StateFlow<List<EarthQuakeEvent>> = _earthquakes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

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
                _earthquakes.value = earthquakeList
                _isLoading.value = false
            }
        }
    }
}
