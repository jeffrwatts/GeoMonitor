package com.jeffrwatts.geomonitor.ui

import androidx.navigation.NavController

object GeoMonitorDestinations {
    const val QUAKE_EVENTS_ROUTE = "quakeEvents"
    const val EARTH_QUAKE_MAP_ROUTE = "earthQuakeMap"
}

class GeoMonitorNavigationActions(navController: NavController) {
    val navigateToQuakeEvents: (currentRoute: String?) -> Unit = { currentRoute->
        if (currentRoute != GeoMonitorDestinations.QUAKE_EVENTS_ROUTE) {
            navController.navigate(GeoMonitorDestinations.QUAKE_EVENTS_ROUTE) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }
    val navigateToEarthQuakeMap: (currentRoute: String?) -> Unit = { currentRoute->
        if (currentRoute != GeoMonitorDestinations.EARTH_QUAKE_MAP_ROUTE) {
            navController.navigate(GeoMonitorDestinations.EARTH_QUAKE_MAP_ROUTE) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }
}