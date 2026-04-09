package com.example.vedika.feature.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.FilterVintage
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(
    onVerificationSuccess: (isNewPartner: Boolean) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "KalyanaVedika",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            // Mandala Background (Mocked with decorative elements)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Hero Content Layer
                Spacer(modifier = Modifier.height(24.dp))
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.FilterVintage,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Verify Number",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = buildAnnotatedString {
                        append("Enter the 4-digit code sent to ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)) {
                            append("+91 98765 43210")
                        }
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )
                TextButton(
                    onClick = { /* Change Number */ },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Change Number", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
                }

                Spacer(modifier = Modifier.height(48.dp))

                // OTP Input Section
                Surface(
                    modifier = Modifier.fillMaxWidth().widthIn(max = 448.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFBF3E4),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.1f)),
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // OTP 4-Digit Grid
                        Row(
                            modifier = Modifier.fillMaxWidth().widthIn(max = 320.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            repeat(4) { index ->
                                Surface(
                                    modifier = Modifier.size(64.dp, 80.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    color = MaterialTheme.colorScheme.surfaceContainerHigh
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "•",
                                            style = MaterialTheme.typography.displaySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(40.dp))

                        // Actions
                        Button(
                            onClick = { viewModel.verifyOtp(onSuccess = onVerificationSuccess) },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Verify & Login", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(Icons.Default.Login, contentDescription = null)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Didn't receive the code?", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(onClick = { /* Resend */ }) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.tertiary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Resend OTP", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(64.dp))

                // Decorative Element
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.alpha(0.1f)) {
                    Box(modifier = Modifier.width(48.dp).height(1.dp).background(MaterialTheme.colorScheme.outlineVariant))
                    Icon(Icons.Default.SettingsSuggest, contentDescription = null, modifier = Modifier.padding(horizontal = 16.dp))
                    Box(modifier = Modifier.width(48.dp).height(1.dp).background(MaterialTheme.colorScheme.outlineVariant))
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = buildAnnotatedString {
                        append("By logging in, you agree to KalyanaVedika's ")
                        withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append("Terms of Service")
                        }
                        append(" and ")
                        withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append("Privacy Policy")
                        }
                        append(".")
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                // Security Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.alpha(0.6f)
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Secure 256-bit encrypted verification", style = MaterialTheme.typography.labelSmall)
                }
            }

            // Visual Anchor Background Image (Decorative Bottom Right)
            AsyncImage(
                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuB9b1faUIP5RRvaHNnp7h4rXHW_1fLTCPp0JPY2c0jMBDacxi-9CrvrFFC9cukE0XaeRIxvL8-Fz6aS33B18Bhf9sW1xEtQqu4ZVOa-Cq2_C-UPsoEQNSL2kCyfheXyGnJHEjcMRMpxRjLpX9fEqbtaSHITvKmzLve1plIEjnsMjqcdMsdo9PUq_lmgJ8-_W2UFEk92MkhqOF6VO2n4tsNIEZ_U9LpoIIfBdiiS2OgGZcVdUw8FErrc82aH4vWnqWrYErV9Aq807Fbs",
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(160.dp)
                    .clip(RoundedCornerShape(topStart = 100.dp))
                    .alpha(0.2f),
                contentScale = ContentScale.Crop
            )
        }
    }
}
