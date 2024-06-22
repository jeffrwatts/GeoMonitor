package com.jeffrwatts.geomonitor.ui.quakeevents

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.jeffrwatts.geomonitor.R
import com.jeffrwatts.geomonitor.ui.GeoMonitorTopAppBar
import com.jeffrwatts.geomonitor.ui.PermissionWrapper

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun QuakeEventsScreen(
    modifier: Modifier = Modifier,
    viewModel: QuakeEventsViewModel = hiltViewModel()
) {
    val topAppBarState = rememberTopAppBarState()

    // Permissions state for location and notifications
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect(key1 = permissionsState) {
        if (!permissionsState.allPermissionsGranted) {
            permissionsState.launchMultiplePermissionRequest()
        }
    }
    Scaffold(
        topBar = {
            GeoMonitorTopAppBar(
                title = stringResource(R.string.quake_events),
                topAppBarState = topAppBarState
            )
        },
        modifier = modifier
    ) { innerPadding ->
        PermissionWrapper(
            permissions = listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            ),
            rationaleMessage = stringResource(id = R.string.permission_rationale)
        ) {
            Column(modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()) {
                // Button row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { viewModel.subscribeToTopic("earthquake-alerts") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Subscribe to Earthquake Alerts")
                    }
                }
                // Additional content can go here
                Spacer(modifier = Modifier.height(8.dp))  // Adds some space below the button
                Text(
                    text = "More information will appear here.",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

        }
    }
}