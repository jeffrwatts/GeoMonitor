package com.jeffrwatts.geomonitor.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jeffrwatts.geomonitor.ui.quakeevents.QuakeEventsScreen
import com.jeffrwatts.geomonitor.ui.quakemap.QuakeMapScreen

@Composable
fun GeoMonitorNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    openDrawer: () -> Unit = {},
    startDestination: String = GeoMonitorDestinations.QUAKE_EVENTS_ROUTE,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = GeoMonitorDestinations.QUAKE_EVENTS_ROUTE) {
            //val actions = remember(navController) { GeoMonitorNavigationActions(navController) }
            QuakeEventsScreen(openDrawer = openDrawer, modifier)
        }
        composable(route = GeoMonitorDestinations.QUAKE_MAP_ROUTE) {
            //val actions = remember(navController) { GeoMonitorNavigationActions(navController) }
            QuakeMapScreen(openDrawer = openDrawer, modifier)
        }
    }
}