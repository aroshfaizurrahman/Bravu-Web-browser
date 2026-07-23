package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PrankLogEntry
import com.example.data.PrankMode
import com.example.data.PrankSettings
import com.example.ui.theme.BravuDarkBg
import com.example.ui.theme.BravuDarkSurface
import com.example.ui.theme.BravuOrange
import com.example.ui.theme.BravuShieldCyan
import com.example.ui.theme.PrankPink
import com.example.ui.theme.PrankYellow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrankSettingsSheet(
    settings: PrankSettings,
    logs: List<PrankLogEntry>,
    prankCount: Int,
    onSettingsChanged: (PrankSettings) -> Unit,
    onClearLogs: () -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState
) {
    var customMsgText by remember(settings.customMessage) { mutableStateOf(settings.customMessage) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BravuDarkBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Psychology,
                        contentDescription = null,
                        tint = BravuOrange,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Prank Studio Controls",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }

            Divider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Total Prank Counter
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = BravuDarkSurface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Total Pranks Executed",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "$prankCount Victims Pranked",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = PrankYellow
                                )
                            }
                            Icon(
                                imageVector = Icons.Outlined.Celebration,
                                contentDescription = null,
                                tint = PrankPink,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                // Custom Prank Text Entry
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Custom Prank Text",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        OutlinedTextField(
                            value = customMsgText,
                            onValueChange = {
                                customMsgText = it
                                onSettingsChanged(settings.copy(customMessage = it))
                            },
                            placeholder = { Text("You are pranked!") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                // Prank Modes Radio List
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Select Prank Style",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        PrankMode.values().forEach { mode ->
                            Card(
                                onClick = { onSettingsChanged(settings.copy(selectedMode = mode)) },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (settings.selectedMode == mode) BravuOrange.copy(
                                        alpha = 0.15f
                                    ) else BravuDarkSurface
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (settings.selectedMode == mode),
                                        onClick = { onSettingsChanged(settings.copy(selectedMode = mode)) },
                                        colors = RadioButtonDefaults.colors(selectedColor = BravuOrange)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = mode.displayName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color.White
                                        )
                                        Text(
                                            text = mode.description,
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Fake Loading Delay Slider
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = BravuDarkSurface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.Speed,
                                        contentDescription = null,
                                        tint = BravuShieldCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Fake Page Load Delay",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                }
                                Text(
                                    text = "${settings.fakeLoadingMs} ms",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = BravuOrange
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Simulates browser progress bar before triggering prank",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                            Slider(
                                value = settings.fakeLoadingMs.toFloat(),
                                onValueChange = {
                                    onSettingsChanged(settings.copy(fakeLoadingMs = it.toLong()))
                                },
                                valueRange = 300f..3000f,
                                steps = 9,
                                colors = SliderDefaults.colors(
                                    thumbColor = BravuOrange,
                                    activeTrackColor = BravuOrange
                                )
                            )
                        }
                    }
                }

                // Toggles (Sound, Vibration, Confetti)
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Prank Effects & Feedback",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        SettingToggleRow(
                            icon = Icons.Filled.MusicNote,
                            title = "Play Sound Fanfare / Chime",
                            subtitle = "Plays sound effect when 'You are pranked!' pops up",
                            checked = settings.soundEnabled,
                            onCheckedChange = { onSettingsChanged(settings.copy(soundEnabled = it)) }
                        )

                        SettingToggleRow(
                            icon = Icons.Filled.Vibration,
                            title = "Haptic Vibration Pulse",
                            subtitle = "Vibrates device upon prank reveal",
                            checked = settings.vibrationEnabled,
                            onCheckedChange = { onSettingsChanged(settings.copy(vibrationEnabled = it)) }
                        )

                        SettingToggleRow(
                            icon = Icons.Outlined.Celebration,
                            title = "Confetti Particle Blast",
                            subtitle = "Show festive falling confetti particles on screen",
                            checked = settings.confettiEnabled,
                            onCheckedChange = { onSettingsChanged(settings.copy(confettiEnabled = it)) }
                        )
                    }
                }

                // Victim Prank Logs Section
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Victim Prank Log History",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            if (logs.isNotEmpty()) {
                                IconButton(onClick = onClearLogs, modifier = Modifier.size(28.dp)) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Clear Logs",
                                        tint = PrankPink
                                    )
                                }
                            }
                        }

                        if (logs.isEmpty()) {
                            Text(
                                text = "No pranks recorded yet. Hand your phone to a friend and tell them to search anything on Bravu Browser!",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        } else {
                            logs.forEach { log ->
                                LogTile(log = log)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BravuOrange),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Save & Close Prank Studio",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun SettingToggleRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BravuDarkSurface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BravuShieldCyan,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(checkedThumbColor = BravuOrange)
            )
        }
    }
}

@Composable
fun LogTile(log: PrankLogEntry) {
    val formatter = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }
    val dateStr = remember(log.timestamp) { formatter.format(Date(log.timestamp)) }

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = BravuDarkSurface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = log.attemptedUrl,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = BravuShieldCyan
                )
                Text(
                    text = dateStr,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Style: ${log.modeName}",
                fontSize = 11.sp,
                color = PrankYellow
            )
        }
    }
}
