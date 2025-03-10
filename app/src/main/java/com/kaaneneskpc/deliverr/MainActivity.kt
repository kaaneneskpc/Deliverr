package com.kaaneneskpc.deliverr

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import com.kaaneneskpc.deliverr.ui.theme.DeliverrTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    var showSplashScreen = true
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                showSplashScreen
            }
            setOnExitAnimationListener { screen ->
                createSplashScreenExitAnimation(screen)
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeliverrTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            showSplashScreen = false
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DeliverrTheme {
        Greeting("Android")
    }
}

private fun createSplashScreenExitAnimation(screen: SplashScreenViewProvider) {
    val zoomX = ObjectAnimator.ofFloat(
        screen.iconView,
        View.SCALE_X,
        0.5f,
        0f
    )
    val zoomY = ObjectAnimator.ofFloat(
        screen.iconView,
        View.SCALE_Y,
        0.5f,
        0f
    )
    zoomX.duration = 500
    zoomY.duration = 500
    zoomX.interpolator = OvershootInterpolator()
    zoomY.interpolator = OvershootInterpolator()
    zoomX.doOnEnd {
        screen.remove()
    }
    zoomY.doOnEnd {
        screen.remove()
    }
    zoomY.start()
    zoomX.start()
}