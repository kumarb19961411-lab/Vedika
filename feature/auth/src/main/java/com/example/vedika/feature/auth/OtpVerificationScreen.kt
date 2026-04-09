package com.example.vedika.feature.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
    onNavigateBack: () -> Unit,
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
                        color = Color(0xFFC2410C),
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF78716A))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White.copy(alpha = 0.8f)
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp, vertical = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                    text = "Verification code sent to +91 ${uiState.phoneNumber}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Change Number",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp).clickable(onClick = onNavigateBack)
                )

                Spacer(modifier = Modifier.height(48.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth().widthIn(max = 448.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFBF3E4).copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.1f))
                ) {
                    Box(modifier = Modifier.padding(32.dp), contentAlignment = Alignment.Center) {
                        androidx.compose.foundation.text.BasicTextField(
                            value = uiState.otp,
                            onValueChange = { 
                                if (it.length <= 4) viewModel.updateOtp(it)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            decorationBox = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    repeat(4) { index ->
                                        val char = uiState.otp.getOrNull(index)?.toString() ?: ""
                                        val isFocused = uiState.otp.length == index
                                        Surface(
                                            modifier = Modifier.size(64.dp, 80.dp),
                                            shape = RoundedCornerShape(16.dp),
                                            color = if (char.isNotEmpty()) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.5f),
                                            border = if (isFocused) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = char,
                                                    style = MaterialTheme.typography.displaySmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        )
                    }
                }

                Text(
                    text = if (uiState.isResendEnabled) "Resend OTP" else "Resend in ${uiState.countdown}s",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (uiState.isResendEnabled) MaterialTheme.colorScheme.primary else Color.Gray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 24.dp).clickable(enabled = uiState.isResendEnabled) {
                        viewModel.resendOtp()
                    }
                )

                if (uiState.error != null) {
                    Text(
                        text = uiState.error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    onClick = { 
                        viewModel.verifyOtp { isNewPartner ->
                            onVerificationSuccess(isNewPartner)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 448.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Verify & Proceed", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    }
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
