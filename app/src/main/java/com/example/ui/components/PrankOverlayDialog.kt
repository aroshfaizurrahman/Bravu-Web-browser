package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.PrankMode
import com.example.data.PrankSettings
import com.example.ui.theme.BravuOrange
import com.example.ui.theme.BravuShieldCyan
import com.example.ui.theme.PrankPink
import com.example.ui.theme.PrankPurple
import com.example.ui.theme.PrankYellow

@Composable
fun PrankOverlayDialog(
    attemptedUrl: String,
    prankSettings: PrankSettings,
    prankCount: Int,
    onDismiss: () -> Unit,
    onOpenSettings: () -> Unit
) {
    var animateScale by remember { mutableStateOf(0.85f) }
    val scaleAnim by animateFloatAsState(
        targetValue = animateScale,
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        label = "prank_scale"
    )

    LaunchedEffect(Unit) {
        animateScale = 1.0f
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF10121A)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF2A103D),
                                Color(0xFF10121A),
                                Color(0xFF1A0A26)
                            )
                        )
                    )
            ) {
                // Confetti animation background across full screen
                if (prankSettings.confettiEnabled) {
                    ConfettiCanvas(particleCount = 100)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                        .scale(scaleAnim)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        // Top Bar with Close button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = PrankPink.copy(alpha = 0.25f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, PrankPink)
                            ) {
                                Text(
                                    text = "🎉 PRANK ALERT #${prankCount}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = PrankPink,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }

                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.White.copy(alpha = 0.1f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Close Prank",
                                    tint = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        // Hero Icon / Emoji Box
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(RoundedCornerShape(32.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(PrankPurple, PrankPink, BravuOrange)
                                    )
                                )
                                .padding(3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(30.dp))
                                    .background(Color(0xFF1A1228)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🤡",
                                    fontSize = 58.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Headline 1: "🤡 You are pranked"
                        val headlineText = when (prankSettings.selectedMode) {
                            PrankMode.CLASSIC -> if (prankSettings.customMessage.startsWith("🤡")) prankSettings.customMessage else "🤡 ${prankSettings.customMessage}"
                            PrankMode.VIRUS_ALERT -> "🤡 ⚠️ VIRUS DETECTED!\nYOU ARE PRANKED!"
                            PrankMode.ERROR_404 -> "🤡 404 PAGE NOT FOUND\nYOU ARE PRANKED!"
                            PrankMode.RICKROLL -> "🤡 NEVER GONNA GIVE YOU UP!\nYOU ARE PRANKED!"
                            PrankMode.CUSTOM -> if (prankSettings.customMessage.startsWith("🤡")) prankSettings.customMessage else "🤡 ${prankSettings.customMessage}"
                        }

                        Text(
                            text = headlineText,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = PrankYellow,
                            textAlign = TextAlign.Center,
                            lineHeight = 38.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Headline 2: "You idiot thought this is a real browser"
                        Text(
                            text = "You idiot thought this is a real browser",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Display victim attempted URL
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF1C182A)
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF322A4A)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Attempted URL:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = attemptedUrl,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BravuShieldCyan,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Bravu Browser is a prank app! Every single search or link opened reveals this screen.",
                            fontSize = 12.sp,
                            color = Color.LightGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }

                    // Bottom Action Buttons
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, bottom = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BravuOrange,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.EmojiEmotions,
                                contentDescription = null,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Dismiss & Try Another Link",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = {
                                onDismiss()
                                onOpenSettings()
                            },
                            shape = RoundedCornerShape(24.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, PrankPurple.copy(alpha = 0.6f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = null,
                                tint = PrankPurple,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Prank Studio (Change Message)",
                                fontSize = 14.sp,
                                color = PrankPurple,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
