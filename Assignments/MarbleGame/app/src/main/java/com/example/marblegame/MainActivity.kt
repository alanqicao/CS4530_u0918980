package com.example.marblegame

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.marblegame.ui.theme.MarbleGameTheme
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var sensorManager: SensorManager
    private lateinit var marbleViewModel: MarbleViewModel

    private var sensorJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sensorManager =
            getSystemService(Context.SENSOR_SERVICE) as SensorManager

        marbleViewModel =
            ViewModelProvider(this)[MarbleViewModel::class.java]

        setContent {
            MarbleGameTheme {
                MarbleScreen(viewModel = marbleViewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        marbleViewModel.resetMotion()

        // AI-assisted code: sensor selection using the assignment's workaround.
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
            ?: sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (sensor == null) {
            marbleViewModel.showError(
                "Neither a gravity sensor nor an accelerometer is available."
            )
            return
        }

        marbleViewModel.showError(null)

        sensorJob = lifecycleScope.launch(
            start = CoroutineStart.UNDISPATCHED
        ) {
            sensorManager.gravityFlow(sensor)
                .catch { exception ->
                    marbleViewModel.showError(
                        "Motion sensor failed: ${exception.message}"
                    )
                }
                .collect { reading ->
                    val rotation = window.decorView.display.rotation

                    marbleViewModel.onGravity(
                        reading = reading,
                        rotation = rotation
                    )
                }
        }
    }

    override fun onPause() {
        sensorJob?.cancel()
        sensorJob = null

        marbleViewModel.resetMotion()

        super.onPause()
    }
}