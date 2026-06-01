package com.delwin.quicky

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.*

data class ChannelShortcut(
    val name: String,
    val videoUrl: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    colors = SurfaceDefaults.colors(containerColor = Color(0xFF090A0F))
                ) {
                    TvHelloScreen()
                }
            }
        }
    }
}

@Composable
fun TvHelloScreen() {
    val context = LocalContext.current
    
    // Multi-layered visual depth for the TV background
    val bgBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF06070C),
            Color(0xFF10121E),
            Color(0xFF040508)
        )
    )

    val channels = listOf(
        ChannelShortcut(
            name = "Asianet News",
            videoUrl = "https://www.youtube.com/watch?v=coYw-N1ES1E"
        ),
        ChannelShortcut(
            name = "Shalom TV",
            videoUrl = "https://www.youtube.com/watch?v=F3wz5J2871s"
        ),
        ChannelShortcut(
            name = "Goodness TV",
            videoUrl = "https://www.youtube.com/watch?v=l4gWq33b66k"
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush),
        contentAlignment = Alignment.Center
    ) {
        // High-end ambient neon light behind the list container
        Box(
            modifier = Modifier
                .size(700.dp, 400.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0x1F00F2FE),
                            Color(0x0800F2FE),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            channels.forEach { channel ->
                ChannelCard(
                    channel = channel,
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(channel.videoUrl))
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Safe fallback
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ChannelCard(
    channel: ChannelShortcut,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    // Smooth TV scale animation with a premium fluid bounce
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.07f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cardScale"
    )

    // Animated container background color for high-end glassmorphism
    val containerColor by animateColorAsState(
        targetValue = if (isFocused) Color(0xFF1E2135).copy(alpha = 0.90f) else Color(0xFF141624).copy(alpha = 0.55f),
        animationSpec = tween(250),
        label = "containerColor"
    )

    // Glowing border gradient brushes
    val activeBorderBrush = Brush.linearGradient(
        colors = listOf(Color(0xFF00F2FE), Color(0xFF4FACFE))
    )
    val inactiveBorderBrush = Brush.linearGradient(
        colors = listOf(Color.White.copy(alpha = 0.08f), Color.White.copy(alpha = 0.04f))
    )

    Row(
        modifier = Modifier
            .scale(scale)
            .width(560.dp)
            .background(containerColor, shape = RoundedCornerShape(24.dp))
            .border(
                border = BorderStroke(
                    width = if (isFocused) 2.5.dp else 1.dp,
                    brush = if (isFocused) activeBorderBrush else inactiveBorderBrush
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 36.dp, vertical = 26.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // High-end play indicator (Sleek inner play icon that morphs to active state)
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(
                    brush = if (isFocused) {
                        Brush.linearGradient(listOf(Color(0xFF00F2FE), Color(0xFF4FACFE)))
                    } else {
                        Brush.linearGradient(listOf(Color(0xFF282B3E), Color(0xFF1E212E)))
                    },
                    shape = CircleShape
                )
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isFocused) Color.White.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.08f)
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(13.dp)
                    .offset(x = 1.dp) // Optical centering offset for play symbol
            ) {
                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width, size.height / 2f)
                    lineTo(0f, size.height)
                    close()
                }
                drawPath(
                    path = path,
                    color = if (isFocused) Color(0xFF0A0B10) else Color.White.copy(alpha = 0.85f)
                )
            }
        }

        Spacer(modifier = Modifier.width(32.dp))

        Text(
            text = channel.name,
            color = if (isFocused) Color.White else Color.White.copy(alpha = 0.90f),
            fontSize = 34.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.5.sp
        )
    }
}
