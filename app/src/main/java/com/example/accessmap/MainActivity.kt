package com.example.accessmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.FilterList
//import androidx.compose.material3.*
import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.core.content.res.ResourcesCompat
//import androidx.navigation.NavController
//import androidx.navigation.compose.*
import com.google.android.gms.maps.model.*
//import com.google.maps.android.compose.*
//import androidx.core.graphics.createBitmap
import com.example.accessmap.ui.theme.AccessMapTheme
import com.google.firebase.FirebaseApp


enum class AccessibilityType {
    MOBILITY, VISION, HEARING, COGNITIVE
}

data class LocationMarker(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val name: String = "",
    val accessibilityType: String = ""
) {
    fun toLatLng() = LatLng(latitude, longitude)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            AccessMapTheme {
                AppNavigator()
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    val selectedFilters = remember { mutableStateListOf<AccessibilityType>() }

    NavHost(navController = navController, startDestination = "map") {
        composable("map") {
            MapScreen(navController = navController, selectedFilters = selectedFilters)
        }
        composable("filters") {
            FilterScreen(navController = navController, selectedFilters = selectedFilters)
        }
        composable("submitReview") {
            ReviewFormScreen()
        }
        composable("viewReviews") {
            ReviewListScreen()
        }
    }
}

