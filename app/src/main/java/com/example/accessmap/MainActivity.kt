package com.example.accessmap

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.accessmap.ui.theme.AccessMapTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.AddTrace

class MainActivity : ComponentActivity() {

    @AddTrace(name = "onCreateTrace", enabled = true)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Optionally enable performance collection explicitly (if disabled in settings)
        FirebasePerformance.getInstance().isPerformanceCollectionEnabled = true

        setContent {
            AccessMapTheme {
                MapScreen(
                    onLogoutClick = {
                        FirebaseAuth.getInstance().signOut()
                        val loginIntent = Intent(this, LoginActivity::class.java)
                        loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(loginIntent)
                    }
                )
            }
        }
    }
}
