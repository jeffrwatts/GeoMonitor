package com.jeffrwatts.geomonitor.ui.quakeevents

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jeffrwatts.geomonitor.R
import com.jeffrwatts.geomonitor.ui.GeoMonitorTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuakeEventsScreen(
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuakeEventsViewModel = hiltViewModel()
) {
    val topAppBarState = rememberTopAppBarState()

    Scaffold(
        topBar = {
            GeoMonitorTopAppBar(
                title = stringResource(R.string.quake_events),
                openDrawer = openDrawer,
                topAppBarState = topAppBarState
            )
        },
        modifier = modifier
    ) { innerPadding ->
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