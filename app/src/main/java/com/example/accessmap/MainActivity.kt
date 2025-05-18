package com.example.accessmap

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import com.example.accessmap.ui.theme.AccessMapTheme
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
        }

        setContent {
            AccessMapTheme {
                val sharedViewModel: SharedViewModel = viewModel()

                // ✅ Stable launcher
                val launcher = rememberLauncherForActivityResult(StartActivityForResult()) { result: ActivityResult ->
                    if (result.resultCode == RESULT_OK && result.data != null) {
                        val place = Autocomplete.getPlaceFromIntent(result.data!!)
                        Log.d("PlaceSelection", "Place: ${place.name}, ${place.latLng}")
                        sharedViewModel.selectedPlace.value = place
                    }
                }

                AppNavigator(
                    sharedViewModel = sharedViewModel,
                    onSearchClicked = {
                        val intent = Autocomplete.IntentBuilder(
                            AutocompleteActivityMode.FULLSCREEN,
                            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
                        ).build(this)
                        launcher.launch(intent)
                    }
                )
            }
        }
    }
}
