package com.example.accessmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.accessmap.ui.theme.AccessMapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AccessMapTheme {
                Surface {
                    Greeting("AccessMap User")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Welcome to AccessMap, $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AccessMapTheme {
        Greeting("Preview")
    }
}
