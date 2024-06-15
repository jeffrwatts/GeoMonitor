package com.jeffrwatts.geomonitor.ui.earthquakemap

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.jeffrwatts.geomonitor.R
import com.jeffrwatts.geomonitor.ui.GeoMonitorTopAppBar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun EarthQuakeMapScreen(
    modifier: Modifier = Modifier,
    viewModel: EarthQuakeMapViewModel = hiltViewModel()
) {
    val topAppBarState = rememberTopAppBarState()
    val earthquakes = viewModel.earthquakes.collectAsState().value

    // Permissions state for location and notifications
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    // Prepare and remember the initial camera position
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(19.8968, -155.5828), 10f) // Centered on the Big Island
    }

    // Properties for the Google Map
    val mapProperties = remember {
        MapProperties(
            isMyLocationEnabled = permissionsState.allPermissionsGranted,
            mapType = MapType.NORMAL
        )
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

        LaunchedEffect(key1 = permissionsState) {
            if (!permissionsState.allPermissionsGranted) {
                permissionsState.launchMultiplePermissionRequest()
            }
        }

        Column(modifier = contentModifier) {
            GoogleMap(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                properties = mapProperties,
                cameraPositionState = cameraPositionState
            ) {
                earthquakes.forEach { earthquake ->
                    Marker(
                        state = MarkerState(position = LatLng(earthquake.latitude, earthquake.longitude)),
                        title = earthquake.place,
                        snippet = "Magnitude: ${earthquake.magnitude}",
                        onClick = {
                            // Returns true to indicate that the event is consumed
                            true
                        }
                    )
                }
            }

            Button(
                onClick = { viewModel.refreshEarthquakes(true) },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Refresh Earthquake Data")
            }
        }
    }
}
