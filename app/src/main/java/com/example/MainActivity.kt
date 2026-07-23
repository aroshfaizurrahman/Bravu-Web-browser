package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.PrankViewModel
import com.example.ui.components.BravuHeaderBar
import com.example.ui.components.BravuHomeContent
import com.example.ui.components.FakeWebviewPage
import com.example.ui.components.PrankOverlayDialog
import com.example.ui.components.PrankSettingsSheet
import com.example.ui.theme.BravuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BravuTheme {
                BravuApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BravuApp(viewModel: PrankViewModel = viewModel()) {
    val inputUrl by viewModel.inputUrl.collectAsState()
    val displayUrl by viewModel.currentDisplayUrl.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val loadingProgress by viewModel.loadingProgress.collectAsState()
    val isPrankVisible by viewModel.isPrankVisible.collectAsState()
    val activePrankUrl by viewModel.activePrankUrl.collectAsState()
    val isSettingsOpen by viewModel.isSettingsOpen.collectAsState()
    val isIncognito by viewModel.isIncognito.collectAsState()
    val tabCount by viewModel.tabCount.collectAsState()

    val settings by viewModel.settings.collectAsState()
    val logs by viewModel.logs.collectAsState()
    val prankCount by viewModel.prankCount.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header Bar
            BravuHeaderBar(
                inputUrl = inputUrl,
                onUrlChange = { viewModel.onUrlInputChanged(it) },
                onGoClick = { viewModel.openUrlOrSearch(it) },
                isLoading = isLoading,
                loadingProgress = loadingProgress,
                isIncognito = isIncognito,
                tabCount = tabCount,
                onIncognitoToggle = { viewModel.toggleIncognito() },
                onNewTab = { viewModel.addNewTab() },
                onOpenSettings = { viewModel.openSettings() },
                onOpenLogs = { viewModel.openSettings() }
            )

            // Main View Container
            Box(modifier = Modifier.weight(1f)) {
                if (displayUrl == "bravu://home" || displayUrl == "https://bravu.search") {
                    BravuHomeContent(
                        onLinkClick = { viewModel.openUrlOrSearch(it) }
                    )
                } else {
                    FakeWebviewPage(
                        displayUrl = displayUrl,
                        onLinkClick = { viewModel.openUrlOrSearch(it) }
                    )
                }
            }
        }

        // Prank Reveal Dialog ("You are pranked!")
        if (isPrankVisible) {
            PrankOverlayDialog(
                attemptedUrl = activePrankUrl,
                prankSettings = settings,
                prankCount = prankCount,
                onDismiss = { viewModel.dismissPrank() },
                onOpenSettings = { viewModel.openSettings() }
            )
        }

        // Stealth Prank Studio Settings Sheet
        if (isSettingsOpen) {
            PrankSettingsSheet(
                settings = settings,
                logs = logs,
                prankCount = prankCount,
                onSettingsChanged = { viewModel.updateSettings(it) },
                onClearLogs = { viewModel.clearLogs() },
                onDismiss = { viewModel.closeSettings() },
                sheetState = sheetState
            )
        }
    }
}
