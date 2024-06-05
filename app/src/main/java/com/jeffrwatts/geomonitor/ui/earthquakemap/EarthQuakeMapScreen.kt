package com.jeffrwatts.geomonitor.ui.earthquakemap

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jeffrwatts.geomonitor.R
import com.jeffrwatts.geomonitor.ui.GeoMonitorTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EarthQuakeMapScreen(
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EarthQuakeMapViewModel = hiltViewModel()
) {
    val topAppBarState = rememberTopAppBarState()

    Scaffold(
        topBar = {
            GeoMonitorTopAppBar(
                title = stringResource(R.string.quake_map),
                openDrawer = openDrawer,
                topAppBarState = topAppBarState
            )
        },
        modifier = modifier
    ) { innerPadding ->
        val contentModifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        Column(modifier = contentModifier
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
                    onClick = { viewModel.refreshEarthquakes(true) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Subscribe to Earthquake Alerts")
                }
            }
        }
    }
}