package com.canerture.quiz.ui.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
internal fun rememberShake() = remember { ShakeAnim() }

internal class ShakeAnim(
    val animatable: Animatable<Float, AnimationVector1D> = Animatable(0f),
) {
    suspend fun shake() {
        val animationSpec = spring<Float>(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = 5_000_000f)
        val shakeRange = 5f

        with(this.animatable) {
            repeat(5) {
                animateTo(-shakeRange, animationSpec)
                animateTo(shakeRange, animationSpec)
            }
            animateTo(0f, animationSpec)
        }
    }
}

internal fun Modifier.shake(shake: ShakeAnim) = graphicsLayer {
    this.translationX = shake.animatable.value
}