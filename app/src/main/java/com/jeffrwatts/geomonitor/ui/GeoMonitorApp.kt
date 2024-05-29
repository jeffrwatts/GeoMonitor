package com.jeffrwatts.geomonitor.ui

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jeffrwatts.geomonitor.ui.theme.GeoMonitorTheme
import kotlinx.coroutines.launch

@Composable
fun GeoMonitorApp() {
    GeoMonitorTheme {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            GeoMonitorNavigationActions(navController)
        }

        val coroutineScope = rememberCoroutineScope()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: GeoMonitorDestinations.QUAKE_EVENTS_ROUTE

        val drawerState = rememberDrawerState(DrawerValue.Closed)

        ModalNavigationDrawer(
            drawerContent = {
                AppDrawer(
                    currentRoute = currentRoute,
                    navigateToQuakeEvents = navigationActions.navigateToQuakeEvents,
                    navigateToQuakeMap = navigationActions.navigateToQuakeMap,
                    closeDrawer = { coroutineScope.launch { drawerState.close() } }
                )
            },
            drawerState = drawerState
        ) {
            GeoMonitorNavGraph(
                navController = navController,
                openDrawer = { coroutineScope.launch { drawerState.open() } },
            )
        }
    }
}