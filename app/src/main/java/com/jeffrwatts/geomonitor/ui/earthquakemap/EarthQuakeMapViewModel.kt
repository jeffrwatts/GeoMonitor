package com.jeffrwatts.geomonitor.ui.earthquakemap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeffrwatts.geomonitor.data.earthquakeevent.EarthQuakeEvent
import com.jeffrwatts.geomonitor.data.earthquakeevent.EarthquakeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EarthQuakeMapViewModel @Inject constructor(
    private val repository: EarthquakeRepository
) : ViewModel() {

    private val _earthquakes = MutableStateFlow<List<EarthQuakeEvent>>(emptyList())
    val earthquakes: StateFlow<List<EarthQuakeEvent>> = _earthquakes

    init {
        refreshEarthquakes(forceRefresh = false)
    }

    // Function to refresh earthquake data
    fun refreshEarthquakes(forceRefresh: Boolean) {
        viewModelScope.launch {
            repository.getEarthquakeEvents(forceRefresh).collect { earthquakeList ->
                _earthquakes.value = earthquakeList
            }
        }
    }
}
