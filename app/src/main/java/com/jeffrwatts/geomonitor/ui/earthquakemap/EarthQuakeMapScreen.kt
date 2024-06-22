package com.jeffrwatts.geomonitor.ui.earthquakemap

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.jeffrwatts.geomonitor.R
import com.jeffrwatts.geomonitor.data.earthquakeevent.EarthQuakeEvent
import com.jeffrwatts.geomonitor.ui.GeoMonitorTopAppBar
import com.jeffrwatts.geomonitor.ui.PermissionWrapper
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun EarthQuakeMapScreen(
    modifier: Modifier = Modifier,
    viewModel: EarthQuakeMapViewModel = hiltViewModel()
) {
    val topAppBarState = rememberTopAppBarState()
    val earthquakes = viewModel.earthquakes.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val (selectedEarthquake, setSelectedEarthquake) = remember { mutableStateOf<EarthQuakeEvent?>(null) }

    // Permissions state for location and notifications
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS
        )
    )

    LaunchedEffect(key1 = permissionsState) {
        if (!permissionsState.allPermissionsGranted) {
            permissionsState.launchMultiplePermissionRequest()
        }
    }

    // Prepare and remember the initial camera position
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(19.8968, -155.5828), 10f) // Centered on the Big Island
    }

    Scaffold(
        topBar = {
            GeoMonitorTopAppBar(
                title = stringResource(R.string.quake_map),
                topAppBarState = topAppBarState
            )
        },
        modifier = modifier
    ) { innerPadding ->
        val contentModifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        PermissionWrapper(
            permissions = listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            ),
            rationaleMessage = stringResource(id = R.string.permission_rationale)
        ) {
            Column(modifier = contentModifier) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    EarthquakeMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        earthquakes = earthquakes,
                        onMapBoundsChanged = { viewModel.onMapBoundsChanged(it) },
                        onEarthQuakeClick = { setSelectedEarthquake(it) }
                    )

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            // Show the dialog if an earthquake is selected
            selectedEarthquake?.let {
                EarthquakeInfoDialog(earthquake = it, onDismiss = { setSelectedEarthquake(null) })
            }
        }
    }
}


@OptIn(FlowPreview::class)
@Composable
fun EarthquakeMap(
    modifier: Modifier,
    cameraPositionState: CameraPositionState,
    earthquakes: List<EarthQuakeEvent>,
    onMapBoundsChanged: (LatLngBounds) -> Unit,
    onEarthQuakeClick: (EarthQuakeEvent) -> Unit
) {
    val mapProperties = remember {
        MapProperties(isMyLocationEnabled = true, mapType = MapType.NORMAL)
    }

    GoogleMap(
        modifier = modifier,
        properties = mapProperties,
        cameraPositionState = cameraPositionState
    ) {
        earthquakes.forEach { earthquake ->
            val resourceId = when {
                earthquake.magnitude < 4.0 -> R.drawable.earthquake_minor
                earthquake.magnitude < 5.0 -> R.drawable.earthquake_moderate
                earthquake.magnitude < 6.0 -> R.drawable.earthquake_strong
                earthquake.magnitude < 7.0 -> R.drawable.earthquake_major
                earthquake.magnitude < 8.0 -> R.drawable.earthquake_great
                else -> R.drawable.earthquake_massive
            }
            val bitmapDescriptor = BitmapDescriptorFactory.fromResource(resourceId)

            Marker(
                state = MarkerState(position = LatLng(earthquake.latitude, earthquake.longitude)),
                icon = bitmapDescriptor,
                title = earthquake.place,
                snippet = "Magnitude: ${earthquake.magnitude}",
                onClick = { onEarthQuakeClick(earthquake); true }
            )
        }
    }

    LaunchedEffect(cameraPositionState) {
        snapshotFlow { cameraPositionState.position }
            .debounce(1000)
            .collect {
                val visibleRegion = cameraPositionState.projection?.visibleRegion
                visibleRegion?.latLngBounds?.let { bounds ->
                    Log.d("EarthQuakeMapScreen", "bounds = $bounds")
                    onMapBoundsChanged(bounds)
                }
            }
    }
}

@Composable
fun EarthquakeInfoDialog(
    earthquake: EarthQuakeEvent,
    onDismiss: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) }
    val formattedTime = remember { dateFormat.format(Date(earthquake.time)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = earthquake.place) },
        text = {
            Column {
                Text(text = "Magnitude: ${earthquake.magnitude}")
                Text(text = "Depth: ${earthquake.depth} km")
                Text(text = "Time: $formattedTime")
                // Add more details as needed
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}
