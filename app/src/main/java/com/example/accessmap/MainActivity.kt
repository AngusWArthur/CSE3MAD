package com.example.accessmap

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.accessmap.ui.theme.AccessMapTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
