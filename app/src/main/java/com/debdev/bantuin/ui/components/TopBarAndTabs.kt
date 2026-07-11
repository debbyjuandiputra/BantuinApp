package com.debdev.bantuin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.debdev.bantuin.model.FeatureCategory

@Composable
fun BantuinTopBar(
    onMenuClick: () -> Unit,
    onViewProfile: () -> Unit,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit
) {
    var profileMenuExpanded by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    TopAppBar(
        title = {
            Text("Bantuin", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            Box {
                IconButton(onClick = { profileMenuExpanded = true }) {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Profil")
                }
                DropdownMenu(expanded = profileMenuExpanded, onDismissRequest = { profileMenuExpanded = false }) {
                    DropdownMenuItem(onClick = { profileMenuExpanded = false; onViewProfile() }) {
                        Text("Lihat Profil")
                    }
                    DropdownMenuItem(onClick = { profileMenuExpanded = false; onEditProfile() }) {
                        Text("Edit Profil")
                    }
                    DropdownMenuItem(onClick = { profileMenuExpanded = false; onLogout() }) {
                        Text("Keluar")
                    }
                }
            }
        },
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface,
        elevation = 2.dp
    )
}

@Composable
fun CategoryTabsRow(
    selected: FeatureCategory,
    onSelect: (FeatureCategory) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FeatureCategory.values().forEach { category ->
            CategoryTabItem(
                label = category.label,
                isSelected = category == selected,
                onClick = { onSelect(category) }
            )
        }
    }
}

@Composable
private fun CategoryTabItem(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(end = 20.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Box(
            modifier = Modifier
                .height(3.dp)
                .width(if (isSelected) 28.dp else 0.dp)
                .background(MaterialTheme.colors.primary, shape = androidx.compose.foundation.shape.RoundedCornerShape(2.dp))
        )
    }
}
