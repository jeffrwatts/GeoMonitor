package com.jeffrwatts.geomonitor.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jeffrwatts.geomonitor.ui.earthquakemap.EarthQuakeMapScreen
import com.jeffrwatts.geomonitor.ui.quakeevents.QuakeEventsScreen

@Composable
fun GeoMonitorNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = GeoMonitorDestinations.EARTH_QUAKE_MAP_ROUTE,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = GeoMonitorDestinations.QUAKE_EVENTS_ROUTE) {
            //val actions = remember(navController) { GeoMonitorNavigationActions(navController) }
            QuakeEventsScreen()
        }
        composable(route = GeoMonitorDestinations.EARTH_QUAKE_MAP_ROUTE) {
            //val actions = remember(navController) { GeoMonitorNavigationActions(navController) }
            EarthQuakeMapScreen()
        }
    }
}