package com.jeffrwatts.geomonitor.ui.quakeevents

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@HiltViewModel
class QuakeEventsViewModel @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging
) : ViewModel() {

    fun subscribeToTopic(topic: String) = viewModelScope.launch {
        try {
            firebaseMessaging.subscribeToTopic(topic).await()
            Log.d("QuakeEventsViewModel", "Subscribed")
        } catch (e: Exception) {
            Log.d("QuakeEventsViewModel", "Failed: ${e.message}")
        }
    }
}
