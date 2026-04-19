package com.example.vedika.feature.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@Composable
fun SignupScreen(
    onNavigateToOtp: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isPartnerMode = uiState.accountMode == AccountMode.PARTNER
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val activity = context as? android.app.Activity

    // Stable initialization of the AuthFlow state on screen entry
    LaunchedEffect(Unit) {
        viewModel.setAuthFlow(AuthFlow.SIGN_UP)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(remember { SnackbarHostState() }) },
        content = { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                // Hero Background Section
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuA5btkSk9J9QVVc-C1b2S21nwJ_LX16E4TGovvJHuj6pbAkgqfip-ZVM192uY_8rYo5yuuVIzbYORIUuLCkB0xaar9CVVxtegXrMK-foELDK3dOp_0LIJQg3_v4OGYsdkzosy9t0kY1eUwVhj-UoWLsXrVROuYIsF7-JiC4trHcsKbkAdqWzx9jLsJwGO1sG8IsXdv148DeN3WQUjtFq1pGN8voLWx_0Xsd73KSVf0QApN2RMWJb_NswXTDUOvswUHKrOidbF-l0FUc",
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surface,
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Main Content Shell
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 24.dp, vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Left Side: Branding
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Join Heritage.\nBuild Traditions.",
                            style = MaterialTheme.typography.displaySmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            lineHeight = 48.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "The Modern Tech Partner for India's\nFinest Heritage Venues & Decorators",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Scale Your Business With KalyanaVedika".uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 1.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    // Right Side: Signup Card (Glass Panel)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .widthIn(max = 420.dp),
                        shape = RoundedCornerShape(24.dp),
                        color = Color.White.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                        shadowElevation = 0.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(32.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Sign Up",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Create your account to book or provide event services",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            // Signup Toggle Segment
                            Surface(
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = RoundedCornerShape(24.dp),
                                color = Color.White.copy(alpha = 0.1f),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                            ) {
                                Row(modifier = Modifier.fillMaxSize()) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .padding(4.dp)
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(if (!isPartnerMode) Color.White else Color.Transparent)
                                            .clickable { viewModel.selectAccountMode(AccountMode.USER) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "User Sign Up",
                                            fontWeight = FontWeight.Bold,
                                            color = if (!isPartnerMode) MaterialTheme.colorScheme.primary else Color.White
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .padding(4.dp)
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(if (isPartnerMode) Color.White else Color.Transparent)
                                            .clickable { viewModel.selectAccountMode(AccountMode.PARTNER) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "Partner Sign Up",
                                            fontWeight = FontWeight.Bold,
                                            color = if (isPartnerMode) MaterialTheme.colorScheme.primary else Color.White
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isPartnerMode) "Provide and manage your services" else "Book services for your event",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White.copy(alpha = 0.7f),
                                fontStyle = FontStyle.Italic
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            // Input Form
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Full Name".uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                                )
                                OutlinedTextField(
                                    value = uiState.ownerName,
                                    onValueChange = { viewModel.updateOwnerName(it) },
                                    placeholder = { Text("Enter Your Full Name", color = Color.White.copy(alpha = 0.5f)) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                        focusedBorderColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedTextColor = Color.White
                                    )
                                )
                                
                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    text = "Mobile Number".uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                                )
                                OutlinedTextField(
                                    value = uiState.phoneNumber,
                                    onValueChange = { viewModel.updatePhoneNumber(it) },
                                    placeholder = { Text("Enter Phone Number", color = Color.White.copy(alpha = 0.5f)) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    prefix = { Text("+91 ", color = Color.White) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    singleLine = true,
                                    isError = uiState.error != null,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                        focusedBorderColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedTextColor = Color.White
                                    )
                                )
                                if (uiState.error != null) {
                                    Text(
                                        text = uiState.error ?: "",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = { 
                                    activity?.let {
                                        viewModel.sendOtp(it, onSuccess = onNavigateToOtp) 
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(12.dp),
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
                                        Text("Send OTP", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Decorative Divider
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Box(modifier = Modifier.weight(1f).height(1.dp).background(Color.White.copy(alpha = 0.2f)))
                                Icon(
                                    Icons.Default.Spa,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.4f),
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Box(modifier = Modifier.weight(1f).height(1.dp).background(Color.White.copy(alpha = 0.2f)))
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = buildAnnotatedString {
                                    append("Already have an account? ")
                                    withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                                        append("Sign In")
                                    }
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.clickable { onNavigateToSignIn() }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                                Icon(Icons.Default.HelpOutline, contentDescription = null, tint = Color.White.copy(alpha = 0.6f))
                                Icon(Icons.Default.Language, contentDescription = null, tint = Color.White.copy(alpha = 0.6f))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    )
}
