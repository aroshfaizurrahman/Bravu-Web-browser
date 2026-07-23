package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BravuOrange
import com.example.ui.theme.BravuShieldCyan

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BravuHeaderBar(
    inputUrl: String,
    onUrlChange: (String) -> Unit,
    onGoClick: (String) -> Unit,
    isLoading: Boolean,
    loadingProgress: Float,
    isIncognito: Boolean,
    tabCount: Int,
    onIncognitoToggle: () -> Unit,
    onNewTab: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenLogs: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var menuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                if (isIncognito) Color(0xFF14141E) else MaterialTheme.colorScheme.surface
            )
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Shield Icon (With long-press hint for secret Prank Studio)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isIncognito) Color(0xFF262638) else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .combinedClickable(
                        onClick = { onGoClick("https://bravu.search?q=bravu+privacy+shields") },
                        onLongClick = { onOpenSettings() }
                    )
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Shield,
                        contentDescription = "Bravu Shield Protection",
                        tint = BravuOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "1.4k",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BravuShieldCyan
                    )
                }
            }

            // Address bar
            OutlinedTextField(
                value = inputUrl,
                onValueChange = onUrlChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Search or type URL",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Secure connection",
                        tint = if (isIncognito) BravuOrange else BravuShieldCyan,
                        modifier = Modifier.size(16.dp)
                    )
                },
                trailingIcon = {
                    if (inputUrl.isNotEmpty() && inputUrl != "bravu://home") {
                        IconButton(
                            onClick = {
                                keyboardController?.hide()
                                onGoClick(inputUrl)
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowForward,
                                contentDescription = "Navigate to URL",
                                tint = BravuOrange,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(
                    onGo = {
                        keyboardController?.hide()
                        onGoClick(inputUrl)
                    }
                ),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BravuOrange,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = if (isIncognito) Color(0xFF1F1F2E) else MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = if (isIncognito) Color(0xFF1F1F2E) else MaterialTheme.colorScheme.surfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp)
            )

            // Tabs button
            Surface(
                onClick = onNewTab,
                shape = RoundedCornerShape(8.dp),
                color = if (isIncognito) Color(0xFF2B2B3D) else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "$tabCount",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Menu overflow
            Box {
                IconButton(
                    onClick = { menuExpanded = true },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "More Options",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    DropdownMenuItem(
                        text = { Text("New Tab") },
                        leadingIcon = { Icon(Icons.Filled.Add, contentDescription = null) },
                        onClick = {
                            menuExpanded = false
                            onNewTab()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(if (isIncognito) "Standard Mode" else "Private Mode") },
                        leadingIcon = { Icon(Icons.Outlined.Visibility, contentDescription = null) },
                        onClick = {
                            menuExpanded = false
                            onIncognitoToggle()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Prank History Log") },
                        leadingIcon = { Icon(Icons.Outlined.History, contentDescription = null) },
                        onClick = {
                            menuExpanded = false
                            onOpenLogs()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Prank Studio (Secret Controls)") },
                        leadingIcon = { Icon(Icons.Outlined.Settings, contentDescription = null, tint = BravuOrange) },
                        onClick = {
                            menuExpanded = false
                            onOpenSettings()
                        }
                    )
                }
            }
        }

        // Animated fake loading bar
        AnimatedVisibility(visible = isLoading) {
            LinearProgressIndicator(
                progress = { loadingProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .height(3.dp),
                color = BravuOrange,
                trackColor = Color.Transparent
            )
        }
    }
}
