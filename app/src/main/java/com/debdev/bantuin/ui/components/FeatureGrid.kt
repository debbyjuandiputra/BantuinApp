package com.debdev.bantuin.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.debdev.bantuin.model.FeatureItem
import com.debdev.bantuin.ui.theme.DisabledGray

@Composable
fun FeatureGrid(
    features: List<FeatureItem>,
    onFeatureClick: (FeatureItem) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(features, key = { it.id }) { feature ->
            FeatureCard(feature = feature, onClick = { if (feature.enabled) onFeatureClick(feature) })
        }
    }
}

@Composable
private fun FeatureCard(feature: FeatureItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable(enabled = feature.enabled) { onClick() },
        elevation = if (feature.enabled) 3.dp else 0.dp,
        backgroundColor = if (feature.enabled) MaterialTheme.colors.surface else MaterialTheme.colors.surface.copy(alpha = 0.6f)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = feature.icon,
                contentDescription = feature.title,
                tint = if (feature.enabled) MaterialTheme.colors.primary else DisabledGray,
                modifier = Modifier.size(34.dp).padding(bottom = 8.dp)
            )
            Text(
                text = feature.title,
                fontSize = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                color = if (feature.enabled) MaterialTheme.colors.onSurface else DisabledGray
            )
            if (!feature.enabled) {
                Text(
                    "Segera hadir",
                    fontSize = 9.sp,
                    color = DisabledGray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
