package org.com.hcmurs.ui.screens.metro.account

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.material3.DatePicker


private val LightGray = Color(0xFFF0F0F0)
private val TextSecondaryColor = Color(0xFF757575)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterFormScreen(
    navController: NavController,
    viewModel: RegisterFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Xử lý sau khi submit
    LaunchedEffect(key1 = uiState.submissionSuccess, key2 = uiState.errorMessage) {
        if (uiState.submissionSuccess) {
            Toast.makeText(context, "Gửi yêu cầu thành công!", Toast.LENGTH_LONG).show()
            navController.popBackStack()
        }
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearMessages()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Xác thực sinh viên", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Vui lòng cung cấp thông tin dưới đây để xác thực tài khoản sinh viên và hưởng các ưu đãi.",
                fontSize = 14.sp,
                color = TextSecondaryColor,
                textAlign = TextAlign.Center
            )

            // Mục chọn ảnh thẻ sinh viên
            ImagePicker(
                label = "Ảnh mặt trước thẻ sinh viên",
                selectedImageUri = uiState.studentCardImageUri,
                onImageSelected = { viewModel.onStudentCardImageSelected(it) }
            )

            // Mục chọn ảnh CCCD
            ImagePicker(
                label = "Ảnh mặt trước CCCD/CMND",
                selectedImageUri = uiState.citizenCardImageUri,
                onImageSelected = { viewModel.onCitizenCardImageSelected(it) }
            )

            // Mục chọn ngày hết hạn
            DatePicker(
                label = "Ngày hết hạn trên thẻ sinh viên",
                selectedDate = uiState.endDate,
                onDateSelected = { viewModel.onEndDateSelected(it) }
            )

            // Trường nhập nội dung (tùy chọn)
            OutlinedTextField(
                value = uiState.content,
                onValueChange = { viewModel.onContentChange(it) },
                label = { Text("Ghi chú (tùy chọn)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Nút Gửi yêu cầu
            Button(
                onClick = { viewModel.submitRequest(context) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Gửi yêu cầu", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Composable cho việc chọn ảnh
@Composable
private fun ImagePicker(
    label: String,
    selectedImageUri: Uri?,
    onImageSelected: (Uri?) -> Unit
) {
    val launcher = rememberLauncherForActivityResult (
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageSelected(uri)
    }

    Column {
        Text(label, fontWeight = FontWeight.SemiBold, color = DarkGreen)
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(LightGray)
                .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Selected Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.CloudUpload, contentDescription = "Upload", tint = TextSecondaryColor, modifier = Modifier.size(48.dp))
                    Text("Nhấn để chọn ảnh", color = TextSecondaryColor)
                }
            }
        }
    }
}

// Composable cho việc chọn ngày
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePicker(
    label: String,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DatePickerDialog (
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val date = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                        onDateSelected(date)
                    }
                    showDialog = false
                }) { Text("Chọn") }
            },
            dismissButton = {
                Button (onClick = { showDialog = false }) { Text("Hủy") }
            }
        ) {
            DatePicker (state = datePickerState)
        }
    }

    Column {
        Text(label, fontWeight = FontWeight.SemiBold, color = DarkGreen)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = selectedDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "",
            onValueChange = {},
            label = { Text("Chọn ngày") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true },
            readOnly = true,
            enabled = false, // Vô hiệu hóa để ngăn bàn phím hiện lên
            leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Date") },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = TextSecondaryColor,
                disabledBorderColor = Color.Gray,
                disabledLeadingIconColor = PrimaryGreen
            )
        )
    }
}