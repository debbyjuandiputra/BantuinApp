package com.debdev.bantuin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.debdev.bantuin.model.FeatureCategory
import com.debdev.bantuin.model.FeatureItem
import com.debdev.bantuin.model.featureList
import com.debdev.bantuin.ui.components.AppDrawerContent
import com.debdev.bantuin.ui.components.BantuinTopBar
import com.debdev.bantuin.ui.components.CategoryTabsRow
import com.debdev.bantuin.ui.components.FeatureGrid
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(
    isDarkMode: Boolean,
    onToggleMode: () -> Unit,
    onOpenPolicy: () -> Unit,
    onViewProfile: () -> Unit,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit,
    onFeatureClick: (FeatureItem) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(FeatureCategory.SEMUA) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val filteredFeatures = remember(selectedCategory) {
        if (selectedCategory == FeatureCategory.SEMUA) featureList
        else featureList.filter { selectedCategory in it.categories }
    }

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                isDarkMode = isDarkMode,
                onToggleMode = onToggleMode,
                onOpenPolicy = onOpenPolicy,
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                BantuinTopBar(
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onViewProfile = onViewProfile,
                    onEditProfile = onEditProfile,
                    onLogout = onLogout
                )
            }
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                CategoryTabsRow(selected = selectedCategory, onSelect = { selectedCategory = it })
                Divider()
                FeatureGrid(features = filteredFeatures, onFeatureClick = onFeatureClick)
            }
        }
    }
}
