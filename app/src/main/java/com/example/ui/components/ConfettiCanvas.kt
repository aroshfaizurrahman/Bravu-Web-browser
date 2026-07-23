package com.example.ui.components

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.BravuOrange
import com.example.ui.theme.BravuShieldCyan
import com.example.ui.theme.PrankPink
import com.example.ui.theme.PrankPurple
import com.example.ui.theme.PrankYellow
import kotlin.random.Random

private data class ConfettiParticle(
    var x: Float,
    var y: Float,
    var size: Float,
    var speedY: Float,
    var speedX: Float,
    var color: Color,
    var rotation: Float,
    var rotationSpeed: Float
)

@Composable
fun ConfettiCanvas(
    modifier: Modifier = Modifier,
    particleCount: Int = 75
) {
    val colors = listOf(
        BravuOrange,
        BravuShieldCyan,
        PrankPink,
        PrankYellow,
        PrankPurple,
        Color(0xFFFF3D00),
        Color(0xFF00E676)
    )

    val particles = remember {
        mutableStateListOf<ConfettiParticle>().apply {
            repeat(particleCount) {
                add(
                    ConfettiParticle(
                        x = Random.nextFloat(),
                        y = Random.nextFloat() * -1.0f,
                        size = Random.nextFloat() * 20f + 12f,
                        speedY = Random.nextFloat() * 0.012f + 0.006f,
                        speedX = (Random.nextFloat() - 0.5f) * 0.006f,
                        color = colors.random(),
                        rotation = Random.nextFloat() * 360f,
                        rotationSpeed = (Random.nextFloat() - 0.5f) * 10f
                    )
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            withInfiniteAnimationFrameMillis {
                particles.forEach { p ->
                    p.y += p.speedY
                    p.x += p.speedX
                    p.rotation += p.rotationSpeed
                    if (p.y > 1.2f) {
                        p.y = -0.1f
                        p.x = Random.nextFloat()
                    }
                }
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        particles.forEach { p ->
            val drawX = p.x * width
            val drawY = p.y * height

            drawRect(
                color = p.color,
                topLeft = Offset(drawX, drawY),
                size = Size(p.size, p.size * 0.6f)
            )
        }
    }
}
