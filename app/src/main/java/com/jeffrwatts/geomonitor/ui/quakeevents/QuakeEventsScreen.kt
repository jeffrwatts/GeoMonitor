package com.jeffrwatts.geomonitor.ui.quakeevents

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jeffrwatts.geomonitor.R
import com.jeffrwatts.geomonitor.ui.GeoMonitorTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuakeEventsScreen(
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
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
        val contentModifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        Text(
            text = "Quake Events Screen",
            modifier = contentModifier
        )
    }
}