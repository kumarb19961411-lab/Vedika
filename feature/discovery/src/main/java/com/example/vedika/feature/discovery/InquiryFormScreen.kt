package com.example.vedika.feature.discovery

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InquiryFormScreen(
    vendorId: String,
    onNavigateBack: () -> Unit,
    onSuccess: () -> Unit
) {
    var date by remember { mutableStateOf("") }
    var guests by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Send Inquiry") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    "Inquiring for Vendor ID: $vendorId",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Preferred Date") },
                    placeholder = { Text("DD-MM-YYYY") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = guests,
                    onValueChange = { guests = it },
                    label = { Text("Estimated Guest Count") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Your Message") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(32.dp))

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.width(32.dp))
                } else {
                    Button(
                        onClick = {
                            isLoading = true
                            // Mock submission logic or navigate to success state
                            isSuccess = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = date.isNotEmpty() && message.isNotEmpty()
                    ) {
                        Text(
                            "Send Inquiry",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }


        }
    }
}
