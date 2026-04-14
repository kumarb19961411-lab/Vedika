package com.example.vedika.core.design.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.vedika.core.design.theme.NotoSerif

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VedikaTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {
        IconButton(onClick = {}) {
            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.Gray)
        }
    },
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.White.copy(alpha = 0.95f),
        titleContentColor = Color(0xFF8B4513) // Heritage Brown
    )
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontFamily = NotoSerif,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = navigationIcon,
        actions = actions,
        colors = colors,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VedikaTabTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {}
) {
    VedikaTopAppBar(
        title = title,
        navigationIcon = {
            // Root tabs have no back button, often show brand or menu
            IconButton(onClick = {}) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.Gray)
            }
        },
        actions = actions,
        modifier = modifier
    )
}
