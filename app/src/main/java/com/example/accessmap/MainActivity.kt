package com.example.accessmap

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import androidx.core.graphics.createBitmap

// Enums for accessibility
enum class AccessibilityType {
    MOBILITY, VISION, HEARING, COGNITIVE
}

// Data class for markers
data class LocationMarker(
    val latLng: LatLng,
    val name: String,
    val accessibilityType: AccessibilityType
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavController,
    selectedFilters: List<AccessibilityType>
) {
    val context = LocalContext.current
    val laTrobe = LatLng(-37.7210, 145.0485)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(laTrobe, 16f)
    }

    val allLocations = listOf(
        LocationMarker(
            latLng = LatLng(-37.72179, 145.04790),
            name = "La Trobe University Main Entrance",
            accessibilityType = AccessibilityType.MOBILITY
        ),
        LocationMarker(
            latLng = LatLng(-37.721739, 145.051924),
            name = "Menzies College",
            accessibilityType = AccessibilityType.COGNITIVE
        ),
        LocationMarker(
            latLng = LatLng(-37.7176975, 145.0491653),
            name = "Car Park 8",
            accessibilityType = AccessibilityType.VISION
        ),
        LocationMarker(
            latLng = LatLng(-37.72180, 145.04860),
            name = "Union Hall",
            accessibilityType = AccessibilityType.HEARING
        )
    )



    val filtered = if (selectedFilters.isEmpty()) {
        allLocations
    } else {
        allLocations.filter { it.accessibilityType in selectedFilters }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AccessMap") },
                actions = {
                    IconButton(onClick = { navController.navigate("filters") }) {
                        Icon(Icons.Filled.FilterList, contentDescription = "Filter")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                filtered.forEach { location ->
                    val iconResId = when (location.accessibilityType) {
                        AccessibilityType.MOBILITY -> R.drawable.ic_accessibility_mobility
                        AccessibilityType.VISION -> R.drawable.ic_accessibility_vision
                        AccessibilityType.HEARING -> R.drawable.ic_accessibility_hearing
                        AccessibilityType.COGNITIVE -> R.drawable.ic_accessibility_cognitive
                    }

                    val bitmapDescriptor = vectorToBitmapDescriptor(context, iconResId)

                    Marker(
                        state = MarkerState(position = location.latLng),
                        title = location.name,
                        icon = bitmapDescriptor
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    navController: NavController,
    selectedFilters: MutableList<AccessibilityType>
) {
    val options = AccessibilityType.entries.toTypedArray()
    val tempFilters = remember { mutableStateListOf<AccessibilityType>().apply { addAll(selectedFilters) } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Accessibility Filters") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        selectedFilters.clear()
                        selectedFilters.addAll(tempFilters)
                        navController.popBackStack()
                    }) {
                        Text("Apply")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .padding(16.dp)) {
            options.forEach { type ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(type.name.replaceFirstChar { it.uppercaseChar() })
                    Checkbox(
                        checked = type in tempFilters,
                        onCheckedChange = {
                            if (it) tempFilters.add(type) else tempFilters.remove(type)
                        }
                    )
                }
            }
        }
    }
}

// Convert vector drawable to bitmap for map marker
fun vectorToBitmapDescriptor(context: android.content.Context, resId: Int): BitmapDescriptor {
    val drawable: Drawable = ResourcesCompat.getDrawable(context.resources, resId, null)
        ?: throw IllegalArgumentException("Resource not found: $resId")

    val bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}
