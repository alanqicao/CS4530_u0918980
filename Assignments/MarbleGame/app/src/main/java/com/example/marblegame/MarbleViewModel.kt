package com.example.marblegame

import android.view.Surface
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.exp

class MarbleViewModel : ViewModel() {

    private val _state = MutableStateFlow(MarbleState())
    val state = _state.asStateFlow()

    private var maxX = 0f
    private var maxY = 0f
    private var hasBounds = false

    private var velocityX = 0f
    private var velocityY = 0f

    private var lastTimestamp = 0L
    private var lastRotation: Int? = null

    fun setBounds(xLimit: Float, yLimit: Float) {
        maxX = xLimit.coerceAtLeast(0f)
        maxY = yLimit.coerceAtLeast(0f)

        val old = _state.value

        _state.value = old.copy(
            x = if (hasBounds) {
                old.x.coerceIn(0f, maxX)
            } else {
                maxX / 2f
            },
            y = if (hasBounds) {
                old.y.coerceIn(0f, maxY)
            } else {
                maxY / 2f
            }
        )

        hasBounds = true
        resetMotion()
    }

    fun resetMotion() {
        lastTimestamp = 0L
        lastRotation = null
        velocityX = 0f
        velocityY = 0f
    }

    fun showError(message: String?) {
        _state.value = _state.value.copy(error = message)
    }

    fun onGravity(reading: GravityReading, rotation: Int) {
        if (!hasBounds) return

        if (lastTimestamp == 0L || lastRotation != rotation) {
            resetMotion()
            lastTimestamp = reading.timestamp
            lastRotation = rotation
            return
        }

        val dt = (reading.timestamp - lastTimestamp) / 1_000_000_000f
        lastTimestamp = reading.timestamp

        if (dt <= 0f || dt > 0.1f) {
            resetMotion()
            return
        }

        // Convert device axes into the current screen axes.
        val (gx, gy) = when (rotation) {
            Surface.ROTATION_0 -> reading.x to reading.y
            Surface.ROTATION_90 -> -reading.y to reading.x
            Surface.ROTATION_180 -> -reading.x to -reading.y
            Surface.ROTATION_270 -> reading.y to -reading.x
            else -> error("Unknown screen rotation: $rotation")
        }

        val scale = 100f
        val damping = exp(-2f * dt)

        velocityX = (velocityX - gx * scale * dt) * damping
        velocityY = (velocityY + gy * scale * dt) * damping

        val old = _state.value

        val nextX = old.x + velocityX * dt
        val nextY = old.y + velocityY * dt

        val x = nextX.coerceIn(0f, maxX)
        val y = nextY.coerceIn(0f, maxY)

        if (nextX <= 0f || nextX >= maxX) {
            velocityX = 0f
        }

        if (nextY <= 0f || nextY >= maxY) {
            velocityY = 0f
        }

        _state.value = old.copy(x = x, y = y)
    }
}