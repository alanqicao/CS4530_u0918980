package com.example.marblegame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MarbleScreen(viewModel: MarbleViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle(
        minActiveState = Lifecycle.State.RESUMED
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8EDF2))
            .safeDrawingPadding()
    ) {
        val ballSize = minOf(40.dp, maxWidth, maxHeight)

        val maxX = (maxWidth - ballSize).value.coerceAtLeast(0f)
        val maxY = (maxHeight - ballSize).value.coerceAtLeast(0f)

        LaunchedEffect(maxX, maxY) {
            viewModel.setBounds(maxX, maxY)
        }

        val errorMessage = state.error

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(24.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .offset(
                        x = state.x.coerceIn(0f, maxX).dp,
                        y = state.y.coerceIn(0f, maxY).dp
                    )
                    .size(ballSize)
                    .background(
                        color = Color(0xFF1565C0),
                        shape = CircleShape
                    )
            )
        }
    }
}