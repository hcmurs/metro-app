package org.com.hcmurs.ui.screens.changelanguage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.com.hcmurs.R
import org.com.hcmurs.ui.theme.PrimaryGreen
import org.com.hcmurs.utils.CurrencyManager
import org.com.hcmurs.utils.LanguageManager

data class LanguageOption(
    val code: String,
    val name: String,
    val localizedName: String,
    val flag: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeLanguageScreen(
    navController: NavController,
    currencyManager: CurrencyManager
) {
    val context = LocalContext.current
    val currentLanguage = LanguageManager.getLocale(context)
    val isUpdatingRate by currencyManager.isLoading.collectAsState()
    
    val languageOptions = listOf(
        LanguageOption(
            code = "vi",
            name = "Vietnamese",
            localizedName = "Tiếng Việt",
            flag = "\uD83C\uDDFB\uD83C\uDDF3"
        ),
        LanguageOption(
            code = "en",
            name = "English",
            localizedName = "English",
            flag = "\uD83C\uDDFA\uD83C\uDDF8"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.change_language),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back to Home",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGreen,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.select_language),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            
            items(languageOptions) { language ->
                LanguageOptionItem(
                    language = language,
                    isSelected = currentLanguage == language.code,
                    isUpdatingCurrency = isUpdatingRate && currentLanguage != language.code,
                    onLanguageSelected = { selectedLanguage ->
                        LanguageManager.setLocale(context, selectedLanguage.code)
                        // Recreate activity to apply language change
                        (context as? android.app.Activity)?.recreate()
                    }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F5F5)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.language_note_title),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.language_note_description),
                            fontSize = 12.sp,
                            color = Color.Gray,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LanguageOptionItem(
    language: LanguageOption,
    isSelected: Boolean,
    isUpdatingCurrency: Boolean = false,
    onLanguageSelected: (LanguageOption) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onLanguageSelected(language) },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PrimaryGreen.copy(alpha = 0.1f) else Color.White
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, PrimaryGreen)
        } else {
            androidx.compose.foundation.BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f))
        },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = language.flag,
                fontSize = 24.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = language.localizedName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) PrimaryGreen else Color.Black
                )
                Text(
                    text = language.name,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                if (isUpdatingCurrency) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.updating_price),
                        fontSize = 12.sp,
                        color = PrimaryGreen,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
            
            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = PrimaryGreen,
                    modifier = Modifier.size(24.dp)
                )
            } else if (isUpdatingCurrency) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = PrimaryGreen
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChangeLanguageScreenPreview() {
    // For preview, we'll create a mock CurrencyManager
    // In real usage, this will be injected via Hilt
    // ChangeLanguageScreen(navController = rememberNavController(), currencyManager = mockCurrencyManager)
}