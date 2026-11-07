package com.example.marblegame

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

fun SensorManager.gravityFlow(sensor: Sensor): Flow<GravityReading> = callbackFlow {
    require(
        sensor.type == Sensor.TYPE_GRAVITY ||
                sensor.type == Sensor.TYPE_ACCELEROMETER
    ) {
        "Unsupported sensor type: ${sensor.type}"
    }

    val alpha = 0.8f

    var gravityX = 0f
    var gravityY = 0f
    var initialized = false

    val listener = object : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent) {
            if (sensor.type == Sensor.TYPE_ACCELEROMETER && initialized) {
                gravityX =
                    alpha * gravityX + (1f - alpha) * event.values[0]

                gravityY =
                    alpha * gravityY + (1f - alpha) * event.values[1]
            } else {
                gravityX = event.values[0]
                gravityY = event.values[1]
                initialized = true
            }

            val reading = GravityReading(
                x = gravityX,
                y = gravityY,
                timestamp = event.timestamp
            )

            trySend(reading)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
    }

    val registered = registerListener(
        listener,
        sensor,
        SensorManager.SENSOR_DELAY_GAME
    )

    if (!registered) {
        close(
            IllegalStateException(
                "Could not register sensor: ${sensor.name}"
            )
        )
        return@callbackFlow
    }

    awaitClose {
        unregisterListener(listener)
    }
}.conflate()