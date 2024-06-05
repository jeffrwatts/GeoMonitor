package com.jeffrwatts.geomonitor.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

object GeoMonitorDestinations {
    const val QUAKE_EVENTS_ROUTE = "quakeEvents"
    const val EARTH_QUAKE_MAP_ROUTE = "earthQuakeMap"
}

class GeoMonitorNavigationActions(navController: NavController) {
    val navigateToQuakeEvents: () -> Unit = {
        navController.navigate(GeoMonitorDestinations.QUAKE_EVENTS_ROUTE) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
    val navigateToEarthQuakeMap: () -> Unit = {
        navController.navigate(GeoMonitorDestinations.EARTH_QUAKE_MAP_ROUTE) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}