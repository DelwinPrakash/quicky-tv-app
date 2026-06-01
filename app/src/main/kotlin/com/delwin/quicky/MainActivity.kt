package com.delwin.quicky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0F1016)
                ) {
                    TvHelloScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvHelloScreen() {
    val greetings = listOf(
        "Hello, World!",
        "Welcome to Android TV!",
        "Hello from Jetpack Compose!",
        "Ready for Android OS 11!",
        "Super Fast & Responsive!"
    )
    var greetingIndex by remember { mutableStateOf(0) }
    
    // Background gradient setup for premium TV visual depth
    val bgBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0A0B10),
            Color(0xFF131522),
            Color(0xFF07080B)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush),
        contentAlignment = Alignment.Center
    ) {
        // Glowing cyan-purple ambient backdrop light behind the card to create wow factor
        Box(
            modifier = Modifier
                .size(500.dp, 250.dp)
                .offset(y = (-30).dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0x2200F2FE),
                            Color(0x0C9F5AF6),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Branding Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 28.dp)
            ) {
                // Glowing cyan neon logo indicator
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color(0xFF00F2FE), shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "QUICKY TV • LEANBACK SYSTEM",
                    color = Color(0xFF00F2FE),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.5.sp
                )
            }

            // TV Glassmorphic Focusable Greeting Card
            val cardInteractionSource = remember { MutableInteractionSource() }
            val isCardFocused by cardInteractionSource.collectIsFocusedAsState()
            val cardScale by animateFloatAsState(
                targetValue = if (isCardFocused) 1.04f else 1.0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "cardScale"
            )
            val cardBorderColor by animateColorAsState(
                targetValue = if (isCardFocused) Color(0xFF00F2FE) else Color.White.copy(alpha = 0.12f),
                animationSpec = tween(250),
                label = "cardBorder"
            )

            Column(
                modifier = Modifier
                    .scale(cardScale)
                    .width(580.dp)
                    .background(
                        color = if (isCardFocused) Color(0xFF1E2132).copy(alpha = 0.85f) else Color(0xFF141622).copy(alpha = 0.65f),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .border(
                        border = BorderStroke(if (isCardFocused) 2.dp else 1.dp, cardBorderColor),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .clickable(
                        interactionSource = cardInteractionSource,
                        indication = null
                    ) {
                        greetingIndex = (greetingIndex + 1) % greetings.size
                    }
                    .padding(horizontal = 40.dp, vertical = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "HELLO GREETING DEVICE",
                    color = Color.White.copy(alpha = 0.35f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // Large Main Display text for TV 10-foot interface readability
                Text(
                    text = greetings[greetingIndex],
                    color = Color.White,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 52.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                // D-Pad remote control interactive cycle button
                val btnInteractionSource = remember { MutableInteractionSource() }
                val isBtnFocused by btnInteractionSource.collectIsFocusedAsState()
                val btnScale by animateFloatAsState(
                    targetValue = if (isBtnFocused) 1.08f else 1.0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "btnScale"
                )

                Box(
                    modifier = Modifier
                        .scale(btnScale)
                        .background(
                            brush = if (isBtnFocused) {
                                Brush.linearGradient(listOf(Color(0xFF00F2FE), Color(0xFF4FACFE)))
                            } else {
                                Brush.linearGradient(listOf(Color(0xFF282B3E), Color(0xFF1E212E)))
                            },
                            shape = RoundedCornerShape(14.dp)
                        )
                        .border(
                            border = BorderStroke(
                                width = 1.5.dp,
                                color = if (isBtnFocused) Color.White else Color.White.copy(alpha = 0.08f)
                            ),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable(
                            interactionSource = btnInteractionSource,
                            indication = null
                        ) {
                            greetingIndex = (greetingIndex + 1) % greetings.size
                        }
                        .padding(horizontal = 28.dp, vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isBtnFocused) "PRESS SELECT TO SWITCH ✦" else "CYCLE GREETING",
                        color = if (isBtnFocused) Color(0xFF0A0B10) else Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Navigation guide helper text
            Text(
                text = "Use Remote D-PAD to navigate • Press Select key to swap greetings",
                color = Color.White.copy(alpha = 0.25f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )
        }
    }
}
