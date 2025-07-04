package org.com.hcmurs.ui.screens.metro.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterFormScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var cccdNumber by remember { mutableStateOf("") }
    var issueDate by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Form đăng ký xác thực CCCD",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGreen
                ),
                navigationIcon = {
                    IconButton (onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Form Fields
            FormTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                placeholder = "Nhập email...",
                icon = Icons.Default.Email
            )

            FormTextField(
                value = birthDate,
                onValueChange = { birthDate = it },
                label = "Ngày sinh",
                placeholder = "Nhập ngày sinh",
                icon = Icons.Default.DateRange
            )

            FormTextField(
                value = cccdNumber,
                onValueChange = { cccdNumber = it },
                label = "Số CCCD hoặc Căn Cước",
                placeholder = "Nhập số CCCD hoặc Căn Cước",
                icon = Icons.Default.CreditCard
            )

            FormTextField(
                value = issueDate,
                onValueChange = { issueDate = it },
                label = "Ngày cấp CCCD hoặc Căn Cước",
                placeholder = "Nhập ngày cấp",
                icon = Icons.Default.DateRange
            )

            FormTextField(
                value = expiryDate,
                onValueChange = { expiryDate = it },
                label = "Ngày hết hạn CCCD hoặc Căn Cước",
                placeholder = "Nhập hết hạn",
                icon = Icons.Default.DateRange
            )

            FormTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = "Họ và tên",
                placeholder = "Nhập họ và tên",
                icon = Icons.Default.Person
            )

            // Gender Dropdown
            Card (
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column  (
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Giới tính *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkGreen
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row (
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FilterChip(
                            onClick = { selectedGender = "Nam" },
                            label = { Text("Nam") },
                            selected = selectedGender == "Nam",
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                        FilterChip(
                            onClick = { selectedGender = "Nữ" },
                            label = { Text("Nữ") },
                            selected = selectedGender == "Nữ",
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                        FilterChip(
                            onClick = { selectedGender = "Khác" },
                            label = { Text("Khác") },
                            selected = selectedGender == "Khác",
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            FormTextField(
                value = address,
                onValueChange = { address = it },
                label = "Địa chỉ",
                placeholder = "Nhập địa chỉ",
                icon = Icons.Default.LocationOn
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Submit Button
            Button (
                onClick = { /* Handle registration */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Icon(
                    Icons.Default.AppRegistration,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Đăng ký ngay",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: ImageVector
) {
    Column {
        Text(
            "$label *",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = DarkGreen
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryGreen,
                focusedLabelColor = PrimaryGreen
            ),
            leadingIcon = {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(20.dp)
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterFormScreenPreview() {
    RegisterFormScreen(navController = NavController(LocalContext.current))
}