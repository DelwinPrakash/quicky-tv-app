package com.delwin.quicky

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

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
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val context = LocalContext.current

    // Error message state — replaces Toast which is unreliable on Android TV
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Auto-dismiss error overlay after 3 seconds
    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            delay(3_000)
            errorMessage = null
        }
    }

    // Memoized: never reallocated on recomposition
    val bgBrush = remember {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF06070C),
                Color(0xFF10121E),
                Color(0xFF040508)
            )
        )
    }

    val channels = remember {
        listOf(
            ChannelShortcut(
                name = "Asianet News",
                videoUrl = "https://www.youtube.com/@AsianetNews/live"
            ),
            ChannelShortcut(
                name = "Holy Mass",
                videoUrl = "https://www.youtube.com/results?search_query=holy+mass+malayalam+today&sp=EgIIAg%253D%253D"
            ),
            ChannelShortcut(
                name = "Shalom TV",
                videoUrl = "https://www.youtube.com/@ShalomTelevision/live"
            ),
            ChannelShortcut(
                name = "Goodness TV",
                videoUrl = "https://www.youtube.com/@GoodnessTVLive/live"
            ),
            ChannelShortcut(
                name = "Manorama News",
                videoUrl = "https://www.youtube.com/@ManoramaNews/live"
            ),
            ChannelShortcut(
                name = "24 News",
                videoUrl = "https://www.youtube.com/@24NewsHD/live"
            ),
            ChannelShortcut(
                name = "El Ruha",
                videoUrl = "https://youtube.com/playlist?list=PLv24jd6cmGC8PaGPP64Zr9uGPSrVn_Toy"
            )
        )
    }

    val focusRequesters = remember(channels.size) {
        List(channels.size) { FocusRequester() }
    }

    if (focusRequesters.isNotEmpty()) {
        LaunchedEffect(Unit) {
            yield()
            focusRequesters[0].requestFocus()
        }
    }

    val listState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush),
        contentAlignment = Alignment.Center
    ) {
        // Ambient neon glow — purely decorative, drawn below the list
        Box(
            modifier = Modifier
                .size(700.dp, 500.dp)
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

        LazyColumn(
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(vertical = 32.dp),
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth()
        ) {
            itemsIndexed(channels) { index, channel ->
                ChannelTextItem(
                    channel = channel,
                    focusRequester = focusRequesters[index],
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(channel.videoUrl))
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            errorMessage = "Could not open ${channel.name}"
                        }
                    }
                )
            }
        }

        AnimatedVisibility(
            visible = errorMessage != null,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xCC1A1A2E),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 32.dp, vertical = 14.dp)
            ) {
                Text(
                    text = errorMessage ?: "",
                    color = Color(0xFFFF6B6B),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun ChannelTextItem(
    channel: ChannelShortcut,
    focusRequester: FocusRequester,
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

    // Dim when unfocused, glowing cyan when focused
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
            .focusRequester(focusRequester)
            .onKeyEvent { keyEvent ->
                if (
                    keyEvent.type == KeyEventType.KeyUp &&
                    (keyEvent.key == Key.DirectionCenter || keyEvent.key == Key.Enter)
                ) {
                    onClick()
                    true  // event consumed
                } else {
                    false // pass through to system
                }
            }
            .focusable(interactionSource = interactionSource)
            .scale(scale)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    )
}