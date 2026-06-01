package com.delwin.quicky

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

    // Optimization: Wrapped in remember block to prevent list allocation on every single recomposition
    val channels = remember {
        listOf(
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
    }

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
            verticalArrangement = Arrangement.spacedBy(36.dp)
        ) {
            channels.forEach { channel ->
                ChannelTextItem(
                    channel = channel,
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(channel.videoUrl))
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Production fallback: Notify if device fails to open the video URL
                            Toast.makeText(context, "Could not open video channel", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ChannelTextItem(
    channel: ChannelShortcut,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    // Smooth TV scale animation with a premium fluid bounce
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.15f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "textScale"
    )

    // Animated text color for high-end feel: dim when unfocused, glowing cyan when focused
    val textColor by animateColorAsState(
        targetValue = if (isFocused) Color(0xFF00F2FE) else Color.White.copy(alpha = 0.50f),
        animationSpec = tween(250),
        label = "textColor"
    )

    Text(
        text = channel.name,
        color = textColor,
        fontSize = 44.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 0.5.sp,
        modifier = Modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 24.dp, vertical = 12.dp)
    )
}
