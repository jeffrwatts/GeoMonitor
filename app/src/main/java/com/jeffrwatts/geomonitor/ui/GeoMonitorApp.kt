package com.jeffrwatts.geomonitor.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jeffrwatts.geomonitor.R
import com.jeffrwatts.geomonitor.ui.theme.GeoMonitorTheme

@Composable
fun GeoMonitorApp() {
    GeoMonitorTheme {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            GeoMonitorNavigationActions(navController)
        }

        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    NavigationBarItem(
                        label = { Text(stringResource(id = R.string.quake_events)) },
                        icon = { Icon(Icons.Filled.Radar, null) },
                        selected = currentRoute == GeoMonitorDestinations.QUAKE_EVENTS_ROUTE,
                        onClick = { navigationActions.navigateToQuakeEvents(currentRoute) }
                    )
                    NavigationBarItem(
                        label = { Text(stringResource(id = R.string.quake_map)) },
                        icon = { Icon(Icons.Filled.Map, null) },
                        selected = currentRoute == GeoMonitorDestinations.EARTH_QUAKE_MAP_ROUTE,
                        onClick = { navigationActions.navigateToEarthQuakeMap(currentRoute) }
                    )
                }
            }
        ) { innerPadding ->
            GeoMonitorNavGraph(navController = navController, modifier = Modifier.padding(innerPadding))
        }
    }
}