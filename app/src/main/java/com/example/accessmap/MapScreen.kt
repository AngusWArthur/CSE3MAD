package com.example.accessmap

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import com.example.accessmap.R

@Composable
fun MapScreen(onLogoutClick: () -> Unit) {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-37.721, 145.048), 16f)
    }

    val allMarkers = remember {
        mapOf(
            "Accessible Entry" to MarkerData(
                LatLng(-37.7210, 145.0480),
                R.drawable.ic_accessibility_mobility,
                "Accessible Entry",
                "Ramp and wide door access"
            ),
            "Vision Aid" to MarkerData(
                LatLng(-37.7212, 145.0482),
                R.drawable.ic_accessibility_vision,
                "Vision Aid",
                "Braille signs and high contrast markings"
            ),
            "Cognitive Assistance" to MarkerData(
                LatLng(-37.7214, 145.0484),
                R.drawable.ic_accessibility_cognitive,
                "Cognitive Assistance",
                "Simple signage and friendly assistance"
            ),
            "Hearing Loop" to MarkerData(
                LatLng(-37.7216, 145.0486),
                R.drawable.ic_accessibility_hearing,
                "Hearing Loop",
                "Loop system available inside"
            )
        )
    }

    val selectedCategories = remember {
        mutableStateMapOf(
            "Accessible Entry" to true,
            "Vision Aid" to true,
            "Cognitive Assistance" to true,
            "Hearing Loop" to true
        )
    }

    var showFilterDialog by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            for ((category, markerData) in allMarkers) {
                if (selectedCategories[category] == true) {
                    Marker(
                        state = MarkerState(position = markerData.position),
                        icon = vectorToBitmapDescriptor(context, markerData.iconRes),
                        title = markerData.title,
                        snippet = markerData.snippet
                    )
                }
            }
        }

        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            BasicTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(12.dp),
                singleLine = true
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Button(onClick = { showFilterDialog = true }) {
                    Text("Filter")
                }

                Button(
                    onClick = onLogoutClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("Logout")
                }
            }
        }
    }

    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            confirmButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("Filter Markers") },
            text = {
                Column {
                    selectedCategories.forEach { (category, isChecked) ->
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { selectedCategories[category] = it }
                            )
                            Text(category)
                        }
                    }
                }
            }
        )
    }
}

fun vectorToBitmapDescriptor(context: android.content.Context, resId: Int): BitmapDescriptor {
    val vectorDrawable: Drawable = ContextCompat.getDrawable(context, resId)
        ?: throw IllegalArgumentException("Resource not found: $resId")
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

data class MarkerData(
    val position: LatLng,
    val iconRes: Int,
    val title: String,
    val snippet: String
)
